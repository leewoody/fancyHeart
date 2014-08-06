//
//  Mask.cpp
//  fancyHeart
//
//  Created by zhai on 14-6-25.
//
//

#include "Mask.h"
static Mask* instance;
Mask* Mask::getInstance()
{
    if (instance==nullptr) {
        instance=new Mask();
        instance->init();
    }
    return instance;
}

bool Mask::init()
{
    if(!Layout::init())
    {
        return false;
    }
    Size size=Director::getInstance()->getWinSize();
    this->setSize(size);
    this->setBackGroundColorType(BackGroundColorType::SOLID);
    this->setBackGroundColor(Color3B::BLACK);
    this->setBackGroundColorOpacity(150);
    this->setVisible(false);
    return true;
}

void Mask::show()
{
    this->setVisible(true);
}

void Mask::hide()
{
    this->setVisible(false);
}