//
//  Skill.cpp
//  fancyHeart
//
//  Created by 秦亮亮 on 14-6-4.
//
//

#include "Skill.h"
Skill* Skill::create(MFighter* attacker,int skillID)
{
    Skill* pRet=new Skill();
    if(pRet && pRet->init(attacker,skillID))
    {
        pRet->autorelease();
        return pRet;
    }else{
        delete pRet;
        pRet = NULL;
        return NULL;
    }
}

bool Skill::init(MFighter* attacker,int skillID)
{
    this->attacker=attacker;
    this->skillID=skillID;
    this->isReady=false;
    //被动技能走CD
    XSkill* xskill=XSkill::record(Value(skillID));
//    if(xskill->getType()==1){
        Director::getInstance()->getScheduler()->schedule(SEL_SCHEDULE(&Skill::coldDown),this,XSkill::record(Value(skillID))->getTriggerParam()/10000.0,false);
//    }
    return true;
}

void Skill::coldDown(float dt)
{
    this->setIsReady(true);
}

void Skill::setIsReady(bool isReady)
{
    this->isReady=isReady;
}

bool Skill::getIsReady()
{
    return this->isReady;
}

void Skill::start()
{
    this->setIsReady(false);
    //施法时间 1舜发 2吟唱（引导） 3蓄力
    XSkill* xSkill=XSkill::record(Value(skillID));
    //蓄力
    if(xSkill->getBuildTime()>0)
    {
        //进入蓄力阶段
        //定时触发攻击（中间操作可能直接攻击）
        Director::getInstance()->getScheduler()->schedule(SEL_SCHEDULE(&Skill::cast), this, 0,0,3, false);
        this->attacker->buildup();
        return;
    }
    //引导
    if(xSkill->getLeadNum()>0)
    {
        //引导
        //定时触发正式攻击
        Director::getInstance()->getScheduler()->schedule(SEL_SCHEDULE(&Skill::cast), this, xSkill->getLeadGap()/10000.0,0,0, false);
        this->attacker->spell();
        return;
    }
    //射击
    if(xSkill->getRangeType()==4 && xSkill->getRangeParam1()>0){
        this->attacker->attack(2);
        this->targets=this->selectTarget();
        this->attacker->targets=this->targets;
        return;
    }
    //舜发
    if(xSkill->getLeadNum()==0)
    {
//        this->cast();
        this->attacker->attack(1);
        this->attacker->targets=this->selectTarget();
        return;
    }
}

void Skill::cast()
{
//    XSkill* xSkill=XSkill::record(Value(skillID));
//    if(this->attacker->state==fstate::spell || attacker->state==fstate::buildup || attacker->state==fstate::throwing)
//    {
//        Director::getInstance()->getScheduler()->unschedule(SEL_SCHEDULE(&Skill::cast),this);
//    }
//    
//    this->attacker->attack(1);
//    
//    this->targets=this->selectTarget();
//    
//    int rangeType=xSkill->getRangeType();
//    
//    if (rangeType == 5) //弹射
//    {
//        Director::getInstance()->getScheduler()->schedule(SEL_SCHEDULE(&Skill::bounce), this,0.2,targets.size(), 0.2, false);
//
//    }else{
//        for(MFighter* mf : targets)
//        {
//            int arrowNum=xSkill->getRangeParam2();
//            //带弹道的
//            if(arrowNum>0)
//            {
//                this->shoot(mf);
//                continue;
//            }
////            this->hit(mf);
//        }
//    }
}

void Skill::shoot(MFighter* mf)
{
//    this->attacker->state=fstate::throwing;
//    Director::getInstance()->getScheduler()->schedule(SEL_SCHEDULE(&Skill::shootEnd), this, 1,0,0.4, false);
////    int arrowType=XSkill::record(Value(skillID))->getArrowType();
//    int arrowType=0;
//    this->attacker->shoot(mf,arrowType);
}

void Skill::shootEnd()
{
//    for(MFighter* mf : targets)
//    {
//        this->hit(mf);
//    }
}

void Skill::bounce(float dt)
{
    /*
    if(this->targets.size()==0)
    {
        Director::getInstance()->getScheduler()->unschedule(SEL_SCHEDULE(&Skill::bounce), this);
    }
    MFighter* mf=this->targets.at(0);
    FightMgr::getInstance()->view->bounce(mf);
//    this->hit(mf);

    this->targets.erase(0);
    */
}


//重置受击对象,攻击范围－－》策略
std::vector<int> Skill::selectTarget()
{
    std::vector<int> arr;
    XSkill* xSkill=XSkill::record(Value(skillID));
    
    switch (xSkill->getRangeType()) {
        case 0: //自身
            arr.push_back(attacker->pos);
            break;
        case 1: //我方 影响人数 all
            arr=FightMgr::getInstance()->getFoes(attacker->pos,true);
            arr=this->selectStrategy(arr,xSkill->getRangeParam1());
            break;
        case 2: //近战 影响人数 all
            arr=FightMgr::getInstance()->getFoes(attacker->pos);
            arr=this->selectStrategy(arr,xSkill->getRangeParam1());
            this->selectStrategy(arr,xSkill->getRangeParam2());
            break;
        case 3:{ //爆发 前排/中排/后排
            std::vector<int> vec=FightMgr::getInstance()->getFoes(attacker->pos);
            int row1=0,row2=0,row3=0;
            
            for(int mPos : vec){
                MFighter* mf=FightMgr::getInstance()->getHero(mPos);
                if(attacker->getGrid() - mf->getGrid() <= 2){
                    if(row1 <= xSkill->getRangeParam1()){
                        arr.push_back(mPos);
                        row1++;
                    }
                }
                if(attacker->getGrid() - mf->getGrid() <= 2){
                    if(row2 <= xSkill->getRangeParam2()){
                        arr.push_back(mPos);
                        row2++;
                    }
                }
                if(attacker->getGrid() - mf->getGrid() <= 2){
                    if(row3 <= xSkill->getRangeParam3()){
                        arr.push_back(mPos);
                        row3++;
                    }
                }
            }
            break;
        }
        case 4: //最大距离/子弹数量 all
            arr=FightMgr::getInstance()->getFoes(attacker->pos);
            for(int i=0;i<arr.size();i++)
            {
                MFighter* mf=FightMgr::getInstance()->getHero(arr[i]);

                if(abs(attacker->getGrid()-mf->getGrid()) > xSkill->getRangeParam1())
                {
                    arr.erase(arr.begin()+i);
                }
            }
            arr=this->selectStrategy(arr,xSkill->getRangeParam2());
            break;
        case 5: //弹射 弹射次数
        {
            std::vector<int> vec=FightMgr::getInstance()->getFoes(attacker->pos);
            int bondNum=xSkill->getRangeParam1();
            for(int i=0;i<bondNum;i++)
            {
                arr.push_back(vec.at(i%vec.size()));
            }
        }
            break;
        case 6: //穿透 最大距离
        {
            std::vector<int> vec=FightMgr::getInstance()->getFoes(attacker->pos);
            for(int mPos : vec)
            {
                MFighter* mf=FightMgr::getInstance()->getHero(mPos);
                if(attacker->getGrid()-mf->getGrid() <= xSkill->getRangeParam1()){
                    arr.push_back(mPos);
                }
            }
        }
            break;
        default:
            log("error range type:%d,skillID:%d",xSkill->getRangeType(),skillID);
            break;
    }
    return arr;
}


//选择策略
std::vector<int> Skill::selectStrategy(std::vector<int> arr,int num)
{
//    Vector<MFighter*>targets;
    std::vector<int> targets;
    num=MIN(num, arr.size());

    int type=1;
    switch (type) {
        case 0: //普通-  只能选中一次
        {
            std::vector<int> vec=Utils::randSeveral(num,false);
            for(int i=0;i<num;i++)
            {
                targets.push_back(arr.at(vec[i]));
            }
            break;
        }
        case 1: //随机- 可以选中多次
        {
            std::vector<int> vec=Utils::randSeveral(num,true);
            for(int i=0;i<num;i++)
            {
                targets.push_back(arr.at(vec[i]));
            }
            break;
        }
        case 2://血最少-
        {
            sort(arr.begin(),arr.end(),this->sortLessHp);
            for(int i=0;i<num;i++){
                targets.push_back(arr.at(i%arr.size()));
            }
            break;
        }
        case 3: //血最多
        {
            sort(arr.begin(),arr.end(),this->sortMoreHp);
            for(int i=0;i<num;i++){
                targets.push_back(arr.at(i%arr.size()));
            }
            break;
        }
        case 4: //离得近-
        {
            sort(arr.begin(),arr.end(),this->sortNear);
            for(int i=0;i<num;i++){
                targets.push_back(arr.at(i%arr.size()));
            }
            break;
        }
        case 5: //离得远
        {
            sort(arr.begin(),arr.end(),this->sortFar);
            for(int i=0;i<num;i++){
                targets.push_back(arr.at(i%arr.size()));
            }
            break;
        }
        default:
            log("错误的选择策略");
            break;
    }
    return targets;
}

int Skill::getType()
{
    return XSkill::record(Value(skillID))->getType();
}

int Skill::getMp()
{
    return XSkill::record(Value(skillID))->getMp();
}


bool Skill::sortFar(int pos1,int pos2)
{
    MFighter* f1=FightMgr::getInstance()->getHero(pos1);
    MFighter* f2=FightMgr::getInstance()->getHero(pos2);
    return f1->getGrid()-f2->getGrid() > 0;
}

bool Skill::sortNear(int pos1,int pos2)
{
    MFighter* f1=FightMgr::getInstance()->getHero(pos1);
    MFighter* f2=FightMgr::getInstance()->getHero(pos2);
    return f1->getGrid()-f2->getGrid() < 0;
}

bool Skill::sortLessHp(int pos1,int pos2)
{
    MFighter* f1=FightMgr::getInstance()->getHero(pos1);
    MFighter* f2=FightMgr::getInstance()->getHero(pos2);
    return f1->data->hp<f2->data->hp;
}

bool Skill::sortMoreHp(int pos1,int pos2)
{
    MFighter* f1=FightMgr::getInstance()->getHero(pos1);
    MFighter* f2=FightMgr::getInstance()->getHero(pos2);
    return f1->data->hp>f2->data->hp;
}

void Skill::stop()
{
    Director::getInstance()->getScheduler()->pauseTarget(this);
}

void Skill::resume()
{
    Director::getInstance()->getScheduler()->resumeTarget(this);
}

Skill::~Skill()
{
    Director::getInstance()->getScheduler()->unschedule(SEL_SCHEDULE(&Skill::coldDown),this);
    Director::getInstance()->getScheduler()->unschedule(SEL_SCHEDULE(&Skill::bounce),this);
    Director::getInstance()->getScheduler()->unschedule(SEL_SCHEDULE(&Skill::cast),this);
}