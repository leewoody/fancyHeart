package com.doteyplay.game.service.pipeline;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.doteyplay.core.bhns.BOServiceManager;
import com.doteyplay.core.bhns.ISimpleService;
import com.doteyplay.core.objectpool.SimpleByteArrayPool;
import com.doteyplay.game.MessageCommands;
import com.doteyplay.game.domain.common.SessionHolder;
import com.doteyplay.game.service.runtime.GlobalSessionCache;
import com.doteyplay.net.message.AbstractMessage;

public class GatewayServerPipeline
{
	private static Logger logger = Logger
			.getLogger(GatewayServerPipeline.class);

	private SimpleByteArrayPool pool = new SimpleByteArrayPool(500, 2048);

	public void dispatchAction(IoSession session, IoBuffer buffer)
	{
		short commandId = buffer
				.getShort(AbstractMessage.MESSAGE_COMMANDID_INDEX);

		if (commandId > MessageCommands.MESSAGE_NUM)
		{
			logger.error("消息号不合法");
			return;
		}

		long timeRecord = System.currentTimeMillis();
		logger.debug("RECV_MESSAGE:"
				+ MessageCommands.getMessageCommandName(commandId));

		MessageCommands command = MessageCommands.values()[commandId];
		int boId = MessageCommands.getBoIdByCommandId(commandId);
		try
		{
			// ServiceMessageAction 消息转发到服务的节点

			SessionHolder sessionHolder = GlobalSessionCache.getInstance()
					.getBySession(session);
			if (sessionHolder == null && command.IS_NEED_AUTH)
			{
				logger.error("未登录，不能转发服务请求,session:" + session + ","
						+ GlobalSessionCache.printCurrentUidSessionMap());
				return;
			}

			long sessionId = sessionHolder == null ? GlobalSessionCache
					.getSessionIdByGatewayEndpoint(session) : sessionHolder
					.getSessionId();

			ISimpleService service = null;
			if (sessionHolder != null)
				service = BOServiceManager.findService(boId,
						sessionHolder.getCurRoleId());
			else
			{
				service = BOServiceManager.findDefaultService(boId);
				GlobalSessionCache.getInstance().addSession(session);
			}

			int byteLength = buffer.limit();
			byte[] tmpMessageBytes = pool.borrow();
			buffer.get(tmpMessageBytes, 0, byteLength);
			if (service.isValid())
			{
				service.messageReceived(tmpMessageBytes, sessionId);
			} else
			{
				logger.error("未激活服务，roleId = " + sessionHolder.getCurRoleId(),
						new UnsupportedOperationException("未激活服务，roleId = "
								+ sessionHolder.getCurRoleId()));
			}
			pool.free(tmpMessageBytes);
			
			logger.error("处理请求时间："+(System.currentTimeMillis() - timeRecord));
			return;

		} catch (Exception e)
		{
			logger.error("获取消息处理类失败", e);
		}
	}
}
