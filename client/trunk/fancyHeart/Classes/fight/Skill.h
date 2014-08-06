//
//  Skill.h
//  fancyHeart
//
//  Created by 秦亮亮 on 14-6-4.
//
//

#ifndef __fancyHeart__Skill__
#define __fancyHeart__Skill__

#include <iostream>
#include "cocos2d.h"
#include "MFighter.h"
#include "XSkill.h"
#include "XBuff.h"
#include "XRole.h"
#include "XRoleData.h"
#include "XRoleStar.h"
#include "Buff.h"
#include "fight.pb.h"
using namespace cocos2d;

class Skill:public Ref
{
public:
    ~Skill();
    static Skill* create(MFighter* attacker,int skillID);
    bool init(MFighter* attacker,int skillID);
    void start();
    std::vector<int> selectTarget();
//    Vector<MFighter*> selectStrategy(Vector<MFighter*>arr,int num=5);
    std::vector<int> selectStrategy(std::vector<int> arr,int num=5);
    void cast(); //施法（攻击）
    void shoot(MFighter* mf); //弹道
    void shootEnd();
    void bounce(float dt); //弹射
//    void hit(MFighter* mf,bool isSub=false); //击中算伤害,是否副目标

    void setIsReady(bool isReady);
    bool getIsReady();
    int getType();
    bool isReady;
    int skillID;
    int getMp();
  
    void stop();
    void resume();
    
    static bool sortLessHp(int pos1,int pos2);
    static bool sortMoreHp(int pos1,int pos2);
    static bool sortNear(int pos1,int pos2);
    static bool sortFar(int pos1,int pos2);
private:
    MFighter* attacker;
    void coldDown(float dt);
//    Vector<MFighter*> targets;
    std::vector<int> targets;
    int bounceIdx; //弹射索引
};

#endif /* defined(__fancyHeart__Skill__) */
