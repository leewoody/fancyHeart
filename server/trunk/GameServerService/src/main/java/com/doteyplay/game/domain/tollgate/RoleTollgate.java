package com.doteyplay.game.domain.tollgate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.doteyplay.core.dbcs.DBCS;
import com.doteyplay.game.domain.gamebean.TollgateBean;
import com.doteyplay.game.message.tollgate.ShowTollgateDetailMessage;
import com.doteyplay.game.persistence.serverdata.tollgate.ITollgateBeanDao;

/**
 * @className:RoleTollgate.java
 * @classDescription:针对数据库的Bean
 * @author:Tom.Zheng
 * @createTime:2014年6月23日 下午4:24:13
 */
public class RoleTollgate {

	private static final Logger logger = Logger
			.getLogger(RoleTollgate.class.getName());
	
	private long roleId;
	
	private TollgateBean tollgateBean;
	
	private RoleTollgateManager manager;
	/**
	 * 无参数构造函数。
	 * @author:Tom.Zheng
	 * @createTime:2014年6月24日 下午3:36:23
	 */
	public RoleTollgate(long roleId) {
		super();
		this.roleId = roleId;
		// TODO Auto-generated constructor stub
	}
	/**
	 * 初始化空间。
	 */
	public void initlize(){
		
		manager = new RoleTollgateManager(roleId);
		
		ITollgateBeanDao roleInstanceBeanDao = DBCS.getExector(ITollgateBeanDao.class);

		tollgateBean = roleInstanceBeanDao.selectTollgateBean(roleId);
		if (tollgateBean == null) {
			tollgateBean = new TollgateBean();
			tollgateBean.setRoleId(roleId);
			addFirstGateData();//添加第一关，第一个结点。作为起点。
			buildData();//构建数据，方便存库。
			roleInstanceBeanDao.insertTollgateBean(tollgateBean);
		} else {
			//已经有的数据。
			readByteInfo();
		}

	}
	/**
	 * 数据的起点。第一个副本的数据开启。
	 */
	public void addFirstGateData(){
		manager.openTollgateAndNode(100,10001);
	}
	/**
	 * 查询数据后，解析数据的操作。
	 */
	public  void readByteInfo() {
		if (tollgateBean.getTollgateInfo() == null) {
			return;
		}
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(
				tollgateBean.getTollgateInfo()));

		try {
			// 数据版本
			int ver = in.readByte();
			manager.parseData(in, ver);

			in.close();
		} catch (Exception e) {
			logger.error("【" + this.getRoleId() + "】副本数据解析错误");
		}
	}
	/**
	 * 构建数据，存入DB之前，必须做的操作。
	 */
	public void buildData() {
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteArrayOutputStream);

		try {
			// 数据版本
			out.writeByte((byte) 1);

			
			manager.buildData(out);

			out.flush();
			out.close();
			tollgateBean.setTollgateInfo(byteArrayOutputStream.toByteArray());//赋值，主要是为了DB存入。
		} catch (Exception e) {
			logger.error(e.getMessage()+"【" + this.getRoleId() + "】副本数据解析错误");
		}
	}
	
	public void addTollGateById(int tollGateId){
//		manager.addFirstTollgateNode(tollGateId, nodeId);
	}

	

	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	public TollgateBean getTollgateBean() {
		return tollgateBean;
	}
	public void setTollgateBean(TollgateBean tollgateBean) {
		this.tollgateBean = tollgateBean;
	}
	public void release() {
		// TODO Auto-generated method stub
		if(this.manager!=null){
			this.manager.release();
			this.manager = null;
		}
	}
	/**
	 * 打开一个关卡或者节点.同时更新DB
	 * @param tollgateId
	 * @param nodeId
	 */
	public void openTollgateOrNodeAndUpdateDB(int tollgateId, int nodeId) {
		// TODO Auto-generated method stub
		manager.openTollgateAndNode(tollgateId, nodeId);
		updateDB();
	}
	public boolean isOpenTollgateAndNode(int tollgateId, int nodeId) {
		// TODO Auto-generated method stub
		return manager.isOpenTollgateAndNode(tollgateId, nodeId);
	}
	public boolean isOpenTollgate(int tollgateId) {
		// TODO Auto-generated method stub
		return manager.isOpenTollgate(tollgateId);
	}
	public void showTollgateDetailInfo(ShowTollgateDetailMessage message) {
		// TODO Auto-generated method stub
		manager.showTollgateDetailInfo(message);
	}
	public byte acceptBattleResult(int tollgateId, int nodeId, int star) {
		// TODO Auto-generated method stub
		byte isUpdate = manager.acceptBattleResult(tollgateId,nodeId,star);
		 
		if(isUpdate==1){
			updateDB();
		}
		return isUpdate;
	}
	/**
	 * 更新数据库
	 */
	public void updateDB(){
		ITollgateBeanDao roleInstanceBeanDao = DBCS
				.getExector(ITollgateBeanDao.class);
		buildData();
		roleInstanceBeanDao.updateTollgateBean(tollgateBean);

	}
	/**
	 * 返回所有的已经开放的关卡.
	 */
	public Set<Integer> getAllOpenTollgate() {
		// TODO Auto-generated method stub
		
		Map<Integer, TollgateInfo> map = manager.getMap();
		
		return map.keySet();
		
	}

}
