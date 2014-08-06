//
//  FighterCtrl.h
//  fancyHeart
//
//  Created by 秦亮亮 on 14-5-5.
//
//

#ifndef __fancyHeart__MFighter__
#define __fancyHeart__MFighter__

#include <iostream>
#include "cocos2d.h"
#include "external/json/rapidjson.h"
#include "Manager.h"
#include "Fview.h"
#include "FightMgr.h"
#include "Utils.h"
#include "Skill.h"
#include "Buff.h"
#include "FData.h"
#include "fight.pb.h"
#include "Confirm.h"
using namespace cocos2d;
class Skill;
class Buff;
class FData;
enum fstate{
    idle, //空闲
    run,
    start, //开始攻击
    spell, //吟唱
    buildup, //蓄力
    throwing, //仍东西
    cast,  //正式攻击
//    attacked,
    die, //死亡
    over //结束战斗
};

class MFighter:public Ref{
    CC_SYNTHESIZE(bool, isRole, IsRole);
public:
    static MFighter* create(FData* fd);
    ~MFighter();
    bool init(FData* fd);
    void initSkill();
    void start();
    void pause();
    void startAttack(float dt=0);
    Skill* selectSkill();
    void spell(); //吟唱（引导）
    void attack(int type);  //攻击 1普通 2射击
    void shoot(MFighter* mf,int type); //仍东西(射击)1抛射 2直射 3穿刺
    void buildup(); //蓄力
    void bounce(); //弹射（弹的过程）
    void attacked(PHit& pHit); //受击
    void fallHp(int num);
    void die();
    void dieClear();
    
    int getGrid();
    bool hpLessSort();
    bool hpMostSort();
    bool hpNearSort();
    bool hpFarSort();
    void clearBuff(Buff* buff);
    void attackOver();
//    void setCurrentSkillByID(int skillID);
    Skill* getSkillByID(int skillID);
    Skill* currentSkill;
    void hit(MFighter* mf);
private:
    void checkAttack(float dt);
    void autoHeal(float dt);
    float castTime; //吟唱时间
    float cd; //
    int rowNum; //0,1,2 前，中，后
    
public:
    FData* data;
    int pos;
    Vector<Skill*> skills;
    Vector<Buff*> buffs;
    fstate state;
    Fview* view;
//    MFighter* foe;
    
//    Vector<MFighter*> targets;
    std::vector<int> targets;
 

};
#endif /* defined(__fancyHeart__MFighter__) */
