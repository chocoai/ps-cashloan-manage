package com.adpanshi.cashloan.manage.cl.service.impl;


import com.adpanshi.cashloan.manage.cl.domain.CreditsUpgrade;
import com.adpanshi.cashloan.manage.cl.domain.CreditsUpgradeLog;
import com.adpanshi.cashloan.manage.cl.mapper.*;
import com.adpanshi.cashloan.manage.cl.model.BorrowProgress;
import com.adpanshi.cashloan.manage.cl.model.BorrowRepay;
import com.adpanshi.cashloan.manage.cl.model.ManageBRepayLogModel;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowRepayLogModel;
import com.adpanshi.cashloan.manage.cl.service.CreditsUpgradeService;
import com.adpanshi.cashloan.manage.core.common.context.Global;
import com.adpanshi.cashloan.manage.core.common.util.DateUtil;
import com.adpanshi.cashloan.manage.core.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 8470
 * @version 1.0.1
 * @date 2018/08/22 15:44:11
 * @desc 用户提额ServiceImpl
 * Copyright 浙江盘石 All Rights Reserved
 */
@Service("creditsUpgradeService")
public class CreditsUpgradeServiceImpl implements CreditsUpgradeService {

	Logger logger = LoggerFactory.getLogger(CreditsUpgradeServiceImpl.class);
   
    @Resource
    private CreditsUpgradeMapper creditsUpgradeMapper;
    @Resource
    private BorrowRepayMapper borrowRepayMapper;
    @Resource
	private BorrowRepayLogMapper borrowRepayLogMapper;
    @Resource
	private BorrowProgressMapper borrowProgressMapper;
    @Resource
    private CreditsUpgradeLogMapper creditsUpgradeLogMapper;


	@Override
	public boolean isCanCreditUpgrade(Long userId) {
		//提额开关关闭的情况不操作用户的提额表(防止以后取消提额)
		if (!"on".equals(Global.getValue("credit_switch"))){
			logger.info("提额开关已关闭");
			return false;
		}
		//判断用户userId是否为空
		if (StringUtil.isEmpty(userId)) {
			logger.info("提额操作时用户userId为空异常");
			return false;
		}

		//初始用户的梯度
		int creditLevel = 0;
		//设置失效时间
		Date expiredTime = DateUtil.parse("2019-09-30 23:59:59",DateUtil.DATEFORMAT_STR_001);
		//判断当前用户是否符合提额的要求
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("userId", userId);
		//查询用户当前的梯度值
		CreditsUpgrade creditsUpgrade = creditsUpgradeMapper.findSelective(paramsMap);
		//1.判断用户是否借过款
		List<BorrowRepay> borrowRepayList = borrowRepayMapper.findRepayWithUser(userId);
		//获取用户当前的梯度
		if(creditsUpgrade!=null){
			creditLevel = creditsUpgrade.getCreditsLeve();
		}
		//借款次数为0的情况(这种情况走不到)
//		if (borrowRepayList==null && creditsUpgrade==null) {
//			//如果用户不存在借款行为，且提额表不存在的情况(只有这一种情况)
//			creditsUpgrade = new CreditsUpgrade();
//			creditsUpgrade.setUserId(userId);
//			creditsUpgrade.setCreditsLeve(0);
//			creditsUpgrade.setCredits(0d);
//			creditsUpgrade.setStatus(1);
//            creditsUpgrade.setSendStatus(1);
//			creditsUpgrade.setExpiredTime(expiredTime);
//			creditsUpgrade.setGmtCreaterTime(DateUtil.getNow());
//			creditsUpgradeMapper.save(creditsUpgrade);
//			logger.info("用户不存在借款且提额表不存在userId：" + userId + "==保存额度：0==梯度：0");
//			return false;
//		}
		//借款次数大于0的情况
		if (borrowRepayList.size()>0) {
			//如果用户借款次数为1次，且提额表不存在的情况(提额度为当前额度)
			if (borrowRepayList.size()==1 && creditsUpgrade==null) {
				creditsUpgrade = new CreditsUpgrade();
				creditsUpgrade.setCreditsLeve(1);
				creditsUpgrade.setUserId(userId);
				creditsUpgrade.setCredits(0d);
				creditsUpgrade.setStatus(1);
                creditsUpgrade.setSendStatus(1);
				creditsUpgrade.setExpiredTime(expiredTime);
				creditsUpgrade.setGmtCreaterTime(DateUtil.getNow());
				creditsUpgradeMapper.save(creditsUpgrade);
				//提额日志记录保存数据
                saveCreditUpgradeLog(creditsUpgrade);
				logger.info("用户借款一次且提额表不存在userId：" + userId + "==保存额度：0==梯度：1");
				return false;
			}
			//如果用户借款次数为1次，且提额表存在的情况(走不到)
			if (borrowRepayList.size()==1 && creditsUpgrade!=null) {
				creditsUpgrade.setCreditsLeve(1);
				creditsUpgrade.setUserId(userId);
				creditsUpgrade.setCredits(0d);
				creditsUpgrade.setGmtUpdateTime(DateUtil.getNow());
				creditsUpgradeMapper.update(creditsUpgrade);
				logger.info("用户借款一次且提额表存在userId：" + userId + "==更新额度：0==梯度：1");
				return false;
			}
			//判断用户借款次数为2次
			if (borrowRepayList.size()==2) {
				//设置逾期>3天次数
				int penaltyTimes = 0;
				//设置0<逾期<=3天次数
				int penaltyTime = 0;
				//设置刷单次数
				int brushTimes = 0;
				for (int i = 0; i < borrowRepayList.size(); i++) {
					int penaltyDay = borrowRepayList.get(i).getPenaltyDay();
					//查询用户真实还款时间还款时间
					BorrowRepayLogModel manageBRepayLogModel =  borrowRepayLogMapper.selectBorrowRepayLog(borrowRepayList.get(i).getBorrowId());
					//查询订单进度表获取放款时间
					paramsMap.clear();
					paramsMap.put("userId",manageBRepayLogModel.getUserId());
					paramsMap.put("borrowId",manageBRepayLogModel.getBorrowId());
					paramsMap.put("state","30");
					BorrowProgress borrowProgress = borrowProgressMapper.findSelective(paramsMap);
					Date createTime = borrowProgress.getCreateTime();
					Date repayTime = manageBRepayLogModel.getRepayTime();
					//计算还款时间间隔
					int subDays = DateUtil.daysBetween(createTime,repayTime);
					//当前订单时间间隔小于4天，认为是刷单
					if(manageBRepayLogModel.getTimeLimit().equals("7") && subDays < Global.getInt("time_limit_7_days")){
						brushTimes++;
					}

					//当前订单时间间隔小于八天，认为是刷单
					if(manageBRepayLogModel.getTimeLimit().equals("14") && subDays < Global.getInt("time_limit_14_days")){
						brushTimes++;
					}
					//判断用户是否存在逾期天数大于三天的借款
					if (penaltyDay > Global.getInt("penalty_day")) {
						penaltyTimes++;
					}
					//判断用户是否存在0<逾期天数<=3的借款
					if ((0 < penaltyDay) && (penaltyDay <= Global.getInt("penalty_day"))) {
						penaltyTime++;
					}

				}
				//用户刷单次数为0次的情况
				if(brushTimes==0){
					//考虑逾期次数为2次的情况
					if(penaltyTimes>=1 && creditsUpgrade!=null) {
						logger.info("用户借款2次逾期大于1次且提额表存在userId：" + userId + "==不更新额度");
						return false;
					}
					if(penaltyTimes>=1 && creditsUpgrade==null) {
						creditsUpgrade = new CreditsUpgrade();
						creditsUpgrade.setCreditsLeve(1);
						creditsUpgrade.setUserId(userId);
						creditsUpgrade.setCredits(0d);
						creditsUpgrade.setStatus(1);
                        creditsUpgrade.setSendStatus(1);
						creditsUpgrade.setExpiredTime(expiredTime);
						creditsUpgrade.setGmtCreaterTime(DateUtil.getNow());
						creditsUpgradeMapper.save(creditsUpgrade);
                        //提额日志记录保存数据
                        saveCreditUpgradeLog(creditsUpgrade);
						logger.info("用户借款2次逾期大于1次且提额表不存在userId：" + userId + "==保存额度：0==梯度：1");
						return false;
					}
					//如果用户逾期次数为1次，更新提额值为500卢比(用户梯度从1升到2)
					if(penaltyTime==1 && creditsUpgrade!=null) {
						creditsUpgrade.setCreditsLeve(2);
						creditsUpgrade.setUserId(userId);
						creditsUpgrade.setCredits(500d);
						creditsUpgrade.setGmtUpdateTime(DateUtil.getNow());
						creditsUpgradeMapper.update(creditsUpgrade);
						//提额日志记录保存数据
                        saveCreditUpgradeLog(creditsUpgrade);
						logger.info("用户借款2次逾期1次且提额表存在userId：" + userId + "==更新额度：500==梯度：2");
						return true;
					}
					//考虑到上线之前现有的用户(用户梯度从0升到2)
					if(penaltyTime==1 && creditsUpgrade==null) {
						creditsUpgrade = new CreditsUpgrade();
						creditsUpgrade.setCreditsLeve(2);
						creditsUpgrade.setUserId(userId);
						creditsUpgrade.setCredits(500d);
						creditsUpgrade.setStatus(1);
                        creditsUpgrade.setSendStatus(1);
						creditsUpgrade.setExpiredTime(expiredTime);
						creditsUpgrade.setGmtCreaterTime(DateUtil.getNow());
						creditsUpgradeMapper.save(creditsUpgrade);
						//提额日志记录保存数据
                        saveCreditUpgradeLog(creditsUpgrade);

						logger.info("用户借款2次逾期1次且提额表不存在userId：" + userId + "==保存额度：500==梯度：2");
						return true;
					}

					//如果用户逾期次数为0，更新提额值为1000卢比(用户梯度从1升到2)
					if(penaltyTimes==0 && creditsUpgrade!=null) {
						creditsUpgrade.setCreditsLeve(2);
						creditsUpgrade.setUserId(userId);
						creditsUpgrade.setCredits(1000d);
						creditsUpgrade.setGmtUpdateTime(DateUtil.getNow());
						creditsUpgradeMapper.update(creditsUpgrade);
						//提额日志记录保存数据
                        saveCreditUpgradeLog(creditsUpgrade);
						logger.info("用户借款2次逾期0次且提额表存在userId：" + userId + "==更新额度：1000==梯度：2");
						return true;
					}
					//考虑到上线之前现有的用户(用户梯度从0升到2)
					if(penaltyTimes==0 && creditsUpgrade==null) {
						creditsUpgrade = new CreditsUpgrade();
						creditsUpgrade.setCreditsLeve(2);
						creditsUpgrade.setUserId(userId);
						creditsUpgrade.setCredits(1000d);
						creditsUpgrade.setStatus(1);
                        creditsUpgrade.setSendStatus(1);
						creditsUpgrade.setExpiredTime(expiredTime);
						creditsUpgrade.setGmtCreaterTime(DateUtil.getNow());
						creditsUpgradeMapper.save(creditsUpgrade);
                        //提额日志记录保存数据
                        saveCreditUpgradeLog(creditsUpgrade);
						logger.info("用户借款2次逾期0次且提额表不存在userId：" + userId + "==保存额度：1000==梯度：2");
						return true;
					}
				}
				//用户刷单次数为1/2次的情况
				if(brushTimes>=1){
					if(creditsUpgrade!=null) {
						logger.info("用户借款2次刷单1次且提额表存在userId：" + userId + "==不更新额度");
						return false;
					}
					if(creditsUpgrade==null) {
						creditsUpgrade = new CreditsUpgrade();
						creditsUpgrade.setCreditsLeve(1);
						creditsUpgrade.setUserId(userId);
						creditsUpgrade.setCredits(0d);
						creditsUpgrade.setStatus(1);
						creditsUpgrade.setSendStatus(1);
						creditsUpgrade.setExpiredTime(expiredTime);
						creditsUpgrade.setGmtCreaterTime(DateUtil.getNow());
						creditsUpgradeMapper.save(creditsUpgrade);
                        //提额日志记录保存数据
                        saveCreditUpgradeLog(creditsUpgrade);
						logger.info("用户借款2次刷单1次且提额表不存在userId：" + userId + "==保存额度：0");
						return false;
					}
				}
			}

			//判断用户借款次数为3次(大于等于)
			if (borrowRepayList.size()>=3) {
				//设置逾期大于3天次数
				int penaltyTimes = 0;
				//设置逾期小于等于3天次数
				int penaltyTime = 0;
				//设置刷单次数
				int brushTimes = 0;
				//设置第1单逾期
				int firstTimes = 0;
				//设置第2单逾期
				int secondTimes = 0;
				//设置第3单逾期
				int thirdTimes = 0;
				//设置首单刷单次数
                int firstBrush = 0;
				//只判断用户最近三次的还款行为
				for (int i = 0; i < 3 ; i++) {
					//查询用户真实还款时间还款时间
					BorrowRepayLogModel manageBRepayLogModel = borrowRepayLogMapper.selectBorrowRepayLog(borrowRepayList.get(i).getBorrowId());
					//获取订单放款时间，订单还款时间
					paramsMap.clear();
					paramsMap.put("userId",manageBRepayLogModel.getUserId());
					paramsMap.put("borrowId",manageBRepayLogModel.getBorrowId());
					paramsMap.put("state","30");
					BorrowProgress borrowProgress = borrowProgressMapper.findSelective(paramsMap);
					Date createTime = borrowProgress.getCreateTime();
					Date repayTime = manageBRepayLogModel.getRepayTime();
					//计算还款时间间隔
					int subDays = DateUtil.daysBetween(createTime,repayTime);
					//当前订单时间间隔小于4天，认为是刷单
					if(manageBRepayLogModel.getTimeLimit().equals("7") && subDays < Global.getInt("time_limit_7_days")){
						brushTimes++;
					}

					//当前订单时间间隔小于8天，认为是刷单
					if(manageBRepayLogModel.getTimeLimit().equals("14") && subDays < Global.getInt("time_limit_14_days")){
						brushTimes++;
					}
					//判断用户是否存在逾期天数大于三天的借款
					int penaltyDay = borrowRepayList.get(i).getPenaltyDay();
					if (penaltyDay > Global.getInt("penalty_day")) {
						penaltyTimes++;
					}
					//判断用户是否存在第1单逾期的行为
					if(i==2 && penaltyDay > Global.getInt("penalty_day")){
						firstTimes++;
					}
                    //判断用户是否存在首单刷单的行为(借款7天的情况)
                    if(i==2 && (manageBRepayLogModel.getTimeLimit().equals("7") && subDays < Global.getInt("time_limit_7_days"))){
                        firstBrush++;
                    }
					//判断用户是否存在首单刷单的行为(借款14天的情况)
					if(i==2 && (manageBRepayLogModel.getTimeLimit().equals("14") && subDays < Global.getInt("time_limit_14_days"))){
						firstBrush++;
					}
					//判断用户是否存在第2单逾期的行为
					if(i==1 && penaltyDay > Global.getInt("penalty_day")){
						secondTimes++;
					}
					//判断用户是否存在第3单逾期的行为
					if(i==0 && penaltyDay > Global.getInt("penalty_day")){
						thirdTimes++;
					}
					//判断用户是否存在0<逾期天数<=3天的借款
					if ((penaltyDay > 0) && (penaltyDay <= Global.getInt("penalty_day"))) {
						penaltyTime++;
					}
				}

				//如果用户刷单次数为0次且梯度小于3的情况
				if(brushTimes==0 && creditLevel<3){
					//如果用户逾期次数>=2次且存在提额表的时候
					if(penaltyTimes>=2 && creditsUpgrade!=null) {
						logger.info("用户借款"+borrowRepayList.size()+"次逾期天数大于3天>=2且提额表存在userId：" + userId + "==不更新额度表");
						return false;
					}
					//如果用户逾期次数>=2次且不存在提额表的时候
					if(penaltyTimes>=2 && creditsUpgrade==null) {
						creditsUpgrade = new CreditsUpgrade();
						creditsUpgrade.setCreditsLeve(1);
						creditsUpgrade.setUserId(userId);
						creditsUpgrade.setCredits(0d);
                        creditsUpgrade.setStatus(1);
                        creditsUpgrade.setSendStatus(1);
						creditsUpgrade.setExpiredTime(expiredTime);
						creditsUpgrade.setGmtCreaterTime(DateUtil.getNow());
						creditsUpgradeMapper.save(creditsUpgrade);
                        //提额日志记录保存数据
                        saveCreditUpgradeLog(creditsUpgrade);
						logger.info("用户借款"+borrowRepayList.size()+"次逾期天数大于3天>=2且提额表不存在userId：" + userId + "==更新额度：0==梯度：1");
						return false;
					}
					//如果用户逾期次数为1次且是最近三次中间的逾期的情况(近期两次或者三次)
					if(penaltyTimes==1 && (thirdTimes==1 || secondTimes==1) && creditsUpgrade==null) {
						creditsUpgrade = new CreditsUpgrade();
						creditsUpgrade.setCreditsLeve(1);
						creditsUpgrade.setUserId(userId);
						creditsUpgrade.setCredits(0d);
                        creditsUpgrade.setStatus(1);
                        creditsUpgrade.setSendStatus(1);
						creditsUpgrade.setExpiredTime(expiredTime);
						creditsUpgrade.setGmtUpdateTime(DateUtil.getNow());
						creditsUpgradeMapper.save(creditsUpgrade);
                        //提额日志记录保存数据
                        saveCreditUpgradeLog(creditsUpgrade);
						logger.info("用户借款"+borrowRepayList.size()+"次逾期天数大于3天1次且提额表不存在userId：" + userId + "==更新额度：0==梯度：1");
						return false;
					}
					//如果用户逾期次数为1次且是最近三次中间的逾期的情况(近期两次或者三次)
					if(penaltyTimes==1 && (thirdTimes==1 ||secondTimes==1) && creditsUpgrade!=null) {
						logger.info("用户借款"+borrowRepayList.size()+"次逾期天数大于3天1次且提额表存在userId：" + userId + "==不更新额度");
						return false;
					}
					//如果用户逾期次数为1次且是最近三次中第一次的逾期的情况
					if(penaltyTimes==1 && firstTimes==1 && creditsUpgrade!=null) {
					    //如果用户的借款等级为2，不更新额度
					    if(creditLevel==2){
					        logger.info("用户借款"+borrowRepayList.size()+"次逾期1次且提额表存在userId：" + userId + "==不更新额度==梯度为2不更新");
                            return false;
                        }
                        //如果用户的借款等级为1且逾期小于三天借款为0，更新额度为2
                        if(creditLevel==1 && penaltyTime==0){
                            creditsUpgrade.setCreditsLeve(2);
                            creditsUpgrade.setUserId(userId);
                            creditsUpgrade.setCredits(1000d);
                            creditsUpgradeMapper.update(creditsUpgrade);
                            //提额日志记录保存数据
                            saveCreditUpgradeLog(creditsUpgrade);
                            logger.info("用户借款"+borrowRepayList.size()+"次逾期天数<=3天0次且提额表存在userId：" + userId + "==更新额度：1000==梯度1->2");
                            return true;
                        }
						if(creditLevel==1 && penaltyTime==1){
							creditsUpgrade.setCreditsLeve(2);
							creditsUpgrade.setUserId(userId);
							creditsUpgrade.setCredits(500d);
							creditsUpgradeMapper.update(creditsUpgrade);
							//提额日志记录保存数据
							saveCreditUpgradeLog(creditsUpgrade);
							logger.info("用户借款"+borrowRepayList.size()+"次逾期天数<=天1次且提额表存在userId：" + userId + "==更新额度：500==梯度1->2");
							return true;
						}
						if(creditLevel==1 && penaltyTime==2){
							logger.info("用户借款"+borrowRepayList.size()+"次逾期天数<=3天2次且提额表存在userId：" + userId + "==更新额度：0==梯度1->1");
							return false;
						}
					}
					//如果用户逾期次数为1次且是最近三次中第一次的逾期的情况(梯度从0上升到2)
					if(penaltyTimes==1 && firstTimes==1 && creditsUpgrade==null) {
						if(penaltyTime==0){
							creditsUpgrade = new CreditsUpgrade();
							creditsUpgrade.setCreditsLeve(2);
							creditsUpgrade.setUserId(userId);
							creditsUpgrade.setCredits(1000d);
							creditsUpgrade.setStatus(1);
							creditsUpgrade.setSendStatus(1);
							creditsUpgrade.setExpiredTime(expiredTime);
							creditsUpgrade.setGmtUpdateTime(DateUtil.getNow());
							creditsUpgradeMapper.save(creditsUpgrade);
							//提额日志记录保存数据
							saveCreditUpgradeLog(creditsUpgrade);
							logger.info("用户借款"+borrowRepayList.size()+"次逾期天数<=3天0次且提额表不存在userId：" + userId + "==更新额度：1000==梯度：2");
							return true;
						}
						if(penaltyTime==1){
							creditsUpgrade = new CreditsUpgrade();
							creditsUpgrade.setCreditsLeve(2);
							creditsUpgrade.setUserId(userId);
							creditsUpgrade.setCredits(500d);
							creditsUpgrade.setStatus(1);
							creditsUpgrade.setSendStatus(1);
							creditsUpgrade.setExpiredTime(expiredTime);
							creditsUpgrade.setGmtUpdateTime(DateUtil.getNow());
							creditsUpgradeMapper.save(creditsUpgrade);
							//提额日志记录保存数据
							saveCreditUpgradeLog(creditsUpgrade);
							logger.info("用户借款"+borrowRepayList.size()+"次逾期天数<=3天1次且提额表不存在userId：" + userId + "==更新额度：500==梯度：2");
							return true;
						}
						if(penaltyTime==2){
							creditsUpgrade = new CreditsUpgrade();
							creditsUpgrade.setCreditsLeve(1);
							creditsUpgrade.setUserId(userId);
							creditsUpgrade.setCredits(0d);
							creditsUpgrade.setStatus(1);
							creditsUpgrade.setSendStatus(1);
							creditsUpgrade.setExpiredTime(expiredTime);
							creditsUpgrade.setGmtUpdateTime(DateUtil.getNow());
							creditsUpgradeMapper.save(creditsUpgrade);
							//提额日志记录保存数据
							saveCreditUpgradeLog(creditsUpgrade);
							logger.info("用户借款"+borrowRepayList.size()+"次逾期天数<=3天2次且提额表不存在userId：" + userId + "==更新额度：0==梯度：1");
							return true;
						}
					}
					//如果用户逾期次数为0次
					if(penaltyTimes==0 && creditsUpgrade!=null) {
					    if(penaltyTime>=2){
                            logger.info("用户借款"+borrowRepayList.size()+"次逾期天数<=3天大于等于2次且提额表存在userId：" + userId + "==更新额度：0==梯度："+creditsUpgrade.getCreditsLeve());
                            return false;
                        }
                        if(penaltyTime==1){
                            creditsUpgrade.setCreditsLeve(creditsUpgrade.getCreditsLeve()+1);
                            creditsUpgrade.setUserId(userId);
                            creditsUpgrade.setCredits(Double.sum(creditsUpgrade.getCredits(),500d));
                            creditsUpgradeMapper.update(creditsUpgrade);
                            //提额日志记录保存数据,保存增加的记录值
                            creditsUpgrade.setCredits(500d);
                            saveCreditUpgradeLog(creditsUpgrade);
                            logger.info("用户借款"+borrowRepayList.size()+"次逾期天数<=3天1次且提额表存在userId：" + userId + "==更新额度：500==梯度："+creditsUpgrade.getCreditsLeve());
                            return true;
                        }
                        if(penaltyTime==0){
                            creditsUpgrade.setCreditsLeve(creditsUpgrade.getCreditsLeve()+1);
                            creditsUpgrade.setUserId(userId);
                            creditsUpgrade.setCredits(Double.sum(creditsUpgrade.getCredits(),1000d));
                            creditsUpgradeMapper.update(creditsUpgrade);
                            //提额日志记录保存数据,保存增加的记录值
                            creditsUpgrade.setCredits(1000d);
                            saveCreditUpgradeLog(creditsUpgrade);
                            logger.info("用户借款"+borrowRepayList.size()+"次逾期天数<=3天0次且提额表存在userId：" + userId + "==更新额度：1000==梯度："+creditsUpgrade.getCreditsLeve());
                            return true;
                        }
					}
					//如果用户逾期次数为0次(梯度从0上升到2只能)
					if(penaltyTimes==0 && creditsUpgrade==null) {
                        if(penaltyTime>=2){
                            creditsUpgrade = new CreditsUpgrade();
                            creditsUpgrade.setCreditsLeve(1);
                            creditsUpgrade.setUserId(userId);
                            creditsUpgrade.setCredits(0d);
                            creditsUpgrade.setStatus(1);
                            creditsUpgrade.setSendStatus(1);
                            creditsUpgrade.setExpiredTime(expiredTime);
                            creditsUpgrade.setGmtUpdateTime(DateUtil.getNow());
                            creditsUpgradeMapper.save(creditsUpgrade);
                            //提额日志记录保存数据
                            saveCreditUpgradeLog(creditsUpgrade);
                            logger.info("用户借款"+borrowRepayList.size()+"次逾期天数<=3天大于等于2次且提额表不存在userId：" + userId + "==更新额度：0==梯度：1");
                            return false;
                        }
                        if(penaltyTime==1){
                            creditsUpgrade = new CreditsUpgrade();
                            creditsUpgrade.setCreditsLeve(2);
                            creditsUpgrade.setUserId(userId);
                            creditsUpgrade.setCredits(500d);
                            creditsUpgrade.setStatus(1);
                            creditsUpgrade.setSendStatus(1);
                            creditsUpgrade.setExpiredTime(expiredTime);
                            creditsUpgrade.setGmtUpdateTime(DateUtil.getNow());
                            creditsUpgradeMapper.save(creditsUpgrade);
                            //提额日志记录保存数据
                            saveCreditUpgradeLog(creditsUpgrade);
                            logger.info("用户借款"+borrowRepayList.size()+"次逾期天数<=3天1次且提额表不存在userId：" + userId + "==更新额度：500==梯度：2");
                            return true;
                        }
                        if(penaltyTime==0){
                            creditsUpgrade = new CreditsUpgrade();
                            creditsUpgrade.setCreditsLeve(2);
                            creditsUpgrade.setUserId(userId);
                            creditsUpgrade.setCredits(1000d);
                            creditsUpgrade.setStatus(1);
                            creditsUpgrade.setSendStatus(1);
                            creditsUpgrade.setExpiredTime(expiredTime);
                            creditsUpgrade.setGmtUpdateTime(DateUtil.getNow());
                            creditsUpgradeMapper.save(creditsUpgrade);
                            //提额日志记录保存数据
                            saveCreditUpgradeLog(creditsUpgrade);
                            logger.info("用户借款"+borrowRepayList.size()+"次逾期天数<=3天0次且提额表不存在userId：" + userId + "==更新额度：1000==梯度：2");
                            return true;
                        }
					}
				}

				//如果用户存在刷单1次的情况
				if(brushTimes==1 && creditLevel<3){
					//如果用户逾期次数>=1次且存在提额表的时候
					if(penaltyTimes>=1 && creditsUpgrade!=null) {
						logger.info("刷单1次用户借款"+borrowRepayList.size()+"次逾期天数>3天次数>=1且提额表存在userId：" + userId + "==不更新额度");
						return false;
					}
					//如果用户逾期次数>=1次且不存在提额表的时候
					if(penaltyTimes>=1 && creditsUpgrade==null) {
						creditsUpgrade = new CreditsUpgrade();
						creditsUpgrade.setCreditsLeve(1);
						creditsUpgrade.setUserId(userId);
						creditsUpgrade.setCredits(0d);
                        creditsUpgrade.setStatus(1);
                        creditsUpgrade.setSendStatus(1);
						creditsUpgrade.setExpiredTime(expiredTime);
						creditsUpgrade.setGmtUpdateTime(DateUtil.getNow());
						creditsUpgradeMapper.save(creditsUpgrade);
                        //提额日志记录保存数据
                        saveCreditUpgradeLog(creditsUpgrade);
						logger.info("刷单1次用户借款"+borrowRepayList.size()+"次逾期天数>3天次数>=1且提额表不存在userId：" + userId + "==更新额度：0==梯度：1");
						return false;
					}
                    //如果用户逾期次数0次/首次刷单且存在提额表的时候
                    if(penaltyTimes==0 && firstBrush==1 && creditsUpgrade!=null) {
                        //如果用户的借款等级为2，不更新额度
                        if(creditLevel==2){
                            logger.info("刷单1次且首次刷单用户借款"+borrowRepayList.size()+"次逾期天数>3天0次梯度为2且提额表存在userId：" + userId + "==不更新额度==梯度为2不更新");
                            return false;
                        }
                        //如果用户的借款等级为1，更新额度为2
                        if(creditLevel==1 && penaltyTime==0){
                            creditsUpgrade.setCreditsLeve(2);
                            creditsUpgrade.setUserId(userId);
                            creditsUpgrade.setCredits(1000d);
                            creditsUpgradeMapper.update(creditsUpgrade);
                            //提额日志记录保存数据
                            saveCreditUpgradeLog(creditsUpgrade);
                            logger.info("刷单1次且首次刷单用户借款"+borrowRepayList.size()+"次逾期天数<=3天0次且提额表存在userId：" + userId + "==更新额度：1000==梯度1->2");
                            return true;
                        }
						if(creditLevel==1 && penaltyTime==1){
							creditsUpgrade.setCreditsLeve(2);
							creditsUpgrade.setUserId(userId);
							creditsUpgrade.setCredits(500d);
							creditsUpgradeMapper.update(creditsUpgrade);
							//提额日志记录保存数据
							saveCreditUpgradeLog(creditsUpgrade);
							logger.info("刷单1次且首次刷单用户借款"+borrowRepayList.size()+"次逾期天数<=3天1次且提额表存在userId：" + userId + "==更新额度：500==梯度1->2");
							return true;
						}
						if(creditLevel==1 && penaltyTime==2){
							logger.info("刷单1次且首次刷单用户借款"+borrowRepayList.size()+"次逾期天数<=3天2次且提额表存在userId：" + userId + "==更新额度：0==梯度1->1");
							return false;
						}
                    }
                    //如果用户逾期次数0次/首次刷单且不存在提额表的时候
                    if(penaltyTimes==0 && firstBrush==1 && creditsUpgrade==null) {
                    	if(penaltyTime==2){
							creditsUpgrade = new CreditsUpgrade();
							creditsUpgrade.setCreditsLeve(1);
							creditsUpgrade.setUserId(userId);
							creditsUpgrade.setCredits(0d);
							creditsUpgrade.setStatus(1);
							creditsUpgrade.setSendStatus(1);
							creditsUpgrade.setExpiredTime(expiredTime);
							creditsUpgrade.setGmtUpdateTime(DateUtil.getNow());
							creditsUpgradeMapper.save(creditsUpgrade);
							//提额日志记录保存数据
							saveCreditUpgradeLog(creditsUpgrade);
							logger.info("刷单1次且首次刷单用户借款"+borrowRepayList.size()+"次逾期天数<=3天2次且提额表不存在userId：" + userId + "==更新额度：0==梯度：1");
							return false;
						}
						if(penaltyTime==1){
							creditsUpgrade = new CreditsUpgrade();
							creditsUpgrade.setCreditsLeve(2);
							creditsUpgrade.setUserId(userId);
							creditsUpgrade.setCredits(500d);
							creditsUpgrade.setStatus(1);
							creditsUpgrade.setSendStatus(1);
							creditsUpgrade.setExpiredTime(expiredTime);
							creditsUpgrade.setGmtUpdateTime(DateUtil.getNow());
							creditsUpgradeMapper.save(creditsUpgrade);
							//提额日志记录保存数据
							saveCreditUpgradeLog(creditsUpgrade);
							logger.info("刷单1次且首次刷单用户借款"+borrowRepayList.size()+"次逾期天数<=3天1次且提额表不存在userId：" + userId + "==更新额度：500==梯度：2");
							return true;
						}
						if(penaltyTime==0){
							creditsUpgrade = new CreditsUpgrade();
							creditsUpgrade.setCreditsLeve(2);
							creditsUpgrade.setUserId(userId);
							creditsUpgrade.setCredits(1000d);
							creditsUpgrade.setStatus(1);
							creditsUpgrade.setSendStatus(1);
							creditsUpgrade.setExpiredTime(expiredTime);
							creditsUpgrade.setGmtUpdateTime(DateUtil.getNow());
							creditsUpgradeMapper.save(creditsUpgrade);
							//提额日志记录保存数据
							saveCreditUpgradeLog(creditsUpgrade);
							logger.info("刷单1次且首次刷单用户借款"+borrowRepayList.size()+"次逾期天数<=3天0次且提额表不存在userId：" + userId + "==更新额度：1000==梯度：2");
							return true;
						}
                    }
				}
				//如果用户存在刷单大于1次的情况
				if(brushTimes>1 && creditLevel<3){
					if(creditsUpgrade!=null) {
						logger.info("刷单大于1次用户借款"+borrowRepayList.size()+"次且提额表存在userId：" + userId + "==不更新额度");
						return false;
					}
					//考虑到线上可能存在这种情况
					if(creditsUpgrade==null) {
						creditsUpgrade = new CreditsUpgrade();
						creditsUpgrade.setCreditsLeve(1);
						creditsUpgrade.setUserId(userId);
						creditsUpgrade.setCredits(0d);
                        creditsUpgrade.setStatus(1);
                        creditsUpgrade.setSendStatus(1);
						creditsUpgrade.setExpiredTime(expiredTime);
						creditsUpgrade.setGmtUpdateTime(DateUtil.getNow());
						creditsUpgradeMapper.save(creditsUpgrade);
                        //提额日志记录保存数据
                        saveCreditUpgradeLog(creditsUpgrade);
						logger.info("刷单大于1次用户借款"+borrowRepayList.size()+"次且提额表不存在userId：" + userId + "==更新额度：0==梯度：1");
						return false;
					}
				}
			}
		}
		return false;
	}

	@Override
	public Double getCreditsByUserId(Long userId){
		Double tmpCredits=new Double(0);
		if(!"on".equals(Global.getValue("credit_switch"))) return tmpCredits;
		if(null==userId) return tmpCredits;
		List<CreditsUpgrade> creditsList= creditsUpgradeMapper.queryCreditsUpgradeByUserId(userId, 1);
		if(null==creditsList || creditsList.size()==0) return tmpCredits;
		//考虑到提额方法的不同，这里注释掉
		if(creditsList!=null){
			//这里的有效额度有且仅有一条
			return creditsList.get(0).getCredits();
		}
		return tmpCredits;
	}

    /**
     * 保存提额日志记录信息
     * @param creditsUpgrade
     */
	void saveCreditUpgradeLog(CreditsUpgrade creditsUpgrade){
        //提额日志记录保存数据
        CreditsUpgradeLog creditsUpgradeLog = new CreditsUpgradeLog();
        creditsUpgradeLog.setParentId(creditsUpgrade.getId());
        creditsUpgradeLog.setUserId(creditsUpgrade.getUserId());
        creditsUpgradeLog.setCreditsLeve(creditsUpgrade.getCreditsLeve());
        creditsUpgradeLog.setCredits(creditsUpgrade.getCredits());
        creditsUpgradeLogMapper.save(creditsUpgradeLog);
    }
}
