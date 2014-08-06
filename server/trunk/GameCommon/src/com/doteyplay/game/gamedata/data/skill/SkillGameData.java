package com.doteyplay.game.gamedata.data.skill;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.doteyplay.game.constants.ActivateEvent;
import com.doteyplay.game.constants.DamageType;
import com.doteyplay.game.constants.skill.SkillActionType;
import com.doteyplay.game.constants.skill.SkillAmmoType;
import com.doteyplay.game.constants.skill.SkillEffectRange;
import com.doteyplay.game.constants.skill.SkillTargetSelectType;
import com.doteyplay.game.gamedata.data.IGameData;

public class SkillGameData implements IGameData
{
	/**
	 * 等级总数
	 */
	public int levelCount;
	/**
	 * 描述id
	 */
	public int descId;
	/**
	 * 施法动作效果id
	 */
	public String resSkillActionId="";
	/**
	 * 施法特效id
	 */
	public String resSkillEffectId="";
	/**
	 * 被攻击效果id
	 */
	public String resBeAttackId="";

	/**
	 * 影响范围
	 */
	public SkillEffectRange effectRange = SkillEffectRange.SELF;
	/**
	 * 伤害类型
	 */
	public DamageType damageType = DamageType.PHYSICS;
	/**
	 * 事件触发
	 */
	public ActivateEvent event = ActivateEvent.NONE;
	
	/**
	 * 事件参数
	 */
	public String eventParam = "";

	/**
	 * 弹道类型
	 */
	public SkillAmmoType ammoType = SkillAmmoType.DEFAULT;

	/**
	 * 选择策略
	 */
	public SkillTargetSelectType selectType = SkillTargetSelectType.DEFAULT;

	/**
	 * 主动类型
	 */
	public SkillActionType actionType = SkillActionType.INITIATIVE;
	
	/**
	 * 引导次数
	 */
	public int leadCount;
	
	/**
	 * 两次引导之间的差值
	 */
	public int leadTimeDelta;

	/**
	 * 技能消耗法力豆
	 */
	public int cost;
	
	/////////////////////////////////////蓄力相关///////////////////////////////////////////
	
	/**
	 * 蓄力时间
	 */
	public int storageTime;
	
	/**
	 * 起始加成
	 */
	public int storageStartRate;
	
	/**
	 * 起始加值
	 */
	public int storageStartValue;
	
	/**
	 * 结束加成
	 */
	public int storageEndRate;
	
	/**
	 * 结束加值
	 */
	public int storageEndValue;
	/////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 等级数据
	 */
	public List<SkillLevelGameData> levelDataList = new  ArrayList<SkillLevelGameData>();
	

	@Override
	public void load(DataInputStream in) throws IOException
	{
		this.levelCount = in.readInt();
		this.descId = in.readInt();
		
		this.resSkillActionId = in.readUTF();
		this.resSkillEffectId = in.readUTF();
		this.resBeAttackId = in.readUTF();

		this.effectRange = SkillEffectRange.values()[in.readByte()];
		this.damageType = DamageType.values()[in.readByte()];
		this.event = ActivateEvent.values()[in.readByte()];
		this.eventParam = in.readUTF();
		
		this.ammoType = SkillAmmoType.values()[in.readByte()];
		this.selectType = SkillTargetSelectType.values()[in.readByte()];
		this.actionType = SkillActionType.values()[in.readByte()];
		
		this.leadCount = in.readInt();
		this.leadTimeDelta = in.readInt();
		
		this.cost = in.readInt();
		
		this.storageTime = in.readInt();
		this.storageStartRate = in.readInt();
		this.storageStartValue = in.readInt();
		this.storageEndRate = in.readInt();
		this.storageEndValue = in.readInt();
				
		int size = in.readInt();
		for(int i = 0; i < size ; i ++)
		{
			SkillLevelGameData data = new SkillLevelGameData();
			data.load(in);
			levelDataList.add(data);
		}
	}

	@Override
	public void save(DataOutputStream out) throws IOException
	{
		out.writeInt(levelCount);
		out.writeInt(descId);
		
		out.writeUTF(resSkillActionId);
		out.writeUTF(resSkillEffectId);
		out.writeUTF(resBeAttackId);
		
		out.writeByte(this.effectRange.ordinal());
		out.writeByte(this.damageType.ordinal());
		out.writeByte(this.event.ordinal());
		
		out.writeUTF(this.eventParam);
		
		out.writeByte(this.ammoType.ordinal());
		out.writeByte(this.selectType.ordinal());
		out.writeByte(this.actionType.ordinal());
		
		out.writeInt(leadCount);
		out.writeInt(leadTimeDelta);
		
		out.writeInt(cost);
		
		out.writeInt(storageTime);
		out.writeInt(storageStartRate);
		out.writeInt(storageStartValue);
		out.writeInt(storageEndRate);
		out.writeInt(storageEndValue);
		
		out.writeInt(this.levelDataList.size());
		for(int i = 0 ; i < levelDataList.size() ; i ++)
		{
			levelDataList.get(i).save(out);
		}
		
	}

}
