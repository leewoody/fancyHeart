package com.doteyplay.game.service.bo.virtualworld;

import com.doteyplay.core.bhns.ISimpleService;
import com.doteyplay.game.BOConst;
import com.doteyplay.game.domain.gamebean.UserBean;

public interface IVirtualWorldService extends ISimpleService
{
	public final static int PORTAL_ID = BOConst.BO_WORLD;

	public void initialize();

	public void doLogin(long sessionId, String account, String sessionKey,int areaId);
	
	public void loginResult(boolean success, UserBean userBean, long sessionId);

	public void doLogout(long roleId);
	
	public void doGm(String source);
}
