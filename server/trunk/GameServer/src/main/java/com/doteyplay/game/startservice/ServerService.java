package com.doteyplay.game.startservice;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.doteyplay.core.server.service.IServerService;
import com.doteyplay.core.server.service.IServerServiceException;
import com.doteyplay.game.config.ServerConfig;
import com.doteyplay.game.service.pipeline.GatewayServerPipeline;
import com.doteyplay.net.AbstractTCPServer;
import com.doteyplay.net.codec.DefaultProtocolCodecFactory;
import com.doteyplay.net.protocol.DefaultSessionHandler;
import com.doteyplay.net.protocol.WebSocketIoHandler;

public class ServerService extends AbstractTCPServer implements IServerService
{
	private static final Logger logger = Logger.getLogger(ServerService.class);

	private static ServerService service = new ServerService();

	private ServerService()
	{
	}

	public static ServerService getInstance()
	{
		return service;
	}

	@Override
	public void onReady() throws IServerServiceException
	{
		logger.error("加载服务器配置信息");
		setServerPort(ServerConfig.SERVER_PORT);
		setProtocolCodeFactory(new DefaultProtocolCodecFactory());
		DefaultSessionHandler sessionHandler = new DefaultSessionHandler();
		sessionHandler.setPipeline(new GatewayServerPipeline());
		setSessionHandler(sessionHandler);
	}

	@Override
	public void onStart() throws IServerServiceException
	{
		try
		{
			this.start();
			logger.error("游戏服务启动! " + ServerConfig.SERVER_PORT);
		} catch (IOException e)
		{
			logger.error("TCP 服务启动失败!", e);
		}
	}

	@Override
	public void onDown() throws IServerServiceException
	{
		this.stop();
		logger.error("游戏连接服务关闭!");
	}

	@Override
	public void dispose()
	{
	}
}
