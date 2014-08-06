//
//  HomeScene.cpp
//  fancyHeart
//
//  Created by 秦亮亮 on 14-5-12.
//
//

#include "HomeScene.h"
Scene* HomeScene::createScene(){
	auto scene = Scene::create();
    auto layer = HomeScene::create();
    scene->addChild(layer);
	return scene;
}

HomeScene* HomeScene::create()
{
    HomeScene* homeScene=new HomeScene();
    if (homeScene && homeScene->init("publish/home/HomeScene.json",true))
    {
        homeScene->autorelease();
        return homeScene;
    }
    CC_SAFE_DELETE(homeScene);
    return nullptr;
}

bool HomeScene::init(std::string fileName,bool isScence)
{
	if(!BaseUI::init(fileName,isScence))
    {
		return false;
	}
    Size dSize(1136,640);
    Size size=Director::getInstance()->getOpenGLView()->getDesignResolutionSize();
    float scale=fmin(size.width/dSize.width,size.height/dSize.height);

    std::vector<std::string> buildNames={"img_hecheng","img_tiaozhan","img_hero","img_shichang","img_zhaohuan","img_fuben"};
    ComRender *render = static_cast<ComRender*>(layout->getChildByTag(10003)->getComponent("GUIComponent"));
    comLayout=static_cast<Layout*>(render->getNode());

   
    auto widget=static_cast<Widget*>(comLayout->getChildByName("home_botom"));
    widget->setPosition(Vec2(size.width/2, 0+widget->getContentSize().height*scale/2));
    widget->setScale(scale);
    
    widget=static_cast<Widget*>(comLayout->getChildByName("home_top"));
    widget->setPosition(Vec2(size.width/2, (size.height-widget->getContentSize().height*scale/2)));
    widget->setScale(scale);
    
    widget=static_cast<Widget*>(comLayout->getChildByName("home_leftop"));
    widget->setPosition(Vec2(widget->getContentSize().width*scale/2, (size.height-widget->getContentSize().height*scale/2)));
    widget->setScale(scale);
    
    widget=static_cast<Widget*>(comLayout->getChildByName("home_right"));
    widget->setPosition(Vec2(size.width-widget->getContentSize().width*scale/2, (size.height-widget->getContentSize().height*scale/2)));
    widget->setScale(scale);
  
    widget=static_cast<Widget*>(comLayout->getChildByName("home_build"));
    widget->setPosition(Vec2((size.width-widget->getContentSize().width*scale)/2, (size.height-widget->getContentSize().height)/2));
    widget->setScale(scale);
    for (std::string name : buildNames)
    {
        auto image=widget->getChildByName(name);
        image->setTouchEnabled(true);
        image->addTouchEventListener(CC_CALLBACK_2(HomeScene::touchBuildEvent, this));
    }
    widget=comLayout->getChildByName("btnChat");
    widget->setPosition(Vec2(widget->getContentSize().width*scale/2, 200));
    widget->addTouchEventListener(CC_CALLBACK_2(HomeScene::touchButtonEvent, this));
    widget->setScale(scale);
    
    widget=comLayout->getChildByName("img_cloud");
    widget->setOpacity(0);
    lrTuoyuanConfig c={Vec2(size.width/2,0),size.width/2,0};
    widget->runAction(RepeatForever::create(CircleMove::actionWithDuration(50, c)));
    widget->runAction(RepeatForever::create(Sequence::create(FadeIn::create(12),FadeOut::create(12), NULL)));
    
    srand((int)time(0));

    
    this->setTouchEnabled(true);
    this->setEnabled(true);
    this->addTouchEventListener(CC_CALLBACK_2(HomeScene::touchBuildEvent, this));
    
    
    this->initUi();
    this->chat=Chat::create();
    //this->addChild(this->chat,CHAT_LAY);
    this->chat->setVisible(false);
    
//    Label* lable=Label::createWithTTF("名字到底几个字", "Marker Felt.ttf", 25);
//    layout->addChild(lable);
//    lable->setPosition(Vec2(100, 200));
	return true;
}

void HomeScene::initUi()
{
    PRole role=Manager::getInstance()->getRoleData()->role();
    auto widget=static_cast<Widget*>(comLayout->getChildByName("home_leftop"));
    static_cast<Text*>(widget->getChildByName("txt_vip"))->setString("VIP"+Value(role.viplvl()).asString());
    static_cast<Text*>(widget->getChildByName("txt_lvl"))->setString(Value(role.level()).asString());
    static_cast<Text*>(widget->getChildByName("txt_name"))->setString(role.rolename());
    
    widget=static_cast<Widget*>(comLayout->getChildByName("home_top"));
    static_cast<Text*>(widget->getChildByName("txt_currency"))->setString(Value(role.rmb()).asString());
    static_cast<Text*>(widget->getChildByName("txt_diamond"))->setString(Value(role.coin()).asString());
    static_cast<Text*>(widget->getChildByName("txt_stamina"))->setString(Value(role.stamina()).asString());
    
}
void HomeScene::onEnter()
{
    BaseUI::onEnter();
    
}

void HomeScene::touchButtonEvent(Ref* pSender,TouchEventType type)
{
    auto button=static_cast<Button*>(pSender);
    if (!button) {
        return;
    }
    switch(type)
    {
        case TouchEventType::BEGAN:
            
            break;
        case TouchEventType::MOVED:
            break;
        case TouchEventType::ENDED:
            switch (button->getTag()) {
                case 10065://聊天按钮
                {
                    //this->chat->show();
                    break;
                }
                default:
                    break;
            }
            break;
        default:
            break;
    }
}

void HomeScene::touchBuildEvent(cocos2d::Ref *pSender, TouchEventType type)
{
    auto sprite=static_cast<Sprite*>(pSender);
    switch (type)
    {
        case TouchEventType::BEGAN:
            sprite->stopAllActions();
            sprite->runAction(Sequence::create(ScaleTo::create(0.15,1.1),ScaleTo::create(0.15, 1),NULL) );
            break;
        case TouchEventType::MOVED:
            break;
        case TouchEventType::ENDED:
        {
            switch (sprite->getTag())
            {
                case 10001: //竞技场
                {
                    
                    //gate->setPosition(Vec2(0,0));
                    break;

                }
                case 10002: //市场
                {                
                    
                    break;

                }
                case 10003: //战场pve
                {
                    Gate* gate = Gate::create();
                    gate->show();
                    //this->addChild(gate);
//                    GateMap* gateMap=GateMap::create(this, "100");
//                    gateMap->show();
                    break;
                }
                case 10004: //英雄编队（卡组）
                {
                    break;
                }
                case 10005: //英雄属性
                {
                        break;
                }
                case 10006: //合成
                {
                    break;
                }
                default:
                    break;
            }
        }
            break;
        default:
            break;
    }
}

void HomeScene::initNetEvent()
{
    auto listener = EventListenerCustom::create(NET_MESSAGE, [=](EventCustom* event)
    {
        NetMsg* msg = static_cast<NetMsg*>(event->getUserData());
        log("Custom event 1 received:%d,%d",msg->msgId,msg->len);
        switch (msg->msgId)
        {
            case C_UPROLE:
            {
                this->initUi();
//                LoginResp pm;
//                pm.ParseFromArray(msg->bytes, msg->len);
            }
                break;
            default:
                break;
        }
    });
    Director::getInstance()->getEventDispatcher()->addEventListenerWithSceneGraphPriority(listener, this);
}

void HomeScene::onExit()
{
    BaseUI::onExit();
    this->removeAllChildrenWithCleanup(true);
    TextureCache::getInstance()->removeAllTextures();
}