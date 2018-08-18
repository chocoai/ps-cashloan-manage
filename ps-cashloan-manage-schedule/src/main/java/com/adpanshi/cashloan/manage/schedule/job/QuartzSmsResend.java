package com.adpanshi.cashloan.manage.schedule.job;

import com.adpanshi.cashloan.manage.cl.model.QuartzInfo;
import com.adpanshi.cashloan.manage.cl.model.QuartzLog;
import com.adpanshi.cashloan.manage.cl.model.Sms;
import com.adpanshi.cashloan.manage.cl.model.expand.SmsModel;
import com.adpanshi.cashloan.manage.cl.service.QuartzInfoService;
import com.adpanshi.cashloan.manage.cl.service.QuartzLogService;
import com.adpanshi.cashloan.manage.cl.service.SmsService;
import com.adpanshi.cashloan.manage.core.common.context.Global;
import com.adpanshi.cashloan.manage.core.common.util.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import tool.util.BeanUtil;
import tool.util.DateUtil;

import java.util.*;

/**
 * 短信重新发送
 *
 * @version 1.0.0
 *
 */
@Component
@Lazy(value = false)
public class QuartzSmsResend implements Job {
    private static final Logger logger = LoggerFactory.getLogger(QuartzSmsResend.class);
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        QuartzInfoService quartzInfoService = (QuartzInfoService) BeanUtil.getBean("quartzInfoService");
        QuartzLogService quartzLogService = (QuartzLogService) BeanUtil.getBean("quartzLogService");
        QuartzLog ql = new QuartzLog();
        QuartzInfo qiData = new QuartzInfo();
        QuartzInfo qi = quartzInfoService.findSelective("doSmsResend");
        if (null == qi) {
            logger.info(" [新定时任务][定时计算逾期] 启动失败:原因未启用! ");
            return;
        }
        try {
            int interval= Global.getInt("sms_quartz_interval");
            qiData.setId(qi.getId());
            ql.setQuartzId(qi.getId());
            Date time = DateUtil.getNow();
            ql.setStartTime(time);
            String remark = ResendSms(new Date(time.getTime() - (long)(interval*2  * 1000)),new Date(time.getTime() - (long)(interval  * 1000)));
//            String remark = ResendSms(new Date(118,5,1),new Date());
            ql.setTime(DateUtil.getNow().getTime() - ql.getStartTime().getTime());
            ql.setResult("10");
            ql.setRemark(remark);
            qiData.setSucceed(qi.getSucceed()+1);
        } catch (Exception e) {
            ql.setResult("20");
            qiData.setFail(qi.getFail()+1);
            logger.error(e.getMessage(), e);
        } finally {
            logger.info("保存定时任务日志");
            quartzLogService.save(ql);
            quartzInfoService.update(qiData);
        }
    }

    private String ResendSms(Date startTime, Date endTime) {
        String smsPlatform = Global.getValue("sms_platform");
        String smsPlatformReserve =Global.get("sms_platform_reserve");
        SmsService SmsService = (SmsService) BeanUtil.getBean("smsService");
        //查询时间段内未响应的短信
        Map<String,Object> map = new HashMap<>();
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        map.put("state", SmsModel.RESEND_STATE_INIT);
        List<Sms> smsList = SmsService.getResendSmsList(map);
        if(smsList.size()>0){//将未响应短信状态修改为失败状态
            List<Long> ids = new ArrayList<>();
            List<String> waitQuiryMsgids = new ArrayList<>();
            for(Sms sms : smsList){
                if(sms.getSmsPlatform().equals("tianyihong")){
//                if(sms.getSmsPlatform().equals("chuanglan")){
                    waitQuiryMsgids.add(sms.getMsgid());
                }else{
                    ids.add(sms.getId());
                }
            }
            if(ids.size()>0){
                Map<String,Object> clSmsInfo = new HashMap<>();
                clSmsInfo.put("resp","短信发送失败");
                clSmsInfo.put("respState",SmsModel.RESEND_STATE_FAIL);
                clSmsInfo.put("ids",ids);
                clSmsInfo.put("respTime",new Date());
                SmsService.updateByIds(clSmsInfo);
            }
            //查询天一泓短信响应状态
            if(waitQuiryMsgids!=null&&waitQuiryMsgids.size()>0){
                int count = waitQuiryMsgids.size() % 200 > 0 ? waitQuiryMsgids.size() / 200 + 1 : waitQuiryMsgids.size() / 200;
                List<String> successMsgids = new ArrayList<>();//发送成功的msgid集合
                List<String> failMsgids = new ArrayList<>();//发送失败的msgid集合
                //每有两百条执行一次
                for(int i = 0;i < count;i++){
                    String msgids = "";
                    int num = (i + 1) * 200 - waitQuiryMsgids.size() > 0 ? waitQuiryMsgids.size() : (i + 1) * 200;
                    for(int j = i * 200;j < num;j++){
                        msgids += waitQuiryMsgids.get(j)+",";
                    }
                    msgids = msgids.substring(0,msgids.lastIndexOf(","));
                    String account = Global.getValue("sms_tianyihong_account");
                    String password = Global.getValue("sms_tianyihong_password");
                    String url = Global.getValue("sms_tianyihong_url")+"/getreport?account="+account+"&password="+password+"&ids="+msgids;
//                    String url = Global.getValue("sms_tianyihong_url")+"/getreport?account="+account+"&password="+password+"&ids=171428,171431";
                    String result = HttpUtil.doGet(url);
                    try {
                        JSONObject object = JSONObject.parseObject(result);
                        if(object!=null&&object.getInteger("status") == 0) {
                            JSONArray arrays = (JSONArray) object.get("array");
                            for (int k = 0; k < arrays.size(); k++) {
                                JSONArray array = (JSONArray) arrays.get(i);
                                if ((Integer)array.get(3) == 0){//判断是否发送成功
                                    successMsgids.add((String.valueOf(array.get(0))));
                                }else {
                                    failMsgids.add((String.valueOf(array.get(0))));
                                }
                            }
                        }else{
                            String[] msgArray = msgids.split(",");
                            List<String> msgList = new ArrayList<>(Arrays.asList(msgArray));
                            failMsgids.addAll(msgList);
                        }
                    }catch(Exception e) {
                        logger.error("天一泓请求异常：" + e.getMessage());
                    }
                }
                //修改天一泓短信发送结果
                Map<String,Object> tyhSmsInfo = new HashMap<>();
                if(successMsgids.size() > 0){
                    tyhSmsInfo.put("resp","短信发送成功");
                    tyhSmsInfo.put("respState",SmsModel.RESEND_STATE_SUCCESS);
                    tyhSmsInfo.put("respTime",new Date());
                    tyhSmsInfo.put("msgids",successMsgids);
                    SmsService.updateByMsgids(tyhSmsInfo);
                }
                if(failMsgids.size() > 0){
                    tyhSmsInfo.put("resp","短信发送失败");
                    tyhSmsInfo.put("respState",SmsModel.RESEND_STATE_FAIL);
                    tyhSmsInfo.put("respTime",new Date());
                    tyhSmsInfo.put("msgids",failMsgids);
                    SmsService.updateByMsgids(tyhSmsInfo);
                }
            }
        }
        //查询时间段内发送失败的短信
        map.put("state",SmsModel.RESEND_STATE_FAIL);
        List<Sms> smsFailList = SmsService.getResendSmsList(map);
        int success = 0;
        int fail = 0;
        int total =smsFailList.size();
        if(total>0){//重发短信
            List<Long> resendIds = new ArrayList<>();//可重发的短信id集合
            List<Long> finalIds = new ArrayList<>();//无法再次发送的id集合
            for(Sms sms : smsFailList){
                if(SmsService.countDayTime(sms.getPhone(),null,sms.getSmsType())> 0){//可重发
                    resendIds.add(sms.getId());
                    if(sms.getSmsPlatform().equals(smsPlatform)){
                        sms.setSmsPlatform(smsPlatformReserve);
                    }else{
                        sms.setSmsPlatform(smsPlatform);
                    }
                    success += SmsService.sendMsg(sms);
                }else{
                    finalIds.add(sms.getId());
                }
            }
            fail += total-success;
            Map<String,Object> smsInfo = new HashMap<>();
            //将重发短信的状态改为响应结束状态
            if(resendIds.size()>0){
                smsInfo.put("resp","短信发送失败,已重发");
                smsInfo.put("respState",SmsModel.RESEND_STATE_FINAL);
                smsInfo.put("ids",resendIds);
                smsInfo.put("respTime",new Date());
                SmsService.updateByIds(smsInfo);
            }
            if(finalIds.size()>0){
                //修改不能重发短信的响应消息
                smsInfo.put("resp","短信发送失败且当日次数已到最大限制");
                smsInfo.put("respState","");
                smsInfo.put("ids",finalIds);
                smsInfo.put("respTime",new Date());
                SmsService.updateByIds(smsInfo);
            }
            return "执行总次数"+total+",成功"+success+"次,失败"+fail+"次";
        }else{
            return "未查询到失败短信，执行总次数0,成功0次,失败0次";
        }
    }
}
