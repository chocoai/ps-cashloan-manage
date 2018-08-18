package com.adpanshi.cashloan.manage.schedule.job;

import com.adpanshi.cashloan.common.exception.ServiceException;
import com.adpanshi.cashloan.manage.cl.model.Borrow;
import com.adpanshi.cashloan.manage.cl.model.QuartzInfo;
import com.adpanshi.cashloan.manage.cl.model.QuartzLog;
import com.adpanshi.cashloan.manage.cl.pojo.OverDueUser;
import com.adpanshi.cashloan.manage.cl.service.*;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import tool.util.BeanUtil;
import tool.util.DateUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Lazy(value = false)
public class QuartzLateSms implements Job{
	
	private static final Logger logger = LoggerFactory.getLogger(QuartzLateSms.class);
	
	/**
	 * 定时计算逾期
	 * @throws ServiceException 
	 */
	public String lateSms() throws ServiceException {
		BorrowRepayService borrowRepayService = (BorrowRepayService)BeanUtil.getBean("borrowRepayService");
		SmsService clSmsService = (SmsService)BeanUtil.getBean("smsService");
		NoticesService clNoticesService = (NoticesService)BeanUtil.getBean("noticesService");
		BorrowService clBorrowService = (BorrowService) BeanUtil.getBean("borrowService");
		logger.info("进入逾期短信统计...");
		String quartzRemark = null;
		int succeed = 0;
		int fail = 0;
		int total = 0;
		Map<String,Object> paramMap = new HashMap<>();
		//计算第一天逾期未还款用户进行短信通知
//		paramMap.put("state",BorrowRepayModel.STATE_REPAY_NO);
//		paramMap.put("penaltyDay","1");
//		List<BorrowRepay> list = borrowRepayService.listSelective(paramMap);//第一天逾期用户

		//by cc 2017-09-22  对所有逾期用户都做短信提醒
//		TaskFactory factory = new TaskFactory();
//		Task task = factory.getTask("overdueTask");
		List<OverDueUser> list = borrowRepayService.listOverDueUser();
		if (!list.isEmpty()) {
			for (OverDueUser aList : list) {
				try {
					logger.info("[定时任务][逾期用户短信提醒:userId:{};borrow:{}]",aList.getUserId(),aList.getBorrowId());
					Borrow borrow = clBorrowService.getById(aList.getBorrowId());
//					String resultJson = task.sendSMS(borrow.getOrderNo());
//					clSmsService.chuanglanSms(resultJson,"overdue");
					//逾期第一天发送短信通知
					clSmsService.overdue(aList.getBorrowId(), aList.getUserId());
					//逾期第一天发送消息通知
					clNoticesService.overdue(aList.getBorrowId(), aList.getUserId());
					succeed++;
					total++;
				} catch (Exception e) {
					fail++;
					total++;
					logger.error(e.getMessage(), e);
				}
			}
		}
			
		logger.info("逾期短信计算结束...");
		quartzRemark = "执行总次数"+total+",成功"+succeed+"次,失败"+fail+"次";
		return quartzRemark;
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		QuartzInfoService quartzInfoService = (QuartzInfoService)BeanUtil.getBean("quartzInfoService");
		QuartzLogService quartzLogService = (QuartzLogService)BeanUtil.getBean("quartzLogService");
		QuartzLog ql = new QuartzLog();
		QuartzInfo qiData = new QuartzInfo();
		QuartzInfo qi = quartzInfoService.findSelective("doLateSms");
		//@remarks:只有启用状态才会调用.@date:20170814 @author:nmnl
		if(null == qi){
			logger.info(" [定时任务][定时计算逾期用户发送短信] 启动失败:原因未启用! ");
			return;
		}
		try {
			qiData.setId(qi.getId());
			ql.setQuartzId(qi.getId());
			ql.setStartTime(DateUtil.getNow());
			String remark = lateSms();
			ql.setTime(DateUtil.getNow().getTime()-ql.getStartTime().getTime());
			ql.setResult("10");
			ql.setRemark(remark);
			qiData.setSucceed(qi.getSucceed()+1);
		}catch (Exception e) {
			ql.setResult("20");
			qiData.setFail(qi.getFail()+1);
			logger.error(e.getMessage(),e);
		}finally{
			logger.info("保存定时任务日志");
			quartzLogService.save(ql);
			quartzInfoService.update(qiData);
		}
	}
	
}