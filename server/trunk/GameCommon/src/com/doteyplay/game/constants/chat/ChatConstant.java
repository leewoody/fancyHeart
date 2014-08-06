package com.doteyplay.game.constants.chat;
/**
 * @className:GMConstant.java
 * @classDescription:
 * @author:Tom.Zheng
 * @createTime:2014年7月9日 下午3:41:28
 */
public enum ChatConstant {
	
	//----------------------用于GM物品操作的格式串
	//goods[10000,100001,10002][100x1,101x2,103x3,104x1] 
	//整个字符串的意思是指.goods[roleId,roleId,roleId][物品Idx数量,物品Idx数量,物品Idx数量]
	GOODS_SUFFIX("goods",0),
	/**
	 * money[10000,100001,10002][100]
	 * 整个字符串的意思是指.goods[roleId,roleId,roleId][金币数量] 
	 */
	MONEY_SUFFIX ("money",1),
	/**
	 * exp[10000,100001,10002][100]
	 * 整个字符串的意思是指.exp[roleId,roleId,roleId][经验值] 
	 */
	EXP_SUFFIX("exp",1),
	/**
	 * energy[10000,100001,10002][100]
	 * 整个字符串的意思是指.exp[roleId,roleId,roleId][体力值] 
	 */
	ENERGY_SUFFIX("energy",1),;

	private String suffix;
	
	private int  dataType;
	private ChatConstant(String name,int dataType) {
		this.suffix = name;
		this.dataType= dataType;
		// TODO Auto-generated constructor stub
	}
	public String getSuffix() {
		return suffix;
	}
	public int getDataType() {
		return dataType;
	}
	
	public boolean isGoodsType(){
		return dataType == 0;
	}
	public boolean isDataValueType(){
		return dataType == 1;
	}
	
	
}
