package com.doteyplay.game.service.bo.tollgate;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.doteyplay.core.bhns.AbstractSimpleService;
import com.doteyplay.game.MessageCommands;
import com.doteyplay.game.config.template.BattleDataTemplate;
import com.doteyplay.game.config.template.TollgateDataManager;
import com.doteyplay.game.config.template.TollgateNodeDataTemplate;
import com.doteyplay.game.constants.common.CommonResponseType;
import com.doteyplay.game.constants.common.RewardType;
import com.doteyplay.game.constants.tollgate.TollgateErrorType;
import com.doteyplay.game.constants.tollgate.TollgateRewardExp;
import com.doteyplay.game.domain.pet.Pet;
import com.doteyplay.game.domain.role.Role;
import com.doteyplay.game.domain.tollgate.BattleResult;
import com.doteyplay.game.domain.tollgate.RoleTollgate;
import com.doteyplay.game.message.proto.CommonRespProBuf.PCommonResp;
import com.doteyplay.game.message.tollgate.BattleResultMessage;
import com.doteyplay.game.message.tollgate.NodeChangeMessage;
import com.doteyplay.game.message.tollgate.ShowTollgateDetailMessage;
import com.doteyplay.game.message.tollgate.TollgateChangeMessage;
import com.doteyplay.game.message.utils.ResponseMessageUtils;
import com.doteyplay.game.service.runtime.GlobalRoleCache;
import com.doteyplay.game.util.excel.TemplateService;


/**
 * 
 * @className:TollgateInfoService.java
 * @classDescription: 关卡服务类.
 * 
 * @author:Tom.Zheng
 * @createTime:2014年7月16日 下午3:29:31
 */
public class TollgateInfoService extends
		AbstractSimpleService<ITollgateInfoService> implements
		ITollgateInfoService {
	private static Logger logger = Logger.getLogger(TollgateInfoService.class);

	private RoleTollgate roleTollgate = null;

	@Override
	public int getPortalId() {
		// TODO Auto-generated method stub
		return PORTAL_ID;
	}

	@Override
	public void initlize() {
		roleTollgate = new RoleTollgate(this.getServiceId());

		roleTollgate.initlize();

		logger.error("角色的副本数据加载成功！roleId ="+this.getServiceId());

	}

	/**
	 * 向用户发送所有的关卡信息.
	 */
	@Override
	public ShowTollgateDetailMessage showTollgateDetailInfo() {
		// TODO Auto-generated method stub
		ShowTollgateDetailMessage message = new ShowTollgateDetailMessage();

		roleTollgate.showTollgateDetailInfo(message);

		return message;
	}

	/**
	 * 发送开启或关闭某个节点的信息. 附带着,另一节点的星级改变信息.
	 */
	@Override
	public void sendNodeChangeInfo(int tollgateId, int nodeId) {
		NodeChangeMessage message = new NodeChangeMessage();
		message.setTollgateId(tollgateId);
		message.addUpdateItem(nodeId, 1, 3, 0);

		this.sendMessage(message);

	}

	private void sendTollgateChangeMessage(int tollgateId) {
		TollgateChangeMessage message = new TollgateChangeMessage();

		message.addOperateTollgate(tollgateId, true);

		this.sendMessage(message);
	}

	@Override
	public void enterBattle(int tollgateId, int nodeId, int groupId) {
		// TODO Auto-generated method stub
		// 进入关卡或者战斗事件.
		
		// 1.检查已经激活.2.如果结点是用于进入战场,回复成功.3.如果结点用于进入其它关卡,帮助其激活其它关卡,并返回正常.
		boolean isOpenTollgateAndNode = roleTollgate.isOpenTollgateAndNode(
				tollgateId, nodeId);

		if (!isOpenTollgateAndNode) {
			// 发送错误信息.
			Role role = getRole();
			// 未激活该节点。

			ResponseMessageUtils.sendResponseMessage(
					MessageCommands.ENTER_BATTLE_MESSAGE.ordinal(),
					TollgateErrorType.NoNode.ordinal(), role);
		} else {
			//
			Role role = getRole();
			role.setPetGroupId(groupId);
			ResponseMessageUtils.sendResponseMessage(
					MessageCommands.ENTER_BATTLE_MESSAGE.ordinal(),
					TollgateErrorType.Success.ordinal(), role);

			// 发送成功的消息
		}
	}

	/**
	 * 接收战斗结果
	 * 
	 * @param star
	 */
	@Override
	public void acceptBattleResult(int tollgateId, int nodeId, int star) {
		// 根据战斗结果,给玩家添加新的经验.
		boolean openTollgateAndNode = roleTollgate.isOpenTollgateAndNode(
				tollgateId, nodeId);

		if (!openTollgateAndNode) {
			return;
		}
		if (star < 0 || star > 3) {
			return;
		}

		roleTollgate.acceptBattleResult(tollgateId, nodeId, star);

		addBattleResultReward(tollgateId, nodeId, star);
		// 开启下一关.按顺序来开启.
		
		openNextTollgate(tollgateId, nodeId, star);

	}

	private void openNextTollgate(int tollgateId, int nodeId, int star) {
		Set<Integer> allOpenTollgateIds = roleTollgate.getAllOpenTollgate();
		TollgateNodeDataTemplate nextNodeData = TollgateDataManager
				.getInstance().getNextNodeData(tollgateId, nodeId,
						allOpenTollgateIds);

		if (nextNodeData == null) {
			int nextTollgate = TollgateDataManager.getInstance()
					.getNextTollgate(tollgateId, allOpenTollgateIds);

			// 解锁新的关卡.并发送给客户端.
			boolean openTollgate = roleTollgate
					.isOpenTollgate(nextTollgate);
			if (!openTollgate) {
				int firstNodeId = TollgateDataManager.getInstance()
						.getFirstNodeId(nextTollgate);
				roleTollgate.openTollgateOrNodeAndUpdateDB(nextTollgate,
						firstNodeId);
				sendTollgateChangeMessage(tollgateId);

			}

		} else {

			boolean isOpen = roleTollgate.isOpenTollgateAndNode(
					nextNodeData.getTollgateGateId(), nextNodeData.getId());
			if (isOpen) {
				// 下一关已经开启,就不用管了.
				NodeChangeMessage message = new NodeChangeMessage();
				message.setTollgateId(tollgateId);
				message.addUpdateItem(nodeId, 3, star, 2);//历史节点是更新.
				this.sendMessage(message);
			} else {
				// 开启下一关.
				roleTollgate.openTollgateOrNodeAndUpdateDB(
						nextNodeData.getTollgateGateId(),
						nextNodeData.getId());
				NodeChangeMessage message = new NodeChangeMessage();
				message.setTollgateId(tollgateId);
				message.addUpdateItem(nextNodeData.getId(), 3, 0, 1);//当前节点.是添加.
				message.addUpdateItem(nodeId, 3, star, 2);//历史节点是更新.

				this.sendMessage(message);
			
			}

		}
	}

	/**
	 * 由指定的关卡信息,找到指定的奖励信息,进行添加战队奖励.添加每一个宠物的奖励.
	 * 
	 * @param tollgateId
	 * @param nodeId
	 * @param star
	 * @param petIds
	 */
	private void addBattleResultReward(int tollgateId, int nodeId, int star) {

		TollgateNodeDataTemplate nextNodeData = TollgateDataManager
				.getInstance().getTollgateData(tollgateId, nodeId);
		if (nextNodeData == null) {
			return;
		}
		int opreateType = nextNodeData.getOpreateType();

		if (opreateType == 0) {
			return;
		}
		int battleId = nextNodeData.getOpreateId();

		Map<Integer, BattleDataTemplate> all = TemplateService.getInstance()
				.getAll(BattleDataTemplate.class);

		if (!all.containsKey(battleId)) {
			return;
		}
		BattleDataTemplate temp = all.get(battleId);

		int petExp = temp.getPetExp();// 战队经验

		int gameCoin = temp.getGameCoin();// 角色金币

		int dropGroupId = temp.getDropGroupId();
		BattleResult result = new BattleResult();
		result.gameCoin = gameCoin;
		result.star = star;
		recordRoleHistory(result, false);
		recordPetCurrent(result, false);
		addRoleExp(tollgateId);
		addPetExp(petExp,getRole());
		addGameCoin(gameCoin,getRole());
		addDropGroupId(dropGroupId);
		recordRoleHistory(result, true);
		recordPetCurrent(result, true);
		showBattleResultMsg(result);

	}

	private void recordRoleHistory(BattleResult result, boolean isNew) {
		Role role = GlobalRoleCache.getInstance().getRoleById(getServiceId());
		if (isNew) {
			result.battleRoleResult.recordNewRole(
					role.getRoleBean().getLevel(), role.getRoleBean().getExp());
		} else {
			result.battleRoleResult.recordOldRole(
					role.getRoleBean().getLevel(), role.getRoleBean().getExp());

		}

	}

	private void recordPetCurrent(BattleResult result, boolean isNew) {
		Role role = GlobalRoleCache.getInstance().getRoleById(getServiceId());
		List<Pet> curPetList = role.getPetManager().getCurPetList();
		for (Pet pet : curPetList) {
			if (isNew) {
				result.recordNewPet(pet.getId(), pet.getBean().getLevel(), pet
						.getBean().getExp());
			} else {
				result.recordOldPet(pet.getId(), pet.getBean().getLevel(), pet
						.getBean().getExp());
			}
		}

	}

	private void showBattleResultMsg(BattleResult result) {
		BattleResultMessage message = new BattleResultMessage();
		message.setBattleResult(result);
		this.sendMessage(message);
	}

	private void addRoleExp(int tollgateId) {

		int tollgateShowType = TollgateDataManager.getInstance()
				.getTollgateShowType(tollgateId);

		int costEnergyPoint = TollgateRewardExp
				.getCostEnergyPoint(tollgateShowType);

		int roleExp = TollgateRewardExp
				.rewardRoleExpByEnergyPoint(costEnergyPoint);

		if (roleExp < 0) {
			throw new RuntimeException("副本奖励经验不能为负数");
		}

		Role role = GlobalRoleCache.getInstance().getRoleById(getServiceId());
		// 原则是先扣体力,再加经验.由于早期没有经历.
		role.addExp(roleExp);
		// 扣体力.
		if (role.addEnergy(-costEnergyPoint, RewardType.BATTLE, true)) {
			// 加经验
		}

	}
	/**
	 * 添加宠物的经验.
	 * @param petExp
	 */
	private void addPetExp(int petExp,Role role) {
		role.getPetManager().addCurPetListExp(petExp);

	}
	/**
	 * 添加游戏币.
	 * @param gameCoin
	 */
	private void addGameCoin(int gameCoin,Role role) {

		if (gameCoin < 0) {
			throw new RuntimeException("副本奖励游戏币不能为负数");
		}
		// 加钱
		role.addMoney(gameCoin, RewardType.BATTLE, true);

	}
	/**
	 * 添加掉落组物品
	 * @param dropGroupId
	 */
	private void addDropGroupId(int dropGroupId) {

	}

	private Role getRole() {
		Role role = GlobalRoleCache.getInstance().getRoleById(getServiceId());
		return role;
	}
	/**
	 * 根据结点的原型数据,决定节点是直接进入战斗,还是直接进入其它副本.
	 * 此操作,可以不经过服务器.由客户端负责操作.
	 * @param tollgateId
	 * @param sourceNodeId
	 */
	public void enterNode(int tollgateId,int sourceNodeId){
		// 查找原型数据,从中取出要进战斗,还是进入其它关卡.
				TollgateNodeDataTemplate tollgateData = TollgateDataManager
						.getInstance().getTollgateData(tollgateId, sourceNodeId);

				int opreateType = tollgateData.getOpreateType();

				int opreateId = tollgateData.getOpreateId();
				// 如果进入其它关卡.
				if (opreateType == 0) {
					// 根据opreateId 发送给client 具体的关卡内容.
					// 检查关卡内容是否已经激活,如果没有激活,发给它.
					if (!roleTollgate.isOpenTollgate(opreateId)) {
						int nodeId = TollgateDataManager.getInstance().getFirstNodeId(
								opreateId);
						roleTollgate.openTollgateOrNodeAndUpdateDB(opreateId, nodeId);
						// 向客户端发送新开启的副本的数组.
						sendTollgateChangeMessage(opreateId);
					} else {

					}

				}

				if (opreateType == 1) {
					// 发送客户端,开启一个战场.
				}
	}
	

	public void release() {
		if (roleTollgate != null) {
			roleTollgate.release();
			roleTollgate = null;
		}
	}

}
