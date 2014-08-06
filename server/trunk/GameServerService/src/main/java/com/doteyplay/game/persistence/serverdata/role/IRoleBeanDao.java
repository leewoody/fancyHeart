package com.doteyplay.game.persistence.serverdata.role;

import com.doteyplay.core.dbcs.daoproxy.IDaoExecutor;
import com.doteyplay.core.dbcs.executor.DAOInfo;
import com.doteyplay.game.domain.gamebean.RoleBean;

public interface IRoleBeanDao extends IDaoExecutor
{
	@DAOInfo(Params = "")
	public RoleBean selectRoleBean(long roleId);
	
	@DAOInfo(Params = "userId,areaId")
	public RoleBean selectRoleBeanByUserId(long userId,int areaId);

	@DAOInfo(Params = "")
	public RoleBean selectRoleBeanByName(String roleName);

	@DAOInfo(Params = "")
	public void insertRoleBean(RoleBean roleBean);

	@DAOInfo(Params = "")
	public void updateRoleBean(RoleBean roleBean);

}