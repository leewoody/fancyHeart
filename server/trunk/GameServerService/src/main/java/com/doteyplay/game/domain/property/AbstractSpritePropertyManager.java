package com.doteyplay.game.domain.property;

import com.doteyplay.game.CommonConstants;
import com.doteyplay.game.constants.sprite.SpritePropType;
import com.doteyplay.game.domain.sprite.AbstractSprite;
import com.doteyplay.game.gamedata.data.sprite.SpriteQualityPropData;

public abstract class AbstractSpritePropertyManager
{
	protected int[] properties = new int[SpritePropType.values().length];

	protected AbstractSprite owner;

	protected AbstractSpritePropertyManager(AbstractSprite owner)
	{
		this.owner = owner;
	}

	public int getPropertyById(SpritePropType type)
	{
		return properties[type.ordinal()];
	}

	public void setPropertyValue(SpritePropType type, int value)
	{
		properties[type.ordinal()] = value;
	}

	public void init()
	{
		this.recalculateProps();
	}

	public void recalculateProps()
	{
		for (SpriteQualityPropData data : owner.getSpriteDataObject().propDataList)
		{
			if (data.getSpriteQualityType().ordinal() == owner.getSpriteBean().getQuality())
			{
				properties[data.getSpriteQualityType().ordinal()] = data.getBaseValue()
						+ owner.getSpriteBean().getLevel()
						* (data.getRate() + owner.getSpriteDataObject().starDataList
								.get(owner.getSpriteBean().getStar()).propRate
								.get(data.getSpritePropType().ordinal())) / 10000;
			}
		}
	}

	public boolean modifyProperty(SpritePropType type, int addValue, int addType)
	{
		if (addType == CommonConstants.ADDTYPE_VALUE)
			this.setPropertyValue(type, this.getPropertyById(type) + addValue);
		else
		{
			int value = (this.getPropertyById(type) * (10000 + addType)) / 10000;
			this.setPropertyValue(type, value);
		}
		return true;
	}

	public boolean setProperty(SpritePropType type, int setValue)
	{
		this.setPropertyValue(type, setValue);
		return true;
	}

}
