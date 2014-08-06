package com.doteyplay.game.config.template;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.doteyplay.game.constants.DamageType;
import com.doteyplay.game.constants.sprite.SpritePositionType;
import com.doteyplay.game.gamedata.data.sprite.SpriteQualityPropData;
import com.doteyplay.game.gamedata.data.sprite.SpriteStarData;
import com.doteyplay.game.util.excel.ExcelRowBinding;
import com.doteyplay.game.util.excel.TemplateService;

@ExcelRowBinding
public class SpriteDataObject extends SpriteDataTemplate
{
	public DamageType attackType = DamageType.PHYSICS;
	public SpritePositionType positionType = SpritePositionType.POSITION_TYPE_F;
	
	public List<SpriteQualityPropData> propDataList = new ArrayList<SpriteQualityPropData>();
	public List<SpriteStarData> starDataList = new ArrayList<SpriteStarData>();
	
	public SkillDataObject commonAtkSkill;
	public List<SkillDataObject> skillList = new ArrayList<SkillDataObject>();
	public List<SkillDataObject> wakeSkillList = new ArrayList<SkillDataObject>();
	
	
	@Override
	public void patchUp() throws Exception
	{
		this.positionType = SpritePositionType.values()[positionTypeValue];
		this.commonAtkSkill = TemplateService.getInstance().get(commonAtkId, SkillDataObject.class);
		
		SkillDataObject skill = null;
		if(skill1 > 0)
		{
			skill = TemplateService.getInstance().get(skill1, SkillDataObject.class);
			skillList.add(skill);
		}
		if(skill2 > 0)
		{
			skill = TemplateService.getInstance().get(skill2, SkillDataObject.class);
			skillList.add(skill);
		}if(skill3 > 0)
		{
			skill = TemplateService.getInstance().get(skill3, SkillDataObject.class);
			skillList.add(skill);
		}if(skill4 > 0)
		{
			skill = TemplateService.getInstance().get(skill4, SkillDataObject.class);
			skillList.add(skill);
		}
		if(skill5 > 0)
		{
			skill = TemplateService.getInstance().get(skill5, SkillDataObject.class);
			skillList.add(skill);
		}
		if(skill6 > 0)
		{
			skill = TemplateService.getInstance().get(skill6, SkillDataObject.class);
			skillList.add(skill);
		}
		if(skill7 > 0)
		{
			skill = TemplateService.getInstance().get(skill7, SkillDataObject.class);
			skillList.add(skill);
		}
	}


	public DamageType getAttackType()
	{
		return attackType;
	}


	public void setAttackType(DamageType attackType)
	{
		this.attackType = attackType;
	}


	public SpritePositionType getPositionType()
	{
		return positionType;
	}


	public void setPositionType(SpritePositionType positionType)
	{
		this.positionType = positionType;
	}


	public List<SpriteQualityPropData> getPropDataList()
	{
		return propDataList;
	}


	public void setPropDataList(List<SpriteQualityPropData> propDataList)
	{
		this.propDataList = propDataList;
	}


	public List<SpriteStarData> getStarDataList()
	{
		return starDataList;
	}


	public void setStarDataList(List<SpriteStarData> starDataList)
	{
		this.starDataList = starDataList;
	}


	public SkillDataObject getCommonAtkSkill()
	{
		return commonAtkSkill;
	}


	public void setCommonAtkSkill(SkillDataObject commonAtkSkill)
	{
		this.commonAtkSkill = commonAtkSkill;
	}


	public List<SkillDataObject> getSkillList()
	{
		return skillList;
	}


	public void setSkillList(List<SkillDataObject> skillList)
	{
		this.skillList = skillList;
	}


	public List<SkillDataObject> getWakeSkillList()
	{
		return wakeSkillList;
	}


	public void setWakeSkillList(List<SkillDataObject> wakeSkillList)
	{
		this.wakeSkillList = wakeSkillList;
	}
	
	public static void main(String[] args)
	{
		for(Field f:SpriteDataObject.class.getFields())
		{
			if(f.getAnnotations() != null)
				System.out.println(f.getName());
		}
	}
}
