package sample2.server;

import org.apache.mina.core.session.IoSession;

import com.doteyplay.luna.common.action.ActionController;
import com.doteyplay.luna.common.action.BaseAction;
import com.doteyplay.luna.common.message.DecoderMessage;

public class TestActionController implements ActionController {

	@Override
	public BaseAction getAction(DecoderMessage message) {
		return new TestAction();
	}

	@Override
	public void sessionClose(IoSession session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionOpen(IoSession session)
	{
		// TODO Auto-generated method stub
		
	}

}