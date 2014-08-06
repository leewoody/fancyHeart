//
//  Fview.h
//  fancyHeart
//
//  Created by 秦亮亮 on 14-5-6.
//
//

#ifndef __fancyHeart__Fview__
#define __fancyHeart__Fview__

#include <iostream>
#include "cocos2d.h"
#include "Manager.h"
#include "ui/CocosGUI.h"
#include "cocostudio/CocoStudio.h"
#include "fight.pb.h"
#include "BezierMove.h"

USING_NS_CC;
using namespace ui;
using namespace cocostudio;

class MFighter;
#define ani_idle "idle"
#define ani_walk "walk"
#define ani_attacked "attacked"
#define ani_die "die"
#define ani_spell "引导"
#define ani_attack "attack"
#define ani_skillAttack1 "skillAttack1"
#define ani_skillAttack2 "skillAttack2"
#define ani_win "win"

#define speed 190

class Fview:public Node
{
public:

    static Fview* create(std::string fPath,std::string rPath,int pos);
    bool init(std::string fPath,std::string rPath,int pos);
    virtual void onEnter();
    virtual void onExit();
//    virtual void update(float dt);
    void stand();
    void run();
    void attack(int type); //1普通 2射击
    void skillAttak();
    void spell();
    void attacked(PHit& pHit);
    float lineThrow(MFighter* target); //直线
    float arcThrow(MFighter* target);  //弧线
    float impaleThrow(MFighter* target); //穿刺
    void fallHp(Value num);
    void fallMp();
    void hitWord();
    void die();
    void dieClear();
    void revive();
    void playBuffer();
    void win();
    
    
    int pos;
    MFighter* delegate;
    
    EventDispatcher* cEventDispatcher;
private:
    int tickCount=0;
    LoadingBar* hpBar;
    Armature* armature;
    void tick(float dt);
    void onFrameEvent(Bone *bone, const std::string& frameEventName, int originFrameIndex, int currentFrameIndex);
    void animationEvent(Armature *armature, MovementEventType movementType, const std::string& movementID);
};
#endif /* defined(__fancyHeart__Fview__) */
