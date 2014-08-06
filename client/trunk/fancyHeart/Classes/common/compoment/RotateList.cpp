//
//  RotatList.cpp
//  fancyHeart
//
//  Created by 秦亮亮 on 14-6-30.
//
//

#include "RotateList.h"

RotateList::~RotateList()
{
    this->items.clear();
}

RotateList* RotateList::create()
{
    RotateList* widget = new RotateList();
    if (widget && widget->init())
    {
        widget->autorelease();
        return widget;
    }
    CC_SAFE_DELETE(widget);
    return nullptr;
}

void RotateList::onEnter()
{
    Layout::onEnter();
    scheduleUpdate();
}


//void RotateList::initRenderer()
//{
//    Layout::initRenderer();
//}

bool RotateList::init()
{
    if (Layout::init())
    {
        tagNumAtMiddle=0;
        addToNum = 0;
        picNum = 0;
        return true;
    }
    return false;
}

//移动坐标
bool RotateList::scrollChildren(float touchOffsetX, float touchOffsetY)
{
    bool scrollenabled = true;
    float realOffset = touchOffsetX;
    
    Size widgetSize = Director::getInstance()->getWinSize();//屏幕大小
    auto widgetEnd = this->getChildByTag(itemNum-1);
    auto widgetFirst = this->getChildByTag(0);
    isBack = true;
    //向左移动最多确保最后一个模版在中间位置
    if (widgetEnd->getPositionX()+touchOffsetX <= widgetSize.width/2) {
        isBack = false;
        realOffset = widgetSize.width/2 - widgetEnd->getPositionX();
    }
    //向右移动最多确保第一个要在中间位置
    if (widgetFirst->getPositionX()+touchOffsetX >= widgetSize.width/2) {
        isBack = false;
        realOffset = widgetSize.width/2 -widgetFirst->getPositionX();
    }
    changePos(Vec2(realOffset,0));

    //改变坐标以及scale
    change();
    
    return scrollenabled;
}

//移动各个模版坐标
void RotateList::changePos(const Vec2 &realOffset)
{
    for (int i = 0; i<itemNum; ++i) {
        auto widget = this->getChildByTag(i);
        widget->setPosition(widget->getPosition()+realOffset);
    }
}
//释放,并且设置层级关系
void RotateList::handleReleaseLogic(const Vec2 &touchPoint)
{
    //把世界坐标转换到当前节点的本地坐标系
    this->touchEndedPoint = convertToNodeSpace(touchPoint);
    float totalDis = 0.0f;
    totalDis = this->touchEndedPoint.x - this->touchBeganPoint.x;
    goDistance = totalDis;//记录移动的距离，根据距离看是否要更换图片
    
    if (isBack) {
        //回弹
        goBack();
    }
    //查看现在哪个处于中间位置
    Size widgetSize = Director::getInstance()->getWinSize();//屏幕大小
    auto dis = widgetSize.width/haveItemNum;
    for (int i = 0; i<itemNum; ++i) {
        auto widget = this->getChildByTag(i);
        int x= widget->getPositionX();
        //改变层级setLocalZOrder 当x坐标在中间图片范围内
        if (x>= (widgetSize.width/2-dis/4) && x<=(widgetSize.width/2+dis/4)) {
            tagNumAtMiddle = i;
        }
    }
    //再重新设定坐标（确定没有误差）
    auto itemPos = widgetSize.width/haveItemNum;//每个模版间距离
    float x;
    for (int i=0; i<itemNum; ++i) {
        auto widget = this->getChildByTag(i);
        if (tagNumAtMiddle>i) {
            x=widgetSize.width/2 -itemPos*(tagNumAtMiddle-i);
        }else if (tagNumAtMiddle==i){
            x =widgetSize.width/2;
        }else if (tagNumAtMiddle<i){
            x =widgetSize.width/2 +itemPos*(i-tagNumAtMiddle);
        }
        widget->setPosition(x, getYPos(x));
        setMoveType(widget,x);
    }
    //更改层级
    for (int i = 0; i<itemNum; ++i) {
        auto widget = this->getChildByTag(i);
        int x= widget->getPositionX();
        //改变层级setLocalZOrder 当x坐标在中间图片范围内
        if (x>= (widgetSize.width/2-dis/4) && x<=(widgetSize.width/2+dis/4)) {
            widget->setLocalZOrder(itemNum);
        }else if (x > widgetSize.width/2+dis/4) {
            widget->setLocalZOrder(itemNum-i-1);
        }else if(x< (widgetSize.width/2-dis/4)){
            widget->setLocalZOrder(i);
        }
    }
    scrollChildren(0,0);//确保移动最左最右边没有多移动的情况
    changeMiddleEvent();
}
//回调函数
void RotateList::changeMiddleEvent()
{
    if (this->eventCallback) {
        this->eventCallback(this,EventType::SCROLL_MIDDLE);
    }
}

int RotateList::getMiddleIndex()
{
    return tagNumAtMiddle;
}

//回弹
void RotateList::goBack()
{
    Size widgetSize=Director::getInstance()->getWinSize();
    auto dis = widgetSize.width/haveItemNum;
    //多走需要回弹的位移:触摸移动的横向位移－需要移动几个模版＊每个模版需要走的位移（此处只要移动模版与模版距离的3/4就可移动）
    auto para = 1;
    if (goDistance>=0) {
        para = 1;
    }else{
        para = -1;
    }
    float goDis = goDistance - floor((abs(goDistance)+dis/4)/dis)*dis*para;
    changePos(- Vec2(goDis,0));
    change();
}

bool RotateList::onTouchBegan(Touch *touch, Event *unusedEvent)
{
    bool pass = Layout::onTouchBegan(touch, unusedEvent);
    this->touchBeganPoint = convertToNodeSpace(_touchStartPos);
    //起始移动点
    this->touchMovingPoint = this->touchBeganPoint;
    this->slidTime = 0.0f;
    this->bePressed = true;
    return pass;
}

void RotateList::onTouchMoved(Touch *touch, Event *unusedEvent)
{
    if(this->items.size() == 0){
        return;
    }
    Layout::onTouchMoved(touch, unusedEvent);
    this->touchMovedPoint = convertToNodeSpace(_touchMovePos);
    Vec2 delta = this->touchMovedPoint - this->touchMovingPoint;
    //随着移动，起始移动点坐标变化
    this->touchMovingPoint = this->touchMovedPoint;
    
    scrollChildren(delta.x, 0.0f);
}

void RotateList::onTouchEnded(Touch *touch, Event *unusedEvent)
{
    if(this->items.size() == 0){
        return;
    }
    Layout::onTouchEnded(touch, unusedEvent);
    handleReleaseLogic(_touchEndPos);
}

void RotateList::onTouchCancelled(Touch *touch, Event *unusedEvent)
{
    if(this->items.size() == 0){
        return;
    }
    Layout::onTouchCancelled(touch, unusedEvent);
    handleReleaseLogic(touch->getLocation());
}

void RotateList::update(float dt)
{
    //手触发屏幕后时间会进行加操作
    if (this->bePressed){
        this->slidTime += dt;
    }
}

void RotateList::checkChildInfo(int handleState,Widget* sender,const Vec2 &touchPoint)
{
    switch (handleState)
    {
        case 0:
            this->touchBeganPoint = convertToNodeSpace(touchPoint);
            //起始移动点
            this->touchMovingPoint = this->touchBeganPoint;
            this->slidTime = 0.0f;
            this->bePressed = true;
//            setCurSelectedIndex(sender);
//            if (_eventCallback) {//
//                _eventCallback(this,EventType::ON_SELECTED_ITEM_START);
//            }
            break;
        case 1:
        {
            float offset = (sender->getTouchStartPos() - touchPoint).getLength();
            if (offset > this->childFocusCancelOffset){
                sender->setHighlighted(false);
                this->touchMovedPoint = convertToNodeSpace(touchPoint);
                Vec2 delta = this->touchMovedPoint - this->touchMovingPoint;
                //随着移动，起始移动点坐标变化
                this->touchMovingPoint = this->touchMovedPoint;
                scrollChildren(delta.x, 0.0f);
                
                //模版位置的变化，随着移动
                if (this->items.size() != 0) {
                    Size widgetSize=Director::getInstance()->getWinSize();
                    auto dis = widgetSize.width/haveItemNum;
                    int overIndex = float(itemNum/2);
                    auto widget = this->items.at(0);//this->getChildByTag(overIndex);
                    auto widget1 = this->items.at(itemNum-1);
                    int x =widget->getPositionX();
                    int x1 =widget1->getPositionX();
                    if (x<widgetSize.width/2-dis*ceilf(itemNum/2)) {//左侧去掉，右侧添加
                        if (picNum > showPicNum+addToNum) {
                            removeItem(x);
                        }
                    }else if (x1>widgetSize.width/2+dis*ceilf(itemNum/2)){//右侧去掉一个模版，左侧添加一个模版
                        if (addToNum !=0) {
                            insertCustomItem(x1);
                        }
                    }
                }
            }
        }
            break;
        case 2:
            handleReleaseLogic(touchPoint);
//            setCurSelectedIndex(sender);
//            if (_eventCallback) {//
//                _eventCallback(this, EventType::ON_SELECTED_ITEM_END);
//            }
            break;
    }
}

int RotateList::getAddToNum()
{
    return addToNum;
}

void RotateList::setCurSelectedIndex(Widget* sender)
{
    //处理点击事件
    Widget* parent = sender;
    if (parent)
    {
        this->curSelectedIndex = this->items.getIndex(parent);
        parent = dynamic_cast<Widget*>(parent->getParent());
    }
}

ssize_t RotateList::getCurSelectedIndex() const
{
    return this->curSelectedIndex + addToNum;
}

//移动过程中调用的方法，用来改变y轴坐标，并且调用setMoveType方法用来改变缩放和角度值
void RotateList::change()
{
    Size widgetSize=Director::getInstance()->getWinSize();
    
    for (int i = 0; i<itemNum; ++i) {
        auto widget = this->getChildByTag(i);
        //设置Y位置
        int x =widget->getPositionX();
        //将长度widgetSize.width+widgetSize.width/2设置为半个圆弧的长度，通过x坐标计算y值
        widget->setPosition(Vec2(x,getYPos(x)));
        setMoveType(widget,x);
    }
}

//移动的过程中处理缩放以及角度变化
void RotateList::setMoveType(Node* widget,int x)
{
    Size widgetSize=Director::getInstance()->getWinSize();
    //缩放：越往中心点缩放系数越大
    float wholePosX = widget->getPositionX();
    float scaleNum;
    if ( wholePosX< widgetSize.width/2) {//如果图片在屏幕中心点左边
        //模版的坐标／整个屏幕宽度的一半） ＋ 2/5
        scaleNum = pow(wholePosX/(widgetSize.width/2)+2/5,0.5);
    }else if (wholePosX > widgetSize.width/2){//如果图片在屏幕中心点右边
        //（整个屏幕的宽度 － 模版的坐标）／整个屏幕宽度的一半）） ＋ 2/5
        scaleNum = pow((widgetSize.width - wholePosX)/(widgetSize.width/2)+2/5,0.5);
    }else{
        scaleNum = 1;
    }
    widget->setScale(fmax(scaleNum,0.5));
    //旋转角度
    int rotation = (180/widgetSize.width/2)*(x - widgetSize.width/2);
    widget->setRotation(rotation);
}

//设置样式
void RotateList::setItemModel(Widget* model)//此处应该再传一个data数据
{
    if (!model)
    {
        return;
    }
    this->model = model;
    this->model->setTouchEnabled(true);
    this->model->addTouchEventListener(CC_CALLBACK_2(RotateList::touchEvent,this));
    this->model->retain();
    haveItemNum= 7;//整个显示界面当前放几个模版＋1(整个界面被分成几个平均等份)
    showPicNum =haveItemNum+4;
}

//每添加一个模版，删除之前添加到舞台上的对象，之后加入新加载后的所有对象之后重新放在舞台上
void RotateList::pushBackItem()
{
    itemNum = this->items.size();
    //加入新加载后的所有对象之后重新放在舞台上
    Size widgetSize = Director::getInstance()->getWinSize();//屏幕大小
    for (int i = 0; i<itemNum; ++i) {
        Widget* newItem1 = this->items.at(i);
        int x =(widgetSize.width/haveItemNum)*i + widgetSize.width/2;
        newItem1->setTag(i);
        //初始设置位置
        newItem1->setPosition(Vec2(x,getYPos(x)));
        setMoveType(newItem1,x);
        //初始设置层级setLocalZOrder
        if (x >= widgetSize.width/2) {
            newItem1->setLocalZOrder(itemNum-i);
        }else{
            newItem1->setLocalZOrder(i);
        }
    }
}

//根据x坐标求得y坐标
float RotateList::getYPos(float x)
{
    Size widgetSize = Director::getInstance()->getWinSize();//屏幕大小
    auto y =widgetSize.height*sin((x+widgetSize.width/4)*(3.1415926/(widgetSize.width+widgetSize.width/2)))*1/3;
    return y;
}
//设置滚动到第几个在中间位置，index从0起始
void RotateList::setRoll(int index)
{
    if (this->items.size() == 0) {
        return;
    }
    tagNumAtMiddle = index;
    auto widgetSize = Director::getInstance()->getWinSize();
    auto itemPos = widgetSize.width/haveItemNum;//每个模版间距离
    float x;
    for (int i=0; i<itemNum; ++i) {
        auto widget = this->getChildByTag(i);
        if (index>i) {
            x=widgetSize.width/2 -itemPos*(index-i);
        }else if (index==i){
            x =widgetSize.width/2;
        }else if (index<i){
            x =widgetSize.width/2 +itemPos*(i-index);
        }
        widget->setPosition(x, getYPos(x));
        //层级setLocalZOrder
        auto dis = widgetSize.width/haveItemNum;
        if (x>= (widgetSize.width/2-dis/4) && x<=(widgetSize.width/2+dis/4)) {
            widget->setLocalZOrder(itemNum);
        }else if (x > widgetSize.width/2+dis/4) {
            widget->setLocalZOrder(itemNum-i-1);
        }else if(x< (widgetSize.width/2-dis/4)){
            widget->setLocalZOrder(i);
        }
        setMoveType(widget,widget->getPositionX());
    }
    changeMiddleEvent();
}

//删除一张模版,将第一个放后面
void RotateList::removeItem(ssize_t index)
{
    Widget* item = this->items.at(0);
    if (!item)
    {
        return;
    }
    this->items.eraseObject(item);
    this->items.pushBack(item);
    
    Size widgetSize = Director::getInstance()->getWinSize();//屏幕大小
    auto itemPos = widgetSize.width/haveItemNum;//每个模版间距离
    auto widget = this->getChildByTag(int(this->items.size()) -2);
    //设置Y位置
    int x = this->items.at(itemNum-2)->getPositionX()+itemPos;//widget->getPositionX() + itemPos;
    item->setPosition(Vec2(x,getYPos(x)));
    for (int i = 0; i<this->items.size(); ++i) {
        Widget* newItem1 = this->items.at(i);
        newItem1->setTag(i);
    }
    addToNum ++;
}

//将最后一个放前面(在后面或者前面加一张模版)
void RotateList::insertCustomItem(ssize_t index)
{
    Widget* item = this->items.at(itemNum-1);
    if (!item)
    {
        return;
    }
    this->items.eraseObject(item);
    this->items.insert(0, item);
    
    Size widgetSize = Director::getInstance()->getWinSize();//屏幕大小
    auto itemPos = widgetSize.width/haveItemNum;//每个模版间距离
    auto widget = this->getChildByTag(1);
    //设置Y位置
    int x =this->items.at(1)->getPositionX() - itemPos;//widget->getPositionX() - itemPos;
    item->setPosition(Vec2(x,getYPos(x)));
    
    for (int i = 0; i<this->items.size(); ++i) {
        Widget* newItem1 = this->items.at(i);
        newItem1->setTag(i);
    }
    addToNum --;
}

Vector<Widget*>& RotateList::getItems()
{
    return this->items;
}

void RotateList::pushBackDefaultItem()
{
    if (!this->model)
    {
        return;
    }
    ++picNum;
    if (this->items.size()<showPicNum) {
        Widget* newItem = this->model->clone();
        this->items.pushBack(newItem);
        addChild(newItem);
        newItem->addTouchEventListener(CC_CALLBACK_2(RotateList::touchEvent,this));
    }

    pushBackItem();
}

void RotateList::touchEvent(cocos2d::Ref *pSender, TouchEventType type)
{
    switch (type)
    {
        case TouchEventType::ENDED:
            if (this->eventCallback) {
                Widget*sprite=static_cast<Widget*>(pSender);
                setCurSelectedIndex(sprite);
                this->eventCallback(this,EventType::TOUCH_ITEM);
            }
            break;
    }
}

//移除所有对象
void RotateList::removeAllItems()
{
    removeAllChildren();
    this->items.clear();
}

void RotateList::addEventListener(const rotateListCallback& callback)
{
    this->eventCallback = callback;
}

