package com.doteyplay.game.service.pipeline;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;

import com.doteyplay.core.bhns.ISimpleService;
import com.doteyplay.core.configloader.MessageRegistry;
import com.doteyplay.game.MessageCommands;
import com.doteyplay.net.message.AbstractMessage;
import com.doteyplay.net.message.IMessageAction;
import com.doteyplay.net.message.MessageActionHelper;
import com.doteyplay.net.protocol.IServicePipeline;

public class DefaultServicePipeline implements IServicePipeline
{
	private static Logger logger = Logger.getLogger(DefaultServicePipeline.class);

	public void dispatchAction(ISimpleService service, IoBuffer buffer,
			long sessionId)
	{

		short commandId = buffer
				.getShort(AbstractMessage.MESSAGE_COMMANDID_INDEX);
		AbstractMessage message = MessageRegistry.getInstance().getMessage(
				commandId);
		if (message == null)
		{
			return;
		}

		MessageActionHelper helper = MessageRegistry.getInstance()
				.getMessageActionHelper(commandId);

		if (helper.isClosed())
		{
			logger.error("此功能关闭,CommandId=" + commandId); // 暂时先用这个
			return;
		}

		try
		{
			message.decodeMessage(buffer);
			message.setSessionId(sessionId);
		} catch (Exception e)
		{
			logger.error(
					"dispatchAction(IoSession, IoBuffer) -消息解析错误- buffer=" + buffer + ", commandId=" + commandId + ", message=" + message, e); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		try
		{
			IMessageAction action = helper.getAction();

			if (service != null)
			{
				try
				{
					action.processMessage(message, service);
				} catch (Exception e)
				{
					String str = e.getMessage();
					if (str == null)
						str = "";
					StringBuilder sb = new StringBuilder(str);
					sb.append("\n参数：").append(service.getServiceId());
					sb.append("\n消息：").append(message);
					logger.error(sb.toString(), e);
				}
			} else
			{
				logger.error("****强制关闭会话**** : 消息处理时参数对象为空[" + "commandId="
						+ MessageCommands.getMessageCommandName(commandId)
						+ "(" + commandId + "),message=" + message + "]");
				return;
			}
		} catch (Exception e)
		{
			logger.error(
					"dispatchAction(IoSession, AbstractMessage) -消息处理错误- message="
							+ message, e);
		}
		MessageRegistry.getInstance().getFactory().freeMessage(message);
	}

	// ///////////////////////////////////////////////////
	private final static DefaultServicePipeline instance = new DefaultServicePipeline();

	public static DefaultServicePipeline getInstance()
	{
		return instance;
	}

}
