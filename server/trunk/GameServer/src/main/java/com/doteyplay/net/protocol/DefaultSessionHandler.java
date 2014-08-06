package com.doteyplay.net.protocol;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.doteyplay.game.service.pipeline.GatewayServerPipeline;
import com.doteyplay.game.service.runtime.GlobalSessionCache;

public class DefaultSessionHandler extends IoHandlerAdapter
{
	private static final Logger logger = Logger
			.getLogger(DefaultSessionHandler.class);

	private static final String Exception_Key = "ExceptionCounts";

	private GatewayServerPipeline pipeline;

	@Override
	public void sessionCreated(IoSession session)
	{
		session.setAttribute(Exception_Key, 0);
		if (logger.isDebugEnabled())
			logger.debug("createted session!");
	}

	@Override
	public void sessionOpened(IoSession session)
	{
		if (logger.isDebugEnabled())
			logger.debug("opened session!");
		// set idle time to 60 seconds
		session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60);
	}

	@Override
	public void sessionClosed(IoSession session)
	{
		if (logger.isDebugEnabled())
			logger.debug("closed session!");

		GlobalSessionCache.getInstance().disconnect(session);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
	{
//		if (logger.isDebugEnabled())
//			logger.debug("server receive message: " + message);

		pipeline.dispatchAction(session, (IoBuffer) message);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
	{
		// session.close(false);
		// logger.info("sessionIdle: " + session);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
	{
		if (cause instanceof java.io.IOException)
			return;
		logger.error(cause.getMessage(), cause);
	}


	public void setPipeline(GatewayServerPipeline pipeline)
	{
		this.pipeline = pipeline;
	}
}
