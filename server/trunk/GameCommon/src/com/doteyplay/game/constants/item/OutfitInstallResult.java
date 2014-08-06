package com.doteyplay.game.constants.item;

public enum OutfitInstallResult
{
	SUCCESS,
	//已经穿上了
	ITEM_HAS_EXIST,
	//物品不存在
	NOT_FOUND_ITEM,
	//等级不足
	LEVEL_LIMIT,
	//找不到穿装备的精灵
	NOT_FOUND_SPRITE,
	
	//装备不匹配
	ITEM_NOT_FIT,
	//物品数量不足
	ITEM_NOT_ENOUGH,
}
