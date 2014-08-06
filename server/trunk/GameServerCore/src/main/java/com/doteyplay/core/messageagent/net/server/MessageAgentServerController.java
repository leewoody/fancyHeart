package com.doteyplay.core.messageagent.net.server;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.doteyplay.luna.common.action.ActionController;
import com.doteyplay.luna.common.action.BaseAction;
import com.doteyplay.luna.common.message.DecoderMessage;
public class MessageAgentServerController implements ActionController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MessageAgentServerController.class);

	HashMap<Short, BaseAction> actionmap = new HashMap<Short, BaseAction>();

	@Override
	public BaseAction getAction(DecoderMessage arg0) {
		if (actionmap.containsKey(arg0.getCommandId()))
			return actionmap.get(arg0.getCommandId());
		else
			return actionmap.get((short) 0);
	}

	public void addAction(short commandid, BaseAction baseaction) {
		actionmap.put(commandid, baseaction);
	}

	@Override
	public void sessionClose(IoSession conn) {
		if (conn == null) {
			return;
		}

		try {
			conn.close(true);
		} catch (Exception e) {
			logger.error("�ر�����ʧ��",e);
		}
	}

	@Override
	public void sessionOpen(IoSession session)
	{
	}

}
