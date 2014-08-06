//
//  GateSelect.cpp
//  fancyHeart
//
//  Created by zhai on 14-6-18.
//
//

#include "GateSelect.h"
#include "FightMgr.h"
GateSelect* GateSelect::create(BaseUI* delegate,int gateId,int nodeId)
{
    GateSelect* gateSelect=new GateSelect();
    if (gateSelect && gateSelect->init("publish/gateSelect/gateSelect.ExportJson",gateId,nodeId)) {
        gateSelect->autorelease();
        return gateSelect;
    }
    CC_SAFE_DELETE(gateSelect);
    return nullptr;
}

bool GateSelect::init(std::string fileName,int gateId,int nodeId)
{
	if(!BaseUI::init(fileName))
	{
		return false;
	}
	//init ui
    this->gateId=gateId;
    this->nodeId=nodeId;
    Widget* widget=static_cast<Widget*>(layout->getChildByName("img_bg"));
    XGate* xg=XGate::record(Value(this->gateId));
    static_cast<Text*>(widget->getChildByName("txt_title"))->setString(xg->getName());
    static_cast<Text*>(widget->getChildByName("txt_desc"))->setString(xg->getDesc());
    Widget* paneGoup=static_cast<Widget*>(widget->getChildByName("panel_group"));
    std::vector<Button*> buttons;
    for (int i=1; i<6; i++) {
        Button* button=static_cast<Button*>(paneGoup->getChildByName("group_"+Value(i).asString()));
        button->addTouchEventListener(CC_CALLBACK_2(GateSelect::touchButtonEvent,this));
        buttons.push_back(static_cast<Button*>(button));
    }
    tabBar=TabBar::create(buttons);
    tabBar->retain();
    
    auto button=static_cast<Button*>(widget->getChildByName("btn_start"));
    button->addTouchEventListener(CC_CALLBACK_2(GateSelect::touchButtonEvent, this));
    button=static_cast<Button*>(widget->getChildByName("btn_return"));
    button->addTouchEventListener(CC_CALLBACK_2(GateSelect::touchButtonEvent, this));
    this->initGroup();
	return true;
}

void GateSelect::initGroup()
{
    Widget* widget=static_cast<Widget*>(layout->getChildByName("img_bg"));
    Widget* paneGoup=static_cast<Widget*>(widget->getChildByName("panel_group"));
    PageView* pageView=static_cast<PageView*>(paneGoup->getChildByName("pageView"));
    pageView->addEventListener(CC_CALLBACK_2(GateSelect::pageViewEvent, this));
    Widget* panelHero=static_cast<Widget*>(paneGoup->getChildByName("panelHero"));
    panelHero->setVisible(false);
    for (int i=0; i<5; i++) {
        Layout* subPage=static_cast<Layout*>(panelHero->clone());
        subPage->setVisible(true);
        pageView->addPage(subPage);
        PGroup pGroup=Manager::getInstance()->getRoleData()->groups(i);
        for(int j=0;j<5;j++)
        {
            Widget* img=static_cast<Widget*>(subPage->getChildByName("img_"+Value((j+1)).asString()));
            Widget* icon=img->getChildByName("icon");
            icon->setVisible(j<pGroup.npcid_size());

        }
    }
}

void GateSelect::pageViewEvent(Ref *pSender, PageView::EventType type)
{
    switch (type)
    {
        case PageView::EventType::TURNING:
        {
            PageView* pageView = dynamic_cast<PageView*>(pSender);
            tabBar->setIndex(int(pageView->getCurPageIndex()));
        }
            break;
            
        default:
            break;
    }
}

void GateSelect::touchButtonEvent(Ref *pSender, TouchEventType type)
{
    Button* btn=static_cast<Button*>(pSender);
    if (!btn) {
        return;
    }
    if(type==TouchEventType::ENDED)
    {
        switch (btn->getTag()) {
            case 10505://开始
            {
                PNodeReq pNodeReq;
                pNodeReq.set_gateid(this->gateId);
                pNodeReq.set_xid(this->nodeId);
                pNodeReq.set_groupid(this->tabBar->getIndex());
                Manager::getInstance()->socket->send(C_STARTFIGHT, &pNodeReq);
                break;
            }
            case 10522://返回
            {
                this->clear(true);
                break;
            }
            case 10515://返回
            case 10517:
            case 10518://返回
            case 10519:
            case 10520://返回
            {
                tabBar->setIndex(tabBar->getIndex());
                break;
            }
            default:
                break;
        }
    }
}
void GateSelect::initNetEvent(){
    auto listener = EventListenerCustom::create(NET_MESSAGE, [=](EventCustom* event){
        NetMsg* msg = static_cast<NetMsg*>(event->getUserData());
        log("Custom event 1 received:%d,%d",msg->msgId,msg->len);
        switch (msg->msgId)
        {
            case C_COMMONMSG:
            {
                PCommonResp pCommonResp;
                pCommonResp.ParseFromArray(msg->bytes, msg->len);
                if(pCommonResp.resulttype()==C_STARTFIGHT){
                    /*
                    if (pCommonResp.status()==0) {
                        PResultReq pResultReq;
                        pResultReq.set_gateid(this->gateId);
                        pResultReq.set_xid(this->nodeId);
                        pResultReq.set_star(3);
                        Manager::getInstance()->socket->send(C_FIGHTRESULT, &pResultReq);
                    }else{
                        Manager::getInstance()->showMsg("请求战斗失败");
                    }
                    */
                    //武将
                    //怪物Npc
                    //PGroup{groupID,xids:[xid]}
                    PGroup* group=Manager::getInstance()->getRoleData()->mutable_groups(this->tabBar->getIndex());
                    std::vector<long> heros;
                    for(auto hero : group->npcid()){
                        heros.push_back(hero);
                    }
                    Manager::getInstance()->gateId=this->gateId;
                    FightMgr::getInstance()->init(heros,this->nodeId,this->gateId);
                   
                }
                break;
            }
            case C_FIGHTRESULT:
            {
//                PResultResp pResultResp;
//                pResultResp.ParseFromArray(msg->bytes, msg->len);
//                GateResult* gateResult=GateResult::create(this, pResultResp);
//                gateResult->show(this);
              break;
            }
               
            default:
                break;
        }
    });
    Director::getInstance()->getEventDispatcher()->addEventListenerWithSceneGraphPriority(listener, this);
}

void GateSelect::onExit()
{
    tabBar->release();
    BaseUI::onExit();
}
