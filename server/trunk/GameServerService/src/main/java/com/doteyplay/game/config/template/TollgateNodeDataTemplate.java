package com.doteyplay.game.config.template;

import com.doteyplay.game.util.SimpleReflectUtils;
import com.doteyplay.game.util.excel.ExcelCellBinding;
import com.doteyplay.game.util.excel.ExcelRowBinding;
import com.doteyplay.game.util.excel.TemplateConfigException;
import com.doteyplay.game.util.excel.TemplateObject;
/**
 * 
* @className:TollgateNodeDataTemplate.java
* @classDescription: 关卡节点数据
* @author:Tom.Zheng
* @createTime:2014年6月23日 下午3:51:30
 */
@ExcelRowBinding
public class TollgateNodeDataTemplate extends TemplateObject {

	
	/**
	 * 对应关卡id
	 */
	@ExcelCellBinding
	protected int tollgateGateId;

	/**
	 * 坐标
	 */
	@ExcelCellBinding
	protected String mapLoaction;
	/**
	 * 图标
	 */
	@ExcelCellBinding
	protected int mapShowIcon;

	/**
	 * 关卡操作类型
	 */
	@ExcelCellBinding
	protected int opreateType;
	/**
	 * 关卡Id/战役Id
	 */
	@ExcelCellBinding
	protected int opreateId;

	@Override
	public void check() throws TemplateConfigException {
		// TODO Auto-generated method stub

	}

	

	public int getTollgateGateId() {
		return tollgateGateId;
	}



	public void setTollgateGateId(int tollgateGateId) {
		this.tollgateGateId = tollgateGateId;
	}



	public String getMapLoaction() {
		return mapLoaction;
	}

	public void setMapLoaction(String mapLoaction) {
		this.mapLoaction = mapLoaction;
	}

	public int getMapShowIcon() {
		return mapShowIcon;
	}

	public void setMapShowIcon(int mapShowIcon) {
		this.mapShowIcon = mapShowIcon;
	}

	public int getOpreateType() {
		return opreateType;
	}

	public void setOpreateType(int opreateType) {
		this.opreateType = opreateType;
	}

	public int getOpreateId() {
		return opreateId;
	}

	public void setOpreateId(int opreateId) {
		this.opreateId = opreateId;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString()+SimpleReflectUtils.reflect(this);
	}

}
