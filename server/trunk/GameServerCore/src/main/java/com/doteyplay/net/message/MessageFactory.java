package com.doteyplay.net.message;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.log4j.Logger;

/**
 * 
 * @author Administrator
 * 
 */
public class MessageFactory
{
	/**
	 * 唯一实例
	 */

	private int messageNum;

	private static final Logger logger = Logger.getLogger(MessageFactory.class);

	private static final Class<?>[] EMPTY_PARAMS = new Class[0];

	private final Class<? extends AbstractMessage>[] messageClasses;

	private final MessageActionHelper[] actionHelpers;
	@SuppressWarnings("unchecked")
	public MessageFactory(int maxNum)
	{
		messageNum = maxNum;
		messageClasses = new Class[maxNum];
		actionHelpers = new MessageActionHelper[maxNum];
	}


	/**
	 * 获取消息对象
	 * 
	 * @param commandId
	 * @return
	 * @throws Exception
	 */
	public final AbstractMessage getMessage(int commandId)
	{
		if (commandId < 0 || commandId >= messageNum)
			return null;

		try
		{
			Class<? extends AbstractMessage> cls = messageClasses[commandId];
			if (cls == null)
			{
				return null;
			}
			AbstractMessage message = cls.newInstance();
			return message;
		} catch (Exception e)
		{
			logger.error("getMessage(int) - commandId=" + commandId + ". ", e); //$NON-NLS-1$
			//            logger.error("getMessage(int) - commandId=" + commandId + ", activeNum=" + messageObjectPool.getNumActive() + ", idleNum=" + messageObjectPool.getNumIdle(), e); //$NON-NLS-1$
		}
		return null;
	}

	/**
	 * 释放消息对象
	 * 
	 * @param message
	 */
	public final void freeMessage(AbstractMessage message)
	{
//		if (message.isPersist())
//		{
//			// 持久时不释放
//			return;
//		}

		try
		{
			message.release();
		} catch (Exception e)
		{
			logger.error(
					"freeMessage(AbstractMessage) - message=" + message + ". ", e); //$NON-NLS-1$

		}

	}

	public void addMessage(int commandId,
			Class<? extends AbstractMessage> messageClass)
	{
		if (messageClass == null)
		{
			throw new NullPointerException("messageClass");
		}

		try
		{
			messageClass.getConstructor(EMPTY_PARAMS);
		} catch (NoSuchMethodException e)
		{
			throw new IllegalArgumentException(
					"The specified class doesn't have a public default constructor: commandId="
							+ commandId);
		}

		if (messageClasses[commandId] == messageClass)
		{
			throw new IllegalArgumentException(
					"The messageClasses has already the commandId mapping class: commandId="
							+ commandId);
		}

		boolean registered = false;
		if (AbstractMessage.class.isAssignableFrom(messageClass))
		{
			messageClasses[commandId] = messageClass;
			registered = true;
		}

		if (!registered)
		{
			throw new IllegalArgumentException("Unregisterable type: "
					+ messageClass);
		}

	}

	public final MessageActionHelper getMessageActionHelper(int commandId)
	{
		return actionHelpers[commandId];
	}

	public void addMessageAction(int commandId, MessageActionHelper actionInfo)
	{
		if (actionHelpers[commandId] != null)
			throw new IllegalArgumentException("重复的Action设置, commandId="
					+ commandId + ", actionInfo=" + actionInfo);

		actionHelpers[commandId] = actionInfo;
	}

	public void addMessageAndAction(int commandId,
			Class<? extends AbstractMessage> messageClass,
			MessageActionHelper actionInfo)
	{
		addMessage(commandId, messageClass);
		addMessageAction(commandId, actionInfo);
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < messageClasses.length; i++)
		{
			// String name = MessageUtil.getMessageCommandName(i);
			Class<?> messageClass = messageClasses[i];
			MessageActionHelper helper = actionHelpers[i];

			sb.append("\n[").append(i);
			sb.append("]  MessageClass=");
			sb.append(messageClass == null ? "null" : messageClass
					.getSimpleName());
			if (helper != null)
				sb.append("  ActionClass=").append(
						helper.getAction().getClass().getSimpleName());
		}
		return sb.toString();
	}

	class _PoolableMessageFactory extends BasePoolableObjectFactory
	{
		int commandId;

		_PoolableMessageFactory(int commandId)
		{
			this.commandId = commandId;
		}

		@Override
		public Object makeObject() throws Exception
		{
			Class<? extends AbstractMessage> cls = messageClasses[commandId];
			if (cls == null)
			{
				return null;
			}
			AbstractMessage message = messageClasses[commandId].newInstance();
			return message;
		}

		@Override
		public void passivateObject(Object arg0) throws Exception
		{
			AbstractMessage message = (AbstractMessage) arg0;
			message.release();
		}
	}

}
