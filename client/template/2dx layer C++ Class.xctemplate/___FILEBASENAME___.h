//
//  ___FILENAME___
//  ___PROJECTNAME___
//
//  Created by ___FULLUSERNAME___ on ___DATE___.
//___COPYRIGHT___
//

#ifndef _____PROJECTNAMEASIDENTIFIER________FILEBASENAMEASIDENTIFIER_____
#define _____PROJECTNAMEASIDENTIFIER________FILEBASENAMEASIDENTIFIER_____

#include <iostream>
#include "cocos2d.h"
#include "Manager.h"
#include "BaseUI.h"

using namespace ui;
using namespace cocos2d;
using namespace cocostudio;

class ___FILEBASENAMEASIDENTIFIER___:public BaseUI
{
public:
	CREATE_FUNC(___FILEBASENAMEASIDENTIFIER___);
	virtual bool init();
	virtual void onEnter();
	virtual void onExit();
	virtual void show(BaseUI* preUI,int effectType=0);
    virtual void clear(bool isDel);
	//virtual void onDlgClose(std::string data);

private:
	void initNetEvent();
	void touchEvent(Ref *pSender, TouchEventType type);

private: //私有属性
 	BaseUI* preUI;

};
#endif /* defined(_____PROJECTNAMEASIDENTIFIER________FILEBASENAMEASIDENTIFIER_____) */
