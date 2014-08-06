package com.doteyplay.game.service.bo.tollgate;

import com.doteyplay.core.bhns.ISimpleService;
import com.doteyplay.game.BOConst;
import com.doteyplay.game.message.tollgate.ShowTollgateDetailMessage;

/**
 * ������Ϣ����ӿ�
 * 
 * @author Tom.Zheng
 * 
 */
public interface ITollgateInfoService extends ISimpleService {

	public final static int PORTAL_ID = BOConst.BO_TOLLGATE;

	/**
	 * ��ʹ��������Ϣ.��ҵ�¼ʱ,��Ҫ��DB��,���ؿ���ص���Ϣ������,
	 * ���н���,��ֵ��ص�����.
	 * �������ǵ�һ�ε�¼,��Ҫ������һ����Ϣ,����DB.
	 * �ǵ�һ��,��ԭ��Ϣ��������.
	 */
	public void initlize();

	/**
	 * ���û�չʾ,���еĹؿ�����Ϣ���ؿ��ڲ��ڵ����Ϣ.
	 * ������Ϣ,�ڵ�¼ʱ,�ṩ��C.��C����������.
	 */
	public ShowTollgateDetailMessage showTollgateDetailInfo();
	/**
	 * ���͹ؿ�������Ϣ.
	 * ������ر� ĳһ�ؿ���ڵ�ʱ,����֪ͨC.
	 */
	public void sendNodeChangeInfo(int tollgateId,int nodeId);

	/**
	 * ����ڵ�ʱ,��Ҫ������֤.
	 * ��ʾ����ڵ�Ĵ���.
	 */
	public void enterBattle(int tollgateId,int nodeId,int groupId);
	
	/**
	 * ����ս���������Ϣ.
	 * @param tollgateId �ؿ�Id
	 * @param nodeId �ڵ�Id
	 * @param star ս�����.����.
	 */
	public void acceptBattleResult(int tollgateId,int nodeId,int star);
	
	/**
	 * �ͷ���Դ
	 */
	public void release();


}