package com.doteyplay.game.domain.gamebean;

import java.util.Date;

public class RoleBean
{
	private long id;
	private long userId;
	private String name;
	protected int level;
	protected int exp;
	private int money;
	private int rmb;
	private int energy;
	private int vipLevel;
	private int areaId;
	private Date lastLoginTime;
	private Date lastLogoutTime;
	private Date createTime;
	private int petGroupId;
	
	public long getUserId()
	{
		return userId;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getMoney()
	{
		return money;
	}

	public void setMoney(int money)
	{
		this.money = money;
	}

	public int getRmb()
	{
		return rmb;
	}

	public void setRmb(int rmb)
	{
		this.rmb = rmb;
	}

	public int getEnergy()
	{
		return energy;
	}

	public void setEnergy(int energy)
	{
		this.energy = energy;
	}

	public int getVipLevel()
	{
		return vipLevel;
	}

	public void setVipLevel(int vipLevel)
	{
		this.vipLevel = vipLevel;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public int getExp()
	{
		return exp;
	}

	public void setExp(int exp)
	{
		this.exp = exp;
	}

	public int getAreaId()
	{
		return areaId;
	}

	public void setAreaId(int areaId)
	{
		this.areaId = areaId;
	}

	public Date getLastLoginTime()
	{
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime)
	{
		this.lastLoginTime = lastLoginTime;
	}

	public Date getLastLogoutTime()
	{
		return lastLogoutTime;
	}

	public void setLastLogoutTime(Date lastLogoutTime)
	{
		this.lastLogoutTime = lastLogoutTime;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public int getPetGroupId() {
		return petGroupId;
	}

	public void setPetGroupId(int petGroupId) {
		this.petGroupId = petGroupId;
	}
	
}
