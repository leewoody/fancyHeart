//
//  RotatList.h
//  fancyHeart
//
//  Created by 秦亮亮 on 14-6-30.
//
//

#ifndef __fancyHeart__RotatList__
#define __fancyHeart__RotatList__

#include <iostream>
#include "cocos2d.h"
#include "ui/UILayout.h"
#include "ui/UIScrollInterface.h"
using namespace cocos2d;
using namespace cocos2d::ui;

class RotateList:public Layout//, public UIScrollInterface
{
public:
	static RotateList* create();
    virtual void onEnter() override;
    virtual ~RotateList();
    
    enum class EventType
    {
        SCROLL_MIDDLE,
        TOUCH_ITEM,
        ON_SELECTED_ITEM_START,
        ON_SELECTED_ITEM_END
    };
    typedef std::function<void(Ref*, EventType)> rotateListCallback;
    void addEventListener(const rotateListCallback& callback);
    
    
	virtual bool onTouchBegan(Touch *touch, Event *unusedEvent) override;
    virtual void onTouchMoved(Touch *touch, Event *unusedEvent) override;
    virtual void onTouchEnded(Touch *touch, Event *unusedEvent) override;
    virtual void onTouchCancelled(Touch *touch, Event *unusedEvent) override;
    
    virtual void update(float dt) override;//移动还有角度变化
    
    virtual bool init() override;
    

    void setItemModel(Widget* model);//设置样式
    void setMoveType(Node* widget,int x);
    void setRoll(int index);//设置滚动到第几个在中间位置，从0起始
    void setInitPos();
    void pushBackItem();//
    int getMiddleIndex();//得到在中间的那一个Widget
    Vector<Widget*>& getItems();//得到items
    void pushBackDefaultItem();
    void removeAllItems();//移除所有对象
    ssize_t getCurSelectedIndex() const;
    int getAddToNum();//获取最前面去除了几张图片了（即后面加了几张图片）
    
private:
    float goDistance;//触摸移动的横向位移
    void goBack();
    int itemNum;//需要制作多少模版
    void changePos(const Vec2 &realOffset);
    int haveItemNum;//整个显示界面当前放几个模版
    bool isBack;//是否回弹（当到最左边和最右边不能走到时候不能回弹）
    int tagNumAtMiddle;//在中间的模版的tag值,移动过程中此值为－1
    float getYPos(float x);//根据x左边求得y坐标
    void resetPos(int index);//重新设置坐标
    int picNum;//总共需要显示多少模版
    int showPicNum;//显示界面总共绘制几张图片
    void removeItem(ssize_t index);//去除图片
    void insertCustomItem(ssize_t index);//加图片
    int addToNum;//最前面去除了几张图片了（即后面加了几张图片）
    void changeMiddleEvent();//移动到中间位置的回调函数
    void touchEvent(cocos2d::Ref *pSender, TouchEventType type);
    ssize_t curSelectedIndex;//当前点击的item的index
    void setCurSelectedIndex(Widget* sender);//
    
    
protected:
//    virtual void initRenderer() override;
    virtual bool scrollChildren(float touchOffsetX, float touchOffsetY);//移动坐标
    void handleReleaseLogic(const Vec2 &touchPoint);
    bool bePressed;
    float slidTime;
    Vec2 touchBeganPoint;
    Vec2 touchMovedPoint;
    Vec2 touchEndedPoint;
    Vec2 touchMovingPoint;

    virtual void checkChildInfo(int handleState,Widget* sender,const Vec2 &touchPoint) override;
    float childFocusCancelOffset;
    void change();
    
    Widget* model;//模版
    Vector<Widget*> items;
    rotateListCallback eventCallback;
    
};
#endif /* defined(__fancyHeart__RotateList__) */