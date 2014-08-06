//
//  Manager.h
//  fancyHeart
//
//  Created by 秦亮亮 on 14-5-4.
//
//

#ifndef __fancyHeart__Manager__
#define __fancyHeart__Manager__

#include <iostream>
#include <stdio.h>
#include "google/protobuf/message.h"
#include "net/Socket.h"
#include "common/DeviceInfo.h"
#include "MsgID.h"
#include "BaseUI.h"
#include "GConfig.h"

USING_NS_CC;
using namespace google::protobuf;

#define EVENT_RUN "event_run"
#define EVENT_HERO_EVENTER "event_hero_enter"

#define LOADING_LAY 10
#define MSG_LAY 6
#define GUIDE_LAY 5
#define CHAT_LAY 1

class Manager{
    
public:
    Node* scene;
    Socket* socket;
public:
    static Manager* getInstance();
    void switchScence(Scene* scene);
    void setRoleData(NetMsg* msg);//设置玩家数据
    LoginResp* getRoleData();
    
    void updateRole(NetMsg* msg);//更新role
    void updateItems(NetMsg* msg);//更新item

    void updateGates(NetMsg* msg);//更新关卡
    void updateNodes(NetMsg* msg);//更新节点
    
    PNpc* getNpc(long npcId);
    PGateItem* getGateItem(int gateId);
    void showMsg(const string msg);//浮出提示
    int gateId=0;
 
private:
    LoginResp* roleData=nullptr;
    void updateItem(RepeatedPtrField< ::PItemChangeLog >::iterator it,RepeatedPtrField< ::PItem > *items);//更新单个物品
};

#endif /* defined(__fancyHeart__Manager__) */
