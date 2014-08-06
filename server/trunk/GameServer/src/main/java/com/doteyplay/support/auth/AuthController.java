package com.doteyplay.support.auth;

import org.apache.mina.core.session.IoSession;

import com.doteyplay.luna.common.action.ActionController;
import com.doteyplay.luna.common.action.BaseAction;
import com.doteyplay.luna.common.message.DecoderMessage;
import com.doteyplay.support.auth.action.CheckAuthAction;

/**
 */
@SuppressWarnings("deprecation")
public class AuthController implements ActionController,
		AuthCommand {

	private static AuthController controller = new AuthController();

	private AuthController() {

	}

	public synchronized static AuthController getInstance() {
		if (controller == null)
			controller = new AuthController();
		return controller;
	}

	@Override
	public BaseAction getAction(DecoderMessage message) {
		return getAction(message.getCommandId());
	}

	public BaseAction getAction(short commandId) {
		switch (commandId) {
		case CHECK_AUTH:
			return CheckAuthAction.getInstance();
		}
		return null;
	}

	@Override
	public void sessionClose(IoSession session) {
		
		
	}

	@Override
	public void sessionOpen(IoSession session)
	{
		
	}
}
