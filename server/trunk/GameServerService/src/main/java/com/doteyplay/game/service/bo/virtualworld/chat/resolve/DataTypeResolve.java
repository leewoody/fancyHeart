package com.doteyplay.game.service.bo.virtualworld.chat.resolve;

import com.doteyplay.game.constants.chat.ChatConstant;
import com.doteyplay.game.constants.chat.ChatUtils;
import com.doteyplay.game.service.bo.virtualworld.chat.patameter.DataPatameterObject;

/**
 * @className:MoneyGMPatameter.java
 * @classDescription:
 * @author:Tom.Zheng
 * @createTime:2014年7月10日 上午11:42:43
 */
public class DataTypeResolve implements IChatResolve<DataPatameterObject> {

	private DataPatameterObject object=null;
	
	private ChatConstant element;
	
	public DataTypeResolve(ChatConstant element) {
		super();
		this.element = element;
	}

	@Override
	public DataPatameterObject handlePatameter(String source) {
		// TODO Auto-generated method stub
		String data = ChatUtils.doGmPre(source, element.getSuffix());
		String[] split = data.split("\\]\\[");
		if(split.length != 2){
			throw new RuntimeException("gm操作的格式不对,暂不支持");
		}
		String tempRoleIdStr=split[0];
		String roleIdStr = tempRoleIdStr.substring(1, tempRoleIdStr.length());
		
		String tempGoodsStr = split[1];
		
		String goodsStr = tempGoodsStr.substring(0, tempGoodsStr.length()-1);
		
		String[] roleIds = roleIdStr.split(",");
		
		String[] goodsString = goodsStr.split(",");
		String num = goodsString[0];
		
		object = new DataPatameterObject();
		
		object.addRoles(roleIds);
		
		object.setNum(Integer.parseInt(num));
		return object;
	}
	
	

	
}
