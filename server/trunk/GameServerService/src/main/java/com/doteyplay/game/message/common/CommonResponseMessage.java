package com.doteyplay.game.message.common;

import org.apache.mina.core.buffer.IoBuffer;

import com.doteyplay.game.MessageCommands;
import com.doteyplay.game.message.proto.CommonRespProBuf.PCommonResp;
import com.doteyplay.net.message.AbstractMessage;

/**
 * @className:CommonResponse.java
 * @classDescription:
 * @author:Tom.Zheng
 * @createTime:2014��7��18�� ����6:07:01
 */
public class CommonResponseMessage extends AbstractMessage{

	/**
	 * @author:Tom.Zheng
	 * @createTime:2014��7��18�� ����6:07:39
	 */
	private static final long serialVersionUID = 7532255243860103766L;
	
	private int resultType;
	
	private int state;
	

	public CommonResponseMessage() {
		super(MessageCommands.COMMONS_RESPONSE_MESSAGE);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void release() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decodeBody(IoBuffer in) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encodeBody(IoBuffer out) {
		// TODO Auto-generated method stub
		PCommonResp.Builder builder = PCommonResp.newBuilder();
		builder.setResultType(resultType);
		builder.setStatus(state);
		out.put(builder.build().toByteArray());
	}

	public int getResultType() {
		return resultType;
	}

	public void setResultType(int resultType) {
		this.resultType = resultType;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	

}