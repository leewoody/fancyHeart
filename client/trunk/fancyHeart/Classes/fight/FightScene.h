//
//  FightScene.h
//  fancyHeart
//
//  Created by 秦亮亮 on 14-5-5.
//
//

#ifndef __fancyHeart__FightScene__
#define __fancyHeart__FightScene__

#include <iostream>
#include "cocos2d.h"
#include "Manager.h"
#include "ui/CocosGUI.h"
#include "cocostudio/CocoStudio.h"
#include "Fview.h"
#include "external/json/rapidjson.h"
#include "FightMgr.h"
#include "FData.h"
#include "Clip.h"
#include "GateResult.h"
USING_NS_CC;
using namespace ui;
using namespace cocostudio;
class FData;
class FightScene:public BaseUI{
public:
	static Scene* createScene();
    static FightScene* create();
	virtual bool init(const char* fileName);
    virtual void onEnter();
    virtual void onExit();
    virtual void initNetEvent();
    virtual void touchButtonEvent(cocos2d::Ref *pSender, Widget::TouchEventType type);
    virtual bool onTouchBegan(Touch *touch, Event *unusedEvent);
    virtual void onTouchEnded(Touch *touch, Event *unusedEvent);
    void resetProgress();
    void pickBean(Node* pSender);
    void setNpcIcon(int num,bool isKill);
private:
    int duration=180;
    
public:
    Widget* heroNode;
    Widget* effectNode;
    Widget* bossInfo;
    Widget* progress;
    Widget* skillNode;
    Widget* top;
    
    Sprite* bg;
    Vector<Widget*> beans;
    Vector<Widget*> skillIcons;
    Vector<Clip*> skillRims;
    
    void bounce(MFighter* mf);
    void skillColdDown(int skillID);
    void touchSkill(Touch* touch,Event* unusedEvent);
    void tick(float dt);
};
#endif /* defined(__fancyHeart__FightScene__) */
