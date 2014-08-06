package com.doteyplay.game.startservice;

import com.doteyplay.core.server.service.IServerService;
import com.doteyplay.core.server.service.IServerServiceException;
import com.doteyplay.game.CommonConstants;
import com.doteyplay.game.config.template.NpcExpDataTemplate;
import com.doteyplay.game.config.template.RoleExpDataTemplate;
import com.doteyplay.game.util.FileUtil;
import com.doteyplay.game.util.excel.TemplateService;

public class DataStoreService implements IServerService
{

	/**
	 * 单例模式
	 */
	private static DataStoreService service = new DataStoreService();

	private DataStoreService()
	{
	}

	public static DataStoreService getInstance()
	{
		return service;
	}

	@Override
	public void onReady() throws IServerServiceException
	{
		// 获取配置信息的加载项，初始化数据
		TemplateService.getInstance().init(
				FileUtil.getConfigURL("template.xml"));

		int i = 1 ;
		int tmpExp = 0;
		for (RoleExpDataTemplate expData : TemplateService.getInstance()
				.getAll(RoleExpDataTemplate.class).values())
		{
			 CommonConstants.ROLE_LEVEL_EXP[i] = expData.getExp() + tmpExp;
			 tmpExp = CommonConstants.ROLE_LEVEL_EXP[i];
			i++;
		}
		
		i = 1;
		tmpExp = 0;
		for (NpcExpDataTemplate expData : TemplateService.getInstance()
				.getAll(NpcExpDataTemplate.class).values())
		{
			CommonConstants.NPC_LEVEL_EXP[i] = expData.getExp() + tmpExp;
			tmpExp = CommonConstants.NPC_LEVEL_EXP[i];
			i++;
		}
		// data:TemplateService.getInstance().getAll(BattleDataTemplate.class).values())
		// {
		// System.out.println(data);
		// }
	}

	@Override
	public void onStart() throws IServerServiceException
	{
	}

	@Override
	public void onDown() throws IServerServiceException
	{
	}

}
