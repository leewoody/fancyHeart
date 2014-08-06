package com.doteyplay.game.config.template;

import com.doteyplay.game.util.SimpleReflectUtils;
import com.doteyplay.game.util.excel.ExcelCellBinding;
import com.doteyplay.game.util.excel.ExcelRowBinding;
import com.doteyplay.game.util.excel.TemplateConfigException;
import com.doteyplay.game.util.excel.TemplateObject;

@ExcelRowBinding
public class MonsterDataTemplate extends TemplateObject {

	protected int a = 0;
	/**
	 * 战前动画id
	 */
	@ExcelCellBinding
	protected int battleBeforeId;

	/**
	 * 战后动画id
	 */
	@ExcelCellBinding
	protected int battleAfterId;
	/**
	 * 是否为boss战
	 */
	@ExcelCellBinding
	protected int monsterIsBoss;
	/**
	 * 怪物1Id
	 */
	@ExcelCellBinding
	protected int monster1Id;
	/**
	 * 怪物1等级
	 */
	@ExcelCellBinding
	protected int monster1Level;
	/**
	 * 怪物1星级
	 */
	@ExcelCellBinding
	protected int monster1StarLevel;
	/**
	 * 怪物2Id
	 */
	@ExcelCellBinding
	protected int monster2Id;
	/**
	 * 怪物2等级
	 */
	@ExcelCellBinding
	protected int monster2Level;
	/**
	 * 怪物2星级
	 */
	@ExcelCellBinding
	protected int monster2StarLevel;
	/**
	 * 怪物3Id
	 */
	@ExcelCellBinding
	protected int monster3Id;
	/**
	 * 怪物3等级
	 */
	@ExcelCellBinding
	protected int monster3Level;
	/**
	 * 怪物3星级
	 */
	@ExcelCellBinding
	protected int monster3StarLevel;
	/**
	 * 怪物4Id
	 */
	@ExcelCellBinding
	protected int monster4Id;
	/**
	 * 怪物4等级
	 */
	@ExcelCellBinding
	protected int monster4Level;
	/**
	 * 怪物4星级
	 */
	@ExcelCellBinding
	protected int monster4StarLevel;
	/**
	 * 怪物5Id
	 */
	@ExcelCellBinding
	protected int monster5Id;
	/**
	 * 怪物5等级
	 */
	@ExcelCellBinding
	protected int monster5Level;
	/**
	 * 怪物5星级
	 */
	@ExcelCellBinding
	protected int monster5StarLevel;

	@Override
	public void check() throws TemplateConfigException {
		// TODO Auto-generated method stub

	}


	public int getMonster1Id() {
		return monster1Id;
	}

	public void setMonster1Id(int monster1Id) {
		this.monster1Id = monster1Id;
	}

	public int getMonster1Level() {
		return monster1Level;
	}

	public void setMonster1Level(int monster1Level) {
		this.monster1Level = monster1Level;
	}

	public int getMonster1StarLevel() {
		return monster1StarLevel;
	}

	public void setMonster1StarLevel(int monster1StarLevel) {
		this.monster1StarLevel = monster1StarLevel;
	}

	public int getMonster2Id() {
		return monster2Id;
	}

	public void setMonster2Id(int monster2Id) {
		this.monster2Id = monster2Id;
	}

	public int getMonster2Level() {
		return monster2Level;
	}

	public void setMonster2Level(int monster2Level) {
		this.monster2Level = monster2Level;
	}

	public int getMonster2StarLevel() {
		return monster2StarLevel;
	}

	public void setMonster2StarLevel(int monster2StarLevel) {
		this.monster2StarLevel = monster2StarLevel;
	}

	public int getMonster3Id() {
		return monster3Id;
	}

	public void setMonster3Id(int monster3Id) {
		this.monster3Id = monster3Id;
	}

	public int getMonster3Level() {
		return monster3Level;
	}

	public void setMonster3Level(int monster3Level) {
		this.monster3Level = monster3Level;
	}

	public int getMonster3StarLevel() {
		return monster3StarLevel;
	}

	public void setMonster3StarLevel(int monster3StarLevel) {
		this.monster3StarLevel = monster3StarLevel;
	}

	public int getMonster4Id() {
		return monster4Id;
	}

	public void setMonster4Id(int monster4Id) {
		this.monster4Id = monster4Id;
	}

	public int getMonster4Level() {
		return monster4Level;
	}

	public void setMonster4Level(int monster4Level) {
		this.monster4Level = monster4Level;
	}

	public int getMonster4StarLevel() {
		return monster4StarLevel;
	}

	public void setMonster4StarLevel(int monster4StarLevel) {
		this.monster4StarLevel = monster4StarLevel;
	}

	public int getMonster5Id() {
		return monster5Id;
	}

	public void setMonster5Id(int monster5Id) {
		this.monster5Id = monster5Id;
	}

	public int getMonster5Level() {
		return monster5Level;
	}

	public void setMonster5Level(int monster5Level) {
		this.monster5Level = monster5Level;
	}

	public int getMonster5StarLevel() {
		return monster5StarLevel;
	}

	public void setMonster5StarLevel(int monster5StarLevel) {
		this.monster5StarLevel = monster5StarLevel;
	}

	public int getBattleBeforeId() {
		return battleBeforeId;
	}

	public void setBattleBeforeId(int battleBeforeId) {
		this.battleBeforeId = battleBeforeId;
	}

	public int getBattleAfterId() {
		return battleAfterId;
	}

	public void setBattleAfterId(int battleAfterId) {
		this.battleAfterId = battleAfterId;
	}

	public int getMonsterIsBoss() {
		return monsterIsBoss;
	}

	public void setMonsterIsBoss(int monsterIsBoss) {
		this.monsterIsBoss = monsterIsBoss;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method
		return super.toString()+SimpleReflectUtils.reflect(this);
	}

}
