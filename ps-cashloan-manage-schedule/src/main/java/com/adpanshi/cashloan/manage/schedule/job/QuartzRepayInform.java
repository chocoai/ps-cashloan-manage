package com.adpanshi.cashloan.manage.schedule.job;


import com.adpanshi.cashloan.common.exception.ServiceException;
import com.adpanshi.cashloan.manage.cl.model.Borrow;
import com.adpanshi.cashloan.manage.cl.model.BorrowRepay;
import com.adpanshi.cashloan.manage.cl.model.QuartzInfo;
import com.adpanshi.cashloan.manage.cl.model.QuartzLog;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowRepayModel;
import com.adpanshi.cashloan.manage.cl.service.*;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import tool.util.BeanUtil;
import tool.util.DateUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Lazy(value = false)
public class QuartzRepayInform implements Job{
	
	private static final Logger logger = Logger.getLogger(QuartzRepayInform.class);
	
	/**
	 * 还款提醒
	 * @throws ServiceException 
	 */
	public String repayInform() throws ServiceException {
		BorrowRepayService borrowRepayService = (BorrowRepayService)BeanUtil.getBean("borrowRepayService");
		SmsService clSmsService = (SmsService)BeanUtil.getBean("smsService");
		NoticesService clNoticesService = (NoticesService)BeanUtil.getBean("noticesService");
		BorrowService clBorrowService = (BorrowService) BeanUtil.getBean("norrowService");
		logger.info("进入还款日前1~3天计算...");
		String quartzRemark = null;
		int succeed = 0;
		int fail = 0;
		int total = 0;
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("state", BorrowRepayModel.STATE_REPAY_NO);
		List<BorrowRepay> list = borrowRepayService.listSelective(paramMap);
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				try {
					long day = DateUtil.daysBetween(new Date(), list.get(i).getRepayTime());
					if (day==2||day==1||day==0) {//还款前发送短信通知
						clSmsService.repayBefore(list.get(i).getUserId(), list.get(i).getBorrowId());
						//发消息通知
						clNoticesService.repayBefore(list.get(i).getUserId(), list.get(i).getBorrowId());
						Borrow borrow = clBorrowService.getById(list.get(i).getBorrowId());
					}
					succeed++;
					total++;
				} catch (Exception e) {
					fail ++;
					total++;
					logger.error(e.getMessage(),e);
				}
			}
		}
		logger.info("还款日前1~3天计算结束");
		quartzRemark = "执行总次数"+total+",成功"+succeed+"次,失败"+fail+"次";
		return quartzRemark;
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		QuartzInfoService quartzInfoService = (QuartzInfoService)BeanUtil.getBean("quartzInfoService");
		QuartzLogService quartzLogService = (QuartzLogService)BeanUtil.getBean("quartzLogService");
		QuartzLog ql = new QuartzLog();
		QuartzInfo qiData = new QuartzInfo();
		QuartzInfo qi = quartzInfoService.findSelective("doRepayInform");
		//@remarks:只有启用状态才会调用.@date:20170814 @author:nmnl
		if(null == qi){
			logger.info(" [定时任务][还款提醒] 启动失败:原因未启用! ");
			return;
		}
		try {
			qiData.setId(qi.getId());
			ql.setQuartzId(qi.getId());
			ql.setStartTime(DateUtil.getNow());
			
			String remark = repayInform();
			
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