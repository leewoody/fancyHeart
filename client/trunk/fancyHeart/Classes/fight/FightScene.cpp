//
//  FightScene.cpp
//  fancyHeart
//
//  Created by 秦亮亮 on 14-5-5.
//
//

#include "FightScene.h"
#include "HomeScene.h"

Scene* FightScene::createScene(){
	auto scene = Scene::create();
    auto layer = FightScene::create();
    scene->addChild(layer,0,0);
	return scene;
}

FightScene* FightScene::create()
{
    FightScene* pRet=new FightScene();
    if (pRet && pRet->init("publish/fight/fight.ExportJson")) {
        pRet->autorelease();
        return pRet;
    }
    CC_SAFE_DELETE(pRet);
    return nullptr;
}

bool FightScene::init(const char* fileName){


    if(!BaseUI::init(fileName)){
        return false;
    }
    this->heroNode=Widget::create();//this->layout->getChildByName("hero");
    this->addChild(heroNode,-1);
    this->effectNode=Widget::create();
    this->addChild(effectNode,1);

    Size dSize(1136,640);
    Size size=Director::getInstance()->getWinSize();

    this->bg=Sprite::create("fightBg.png");
    this->addChild(bg,-2);
    
    this->bg->setPosition(Vec2(size.width/2,size.height/2));
    
    float scale=fmin(size.width/dSize.width,size.height/dSize.height);
  
    this->top=this->layout->getChildByName("top");
    top->setScale(scale);
    top->setPosition(Vec2(0,size.height-(dSize.height-top->getPositionY())*(scale)));

    this->skillNode=this->layout->getChildByName("skill");
    this->skillNode->setScale(scale);

    this->bossInfo=top->getChildByName("bossBg");
    this->bossInfo->setVisible(false);
    for(int i=0;i<5;i++){
        Widget* skillIcon=skillNode->getChildByName("skill"+Value(i).asString());
        this->skillIcons.pushBack(skillIcon);
        skillIcon->setVisible(false);
        Widget* bean=skillNode->getChildByName("bean"+Value(i).asString());
        bean->setVisible(false);
        this->beans.pushBack(bean);
        Clip* clip=Clip::create("effect/battle_Flashing box.plist",5);
        this->skillNode->addChild(clip);
        clip->setPosition(skillIcon->getPosition());
        clip->setVisible(false);
        clip->setScale(scale);
        this->skillRims.pushBack(clip);
        
    }
    ui::Button* btn_pause=static_cast<ui::Button*>(top->getChildByName("btn_pause"));
    ui::Button* btn_auto=static_cast<ui::Button*>(skillNode->getChildByName("btn_auto"));
    btn_pause->addTouchEventListener(CC_CALLBACK_2(FightScene::touchButtonEvent,this));
    btn_auto->addTouchEventListener(CC_CALLBACK_2(FightScene::touchButtonEvent, this));
    
    auto listener1 = EventListenerTouchOneByOne::create(); //创建一个触摸监听
    listener1->setSwallowTouches(false); //设置是否想下传递触摸
    listener1->onTouchBegan = CC_CALLBACK_2(FightScene::onTouchBegan, this);
    listener1->onTouchEnded = CC_CALLBACK_2(FightScene::onTouchEnded, this);
    _eventDispatcher->addEventListenerWithSceneGraphPriority(listener1, this->effectNode);
    return true;
}

void FightScene::onEnter()
{
    BaseUI::onEnter();
    this->schedule(SEL_SCHEDULE(&FightScene::tick), 1);
}

void FightScene::resetProgress()
{
    
}

void FightScene::bounce(MFighter *mf)
{
    
}

void FightScene::pickBean(Node* pSender)
{
    pSender->removeFromParent();
    
    int mp=++FightMgr::getInstance()->roleMp;
    int beanNum=mp/BEAN_NUM;
    for(int i=0;i<this->beans.size();i++){
        Widget* bean=beans.at(i);
        if(bean->isVisible() || beanNum<i) continue;
        Clip* clip=Clip::create("effect/bean_DDSZ.plist", 12);
        this->skillNode->addChild(clip);
        clip->setPosition(bean->getPosition());
        float duration=clip->play();
        bean->runAction(Sequence::create(DelayTime::create(duration),Show::create(), NULL));
    }
   
    for(int i=0;i<skillIcons.size();i++){
        int skillID=skillIcons.at(i)->getTag();
        if(XSkill::record(Value(skillID))->getMp()<=beanNum){
            this->skillRims.at(i)->setVisible(true);
            this->skillRims.at(i)->play(true);
        }
    }
}

void FightScene::tick(float dt)
{
    this->duration--;
    std::string str=Value(duration/60).asString()+":"+Value(duration%60).asString();
    Text* cdLabel=static_cast<Text*>(this->top->getChildByName("cdLabel"));
    cdLabel->setString(str);
}

void FightScene::skillColdDown(int skillID)
{
    skillIcons.at(0)->setVisible(true);
    this->skillRims.at(0)->setVisible(true);
    this->skillRims.at(0)->play(true);
}

void FightScene::setNpcIcon(int num,bool isKill)
{
    for(int i=0;i<num;i++){
        Widget* icon=this->top->getChildByName("npc"+Value(i).asString());
        if(isKill){
            icon->setVisible(i>num);
        }else{
            icon->setVisible(i<=num);
        }
    }
}

bool FightScene::onTouchBegan(Touch *touch, Event *unusedEvent)
{
    Layout::onTouchBegan(touch, unusedEvent);
    return true;
}

void FightScene::onTouchEnded(Touch *touch, Event *unusedEvent)
{
    Vec2 start=touch->getStartLocation();
    Vec2 end=touch->getLocation();
    
    this->touchSkill(touch,unusedEvent);
    
    //滑动捡豆子
    if(fabs(end.x-start.x) == 0) return;
    Vector<Node*> childs=this->effectNode->getChildren();
    for(auto bean : childs){
        bean->stopAllActions();
        auto cf=CallFuncN::create( CC_CALLBACK_1(FightScene::pickBean, this));
        Spawn* sp=Spawn::create(MoveTo::create(0.3, Vec2(100, 100)),Sequence::create(DelayTime::create(0.2),ScaleTo::create(0.1,0),NULL),NULL);
        bean->runAction(Sequence::create(sp,cf, NULL));
    }
}

void FightScene::touchSkill(Touch* touch,Event* unusedEvent)
{
    Vec2 end=touch->getLocation();
    end=this->skillNode->convertToNodeSpace(end);
    if(FightMgr::getInstance()->isOver){
        return;
    }
    // 使用技能
    for(int i=0;i<skillIcons.size();i++){
        if(skillRims.at(i)->isVisible()==false){
            continue;
        }
        Widget* skillIcon=skillIcons.at(i);
        if(skillIcon->getBoundingBox().containsPoint(end)){
            this->skillRims.at(i)->stopAllActions();
            this->skillRims.at(i)->setVisible(false);
            FightMgr::getInstance()->skillAttack(-skillIcon->getTag());
            
            break;
        }
    }
    int beanNum=FightMgr::getInstance()->roleMp;
    for (int i=4; i>-1; i--) {
        if(beanNum/BEAN_NUM<i-1){
            this->beans.at(i)->setVisible(false);
        }
    }
}

void FightScene::touchButtonEvent(cocos2d::Ref *pSender, Widget::TouchEventType type)
{
    if(type!=TouchEventType::ENDED) return;
    
    Button* sender=dynamic_cast<Button*>(pSender);
    log("tag:%d",sender->getTag());
    if(sender->getTag()==10125){ //pause
//        FightMgr::getInstance()->clear();
//        Director::getInstance()->replaceScene(HomeScene::createScene());
        this->unschedule(SEL_SCHEDULE(&FightScene::tick));
    }
    if(sender->getTag()==10145){ //auto
                
    }
}

void FightScene::initNetEvent()
{
    auto listener = EventListenerCustom::create(NET_MESSAGE, [=](EventCustom* event){
        NetMsg* msg = static_cast<NetMsg*>(event->getUserData());
        switch (msg->msgId)
        {
            case C_FIGHTRESULT:
            {
                PResultResp pResultResp;
                pResultResp.ParseFromArray(msg->bytes, msg->len);
                GateResult* gateResult=GateResult::create(this, pResultResp);
                gateResult->show();
                break;
            }
                
            default:
                break;
        }
    });
    Director::getInstance()->getEventDispatcher()->addEventListenerWithSceneGraphPriority(listener, this);
}

void FightScene::onExit()
{
    FightMgr::getInstance()->clear();
    Node::onExit();
    SpriteFrameCache::getInstance()->removeSpriteFrames();
}