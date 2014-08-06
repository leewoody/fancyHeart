package com.doteyplay.game.domain.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.doteyplay.core.dbcs.DBCS;
import com.doteyplay.game.CommonConstants;
import com.doteyplay.game.config.template.SpriteDataObject;
import com.doteyplay.game.constants.IdType;
import com.doteyplay.game.domain.common.IdGenerator;
import com.doteyplay.game.domain.gamebean.PetBean;
import com.doteyplay.game.domain.gamebean.SkillBean;
import com.doteyplay.game.domain.pet.Pet;
import com.doteyplay.game.persistence.serverdata.pet.IPetBeanDao;
import com.doteyplay.game.persistence.serverdata.skill.ISkillBeanDao;
import com.doteyplay.game.util.excel.TemplateService;

public class RolePetManager
{
	// 角色拥有的全部宠物
	private Map<Long, Pet> petMap = new HashMap<Long, Pet>();
	
	public static final int maxGroupNum = 5;
	/**
	 * 当前宠物Map
	 */
	private Map<Integer,List<Pet>> curPetMap = new HashMap<Integer,List<Pet>>();

	private Role role;

	public RolePetManager(Role role)
	{
		this.role = role;
		for (int i = 0; i < maxGroupNum; i++) {
			curPetMap.put(i, new ArrayList<Pet>());
		}
	}

	public void init()
	{
		IPetBeanDao petBeanDao = DBCS.getExector(IPetBeanDao.class);
		List<PetBean> petBeanList = petBeanDao.selectPetBeanListByRoleId(role
				.getRoleId());

		if (petBeanList == null || petBeanList.size() <= 0)
		{
			//临时默认值
			this.addPet(998);
		} else
		{
			ISkillBeanDao skillBeanDao = DBCS.getExector(ISkillBeanDao.class);
			List<SkillBean> skillBeanList = skillBeanDao
					.selectSkillBeanList(this.role.getRoleId());

			Map<Long, List<SkillBean>> tmpMap = new HashMap<Long, List<SkillBean>>();
			if (skillBeanList != null)
			{
				for (SkillBean skillBean : skillBeanList)
				{
					List<SkillBean> tmpList = tmpMap.get(skillBean
							.getPetId());
					if (tmpList == null)
					{
						tmpList = new ArrayList<SkillBean>();
						tmpMap.put(skillBean.getPetId(), tmpList);
					}
					tmpList.add(skillBean);
				}
			}

			for (PetBean bean : petBeanList)
			{
				Pet pet = new Pet(bean, this.role, tmpMap.get(bean.getId()));
				pet.init();
				addCurPetMap(pet);
				petMap.put(pet.getId(), pet);
			}
		}

	}

	public Map<Long, Pet> getPetMap()
	{
		return petMap;
	}

	public void setPetMap(Map<Long, Pet> petMap)
	{
		this.petMap = petMap;
	}

	public Role getRole()
	{
		return role;
	}

	public void setRole(Role role)
	{
		this.role = role;
	}

	public List<Pet> getCurPetList()
	{
		return curPetMap.get(role.getRoleBean().getPetGroupId());
	}


	public Pet getPetBySriteId(int petSpriteId)
	{
		for (Pet pet : petMap.values())
			if (pet.getSpriteDataObject().getId() == petSpriteId)
				return pet;
		return null;
	}

	public Pet addPet(int petSpriteId)
	{
		if (getPetBySriteId(petSpriteId) != null)
			return null;

		SpriteDataObject petSpriteData = TemplateService.getInstance().get(petSpriteId, SpriteDataObject.class);
		if (petSpriteData == null)
			return null;

		PetBean petBean = new PetBean();
		petBean.setAreaId(role.getRoleBean().getAreaId());
		petBean.setId(IdGenerator.getInstance().getId(IdType.PET_ID,
				petBean.getAreaId()));
		petBean.setLevel(1);
		petBean.setStar(petSpriteData.getInitStar());
		petBean.setRoleId(this.role.getRoleId());
		petBean.setSpriteId(petSpriteId);
		
		if(petSpriteData.getIsHero() == CommonConstants.TRUE){
			petBean.setSelected(CommonConstants.TRUE);
			petBean.setGroupStr("0,1,2,3,4");
		}else{
			petBean.setGroupStr("");
		}

		IPetBeanDao petBeanDao = DBCS.getExector(IPetBeanDao.class);
		petBeanDao.insertPetBean(petBean);

		Pet pet = new Pet(petBean, this.role, null);
		pet.init();

		this.petMap.put(pet.getId(), pet);
		addCurPetMap(pet);
		return pet;
	}
	
	private void addCurPetMap(Pet pet){
		//当前宠物列表中没有加上.
		String[] petGroupStr = pet.getBean().getGroupStr().split(",");
		for (String groupId : petGroupStr) {
			if(groupId==null||groupId.equals("")){
				continue;
			}
			int group = Integer.parseInt(groupId);
			if(this.curPetMap.containsKey(group)){
				this.curPetMap.get(group).add(pet);
			}
		}
	}
	
	
	public void onLeaveGame()
	{
		for(Pet pet:this.petMap.values())
			pet.onLeaveGame();
	}
	
	/**
	 * 添加当前出战的宠物的经验.
	 * @param petExp
	 */
	public synchronized void addCurPetListExp(int currentGroupId,int petExp){
		if(this.curPetMap.containsKey(currentGroupId)){
			for (Pet pet : this.curPetMap.get(currentGroupId)) {
				pet.addExp(petExp);
			}
		}
		
	}
	
	/**
	 * 添加当前出战的宠物的经验.
	 * @param petExp
	 */
	public synchronized void addCurPetListExp(int petExp){
		addCurPetListExp(role.getRoleBean().getPetGroupId(), petExp);
	}
	
	/**
	 * 将英雄加入组内.
	 * 需要验证,英雄本身所在的组的情况.
	 * @param groupId
	 * @param pet
	 * @return 添加失败.因为原来就有此组.
	 */
	public boolean addPetToGroup(int groupId,Pet pet){
		String group = pet.getBean().getGroupStr();
		if(group!=null&&!group.equals("")&&group.indexOf(String.valueOf(groupId))>-1){
			return false;
		}else{
			this.curPetMap.get(groupId).add(pet);
			String tempGroup = "";
			if(group==null){
				tempGroup = String.valueOf(groupId);
			}else{
				tempGroup = group+","+String.valueOf(groupId);
			}
			pet.getBean().setGroupStr(group+","+groupId);
			return true;
		}
		
	}
	/**
	 * 从某组中清理掉,原有的宠物.并清理掉宠物的标识.
	 * @param groupId
	 * @param pet
	 * @return
	 */
	public boolean removePetFromGroup(int groupId,Pet pet){
		String group = pet.getBean().getGroupStr();
		if(group!=null&&!group.equals("")&&group.indexOf(String.valueOf(groupId))>-1){
			this.curPetMap.get(groupId).remove(pet);
			String[] split = group.split(",");
			String tempGroup = String.valueOf(groupId);
			String result = "";
			for (String str : split) {
				if(str!=null&&str.equals(tempGroup)){
					
				}else{
					if(result.equals("")){
						result=result+""+str;
					}else{
						result=result+","+str;
					}
				}
			}
			pet.getBean().setGroupStr(result);
			return true;
		}else{
			return false;
		}
		
	}

	public Map<Integer, List<Pet>> getCurPetMap() {
		return curPetMap;
	}
	
	
}
