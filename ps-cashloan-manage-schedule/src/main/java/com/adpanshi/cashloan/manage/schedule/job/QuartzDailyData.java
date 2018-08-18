package com.adpanshi.cashloan.manage.schedule.job;


import com.adpanshi.cashloan.manage.cl.model.QuartzInfo;
import com.adpanshi.cashloan.manage.cl.model.QuartzLog;
import com.adpanshi.cashloan.manage.cl.service.QuartzInfoService;
import com.adpanshi.cashloan.manage.cl.service.QuartzLogService;
import com.adpanshi.cashloan.manage.cl.service.RiskDataService;
import org.apache.log4j.Logger;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import tool.util.BeanUtil;
import tool.util.DateUtil;

/**
 * 每天凌晨定时更新前一天日报数据
 * Created by cc on 2017/8/2.
 */
@Component
@Lazy(value = false)
public class QuartzDailyData implements Job {

    private static final Logger logger = Logger.getLogger(QuartzDailyData.class);

    /**
     * 自动更新前一天日报数据
     *
     */
    public String dailyData() {
        RiskDataService riskDataService = (RiskDataService) BeanUtil.getBean("systemRcService");

        String quartzRemark = null;
        int succeed = 0;
        int fail = 0;
        int total = 0;
            try {
                riskDataService.saveDayData();
                succeed++;
                total++;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                fail++;
                total++;
            }
        quartzRemark = "执行总次数"+total+",成功"+succeed+"次,失败"+fail+"次";
        return quartzRemark;

    }

    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        QuartzInfoService quartzInfoService = (QuartzInfoService) BeanUtil.getBean("quartzInfoService");
        QuartzLogService quartzLogService = (QuartzLogService) BeanUtil.getBean("quartzLogService");
        // 查询当前任务信息
        QuartzInfo quartzInfo = quartzInfoService.findSelective("doDailyData");
        //@remarks:只有启用状态才会调用.@date:20170814 @author:nmnl
        if(null == quartzInfo){
            logger.info(" [定时任务][每日日报] 启动失败:原因未启用! ");
            return;
        }
        QuartzLog quartzLog = new QuartzLog();
        QuartzInfo qiData = new QuartzInfo();
        qiData.setId(quartzInfo.getId());
        quartzLog.setQuartzId(quartzInfo.getId());
        quartzLog.setStartTime(DateUtil.getNow());

        try {
            String remark = dailyData();
            quartzLog.setTime(DateUtil.getNow().getTime() - quartzLog.getStartTime().getTime());
            quartzLog.setResult("10");
            quartzLog.setRemark(remark);
            qiData.setSucceed(quartzInfo.getSucceed() + 1);
            logger.info("[定时任务][每日日报数据更新成功]["+DateUtil.getNowDate()+"]");
        } catch (Exception e) {
            quartzLog.setResult("20");
            qiData.setFail(quartzInfo.getFail() + 1);
            logger.error(e.getMessage(), e);
        } finally {
            logger.info("保存定时任务日志");
            quartzLogService.save(quartzLog);
            quartzInfoService.update(qiData);
        }

    }

}
