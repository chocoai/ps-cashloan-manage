package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.jms.task.domain.JmsActiveTaskDomain;
import com.adpanshi.cashloan.manage.cl.mapper.*;
import com.adpanshi.cashloan.manage.cl.model.*;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowMainModel;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowRepayModel;
import com.adpanshi.cashloan.manage.cl.pojo.rocketmq.SmsApi;
import com.adpanshi.cashloan.manage.cl.service.SmsService;
import com.adpanshi.cashloan.manage.core.common.context.Global;
import com.adpanshi.cashloan.manage.core.common.util.DateUtil;
import com.adpanshi.cashloan.manage.core.common.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 短信serviceImpl
 * @date 2018/8/6 23:02
 */
@Service("smsService")
public class SmsServiceImpl implements SmsService{
    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);
    @Resource
    private SmsMapper smsMapper;
    @Resource
    private SmsTplMapper smsTplMapper;
    @Resource
    private BorrowRepayMapper borrowRepayMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private BorrowMainMapper borrowMainMapper;
    @Resource
    private UserBaseInfoMapper userBaseInfoMapper;
    @Resource
    private JmsActiveTaskDomain jmsActiveTaskDomain;

    @Override
    public int activePayment(long userId, long borrowMainId, Date settleTime, BigDecimal repayAmount, String repayOrderNo) {
        try {
            BorrowMainModel borrowMain = borrowMainMapper.findById(borrowMainId);
            User user = userMapper.selectByPrimaryKey(userId);
            // 根据付款的订单号，推算出用户还款的期数
            logger.info("立即还款短信通知:userId={},borrowMainId={},settleTime={},repayAmount={},repayOrderNo={}",new Object[]{userId,borrowMainId, JSONObject.toJSONString(settleTime),repayAmount,repayOrderNo});
            String periods = getRepayPeriods(repayOrderNo);
            if (user != null && borrowMain != null) {
                SmsTplExample example = new SmsTplExample();
                example.createCriteria().andTypeEqualTo("activePayment").andStateEqualTo("10");
                List<SmsTpl> infos = smsTplMapper.selectByExample(example);
                SmsTpl tpl =  infos.size() >0 ? infos.get(0) : null;
                if (tpl != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("cardNo",borrowMain.getCardNo().substring(borrowMain.getCardNo().length()-4));
                    map.put("time", settleTime);
                    map.put("loan", repayAmount);
                    map.put("bank",borrowMain.getBank());
                    Map<String, Object> payload = new HashMap<>();
                    payload.put("orderNo", repayOrderNo);
                    payload.put("mobile", user.getLoginName());
                    payload.put("message", changeMessage("activePayment", map));
                    return sendSmsByType(tpl.getNumber(), payload, user.getLoginName(), "activePayment", "");
                }
            }
        } catch (Exception e) {
            logger.error("立即还款成功，短信发送失败，异常原因:",e);
        }
        return 0;
    }

    @Override
    public int payment(Long userId, Long borrowMainId, Date date, BigDecimal amount, String orderNo) {
        try {
            BorrowMain borrowMain = borrowMainMapper.findById(borrowMainId);
            User user = userMapper.selectByPrimaryKey(userId);
            if (user != null && borrowMain != null) {
                SmsTplExample example = new SmsTplExample();
                example.createCriteria().andTplEqualTo("payment").andStateEqualTo("10");
                List<SmsTpl> infos = smsTplMapper.selectByExample(example);
                SmsTpl tpl =  infos.size() >0 ? infos.get(0) : null;
                if (tpl != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("time", borrowMain.getRepayTime());
                    map.put("loan", amount);
                    Map<String, Object> payload = new HashMap<>();
                    payload.put("orderNo", orderNo);
                    payload.put("mobile", user.getLoginName());
                    payload.put("message", changeMessage("payment", map));
                    return sendSmsByType(tpl.getNumber(), payload, user.getLoginName(), "payment", "");
                }
            }
        } catch (Exception e) {
            logger.error("立即还款成功，短信发送失败，异常原因:",e);
        }
        return 0;
    }

    @Override
    public int overdue(long borrowId,long userId){
        SmsTplExample example = new SmsTplExample();
        example.createCriteria().andTplEqualTo("overdue").andStateEqualTo("10");
        List<SmsTpl> infos = smsTplMapper.selectByExample(example);
        SmsTpl tpl =  infos.size() >0 ? infos.get(0) : null;
        if (tpl!=null) {
            BorrowRepayModel brm = borrowRepayMapper.findOverdue(borrowId);
            Map<String,Object> map = new HashMap<>();
            map.put("day",brm.getPenaltyDay());
            map.put("overdue",brm.getPenaltyAmout());
            map.put("loan",brm.getAmount().add(brm.getPenaltyAmout()));
            Map<String, Object> payload = new HashMap<>();
            payload.put("mobile", brm.getPhone());
            payload.put("message", changeMessage("overdue",map));
            payload.put("orderNo",brm.getOrderNo());
            return sendSmsByType(tpl.getNumber(),payload,brm.getPhone(),"overdue","");
        }else {
            return 0;
        }
    }

    @Override
    public int repayBefore(long userId, long borrowId) {
        SmsTplExample example = new SmsTplExample();
        example.createCriteria().andTplEqualTo("repayBefore").andStateEqualTo("10");
        List<SmsTpl> infos = smsTplMapper.selectByExample(example);
        SmsTpl tpl =  infos.size() >0 ? infos.get(0) : null;
        String smsNo = tpl != null ? tpl.getNumber() : null;
        if (smsNo!=null) {
            BorrowRepayModel brm = borrowRepayMapper.findOverdue(borrowId);
            Map<String,Object> map = new HashMap<>();
            map.put("time", brm.getRepayTime());
            map.put("loan",brm.getAmount());
            Map<String, Object> payload = new HashMap<>();
            payload.put("mobile", brm.getPhone());
            payload.put("orderNo", brm.getOrderNo());
            payload.put("message", changeMessage("repayBefore",map));
            return sendSmsByType(smsNo,payload,brm.getPhone(),"repayBefore","");
        }
        return 0;
    }

    @Override
    public int refuse(long userId, int day, String orderNo) {
        try{
            SmsTplExample example = new SmsTplExample();
            example.createCriteria().andTplEqualTo("refuse").andStateEqualTo("10");
            List<SmsTpl> infos = smsTplMapper.selectByExample(example);
            SmsTpl tpl =  infos.size() >0 ? infos.get(0) : null;
            String smsNo = tpl != null ? tpl.getNumber() : null;
            if (smsNo!=null) {
                UserBaseInfoExample example1 = new UserBaseInfoExample();
                example1.createCriteria().andUserIdEqualTo(userId);
                List<UserBaseInfo> infos1 = userBaseInfoMapper.selectByExample(example1);
                if(infos1.size() > 0){
                    UserBaseInfo bi = infos1.get(0);
                    Map<String,Object> map = new HashMap<>();
                    map.put("day",day);
                    Map<String, Object> payload = new HashMap<>();
                    payload.put("mobile", bi.getPhone());
                    payload.put("orderNo", orderNo);
                    payload.put("message", changeMessage("refuse",map));
                    return sendSmsByType(smsNo,payload,bi.getPhone(),"refuse","");
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            logger.error("生成消息异常，异常原因:",e);
        }
        return 0;
    }

    @Override
    public List<Sms> getResendSmsList(Map<String, Object> map) {
        return smsMapper.findResendList(map);
    }

    @Override
    public void updateByIds(Map<String, Object> clSmsInfo) {
        smsMapper.updateByIds(clSmsInfo);
    }
    @Override
    public void updateByMsgids(Map<String, Object> tyhSmsInfo) {
        smsMapper.updateByMsgids(tyhSmsInfo);
    }

    @Override
    public int countDayTime(String phone, String email, String type) {
        String mostTimes = Global.getValue("sms_day_most_times");
        int mostTime = JSONObject.parseObject(mostTimes).getIntValue(type);

        Map<String,Object> data = new HashMap<>();
        if(StringUtil.isNotBlank(phone)){
            data.put("phone", phone);
        }else{
            data.put("email",email);
        }
        data.put("smsType", type);
        int times = smsMapper.countDayTime(data);

        return mostTime - times;
    }

    @Override
    public int sendMsg(Sms sms) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("mobile",sms.getPhone());
        payload.put("message",sms.getContent());
        payload.put("orderNo",sms.getOrderNo());
        payload.put("smsPlatform",sms.getSmsPlatform());
        return sendSmsByType(null,payload,sms.getPhone(),sms.getSmsType(),sms.getCode());
    }

    private String getRepayPeriods(String repayOrderNo) {
        //订单号中含有'X'的是新的分期业务，不含的为单期借款老数据
        StringBuilder msg = new StringBuilder();
        String[] orderNos = repayOrderNo.split("X");
        if (orderNos.length > 1) {
            String stages = orderNos[1].split("Y")[0];
            String[] stage = stages.split("N");
            String startStage = stage[0];
            if (stage.length > 1) {
                String endStage = stage[1];
                msg.append("第").append(startStage).append("-").append(endStage).append("期");
            } else {
                msg.append("第").append(startStage).append("期");
            }
            return msg.toString();

        } else {
            return msg.toString();
        }
    }
    public String ret(String type) {
        SmsTplExample example = new SmsTplExample();
        example.createCriteria().andTypeEqualTo(type).andStateEqualTo("10");
        List<SmsTpl> infos = smsTplMapper.selectByExample(example);
        if (infos.size() > 0) {
            return infos.get(0).getTpl();
        } else {
            return null;
        }
    }
    
    protected String changeMessage(String smsType, Map<String,Object> map) {
        String message = "";
        String[] date;
        String time = "";
        if(map.get("time")!=null){
            date = map.get("time").toString().split(" ");
            time = date[1]+" "+date[2]+" "+date[5];
        }
        if ("overdue".equals(smsType)) {
            message = ret(smsType);
            message = message.replace("{$day}", StringUtil.isNull(map.get("day")));
            message = message.replace("{$overdue}",StringUtil.isNull(map.get("overdue")));
            message = message.replace("{$loan}",StringUtil.isNull(map.get("loan")));
        }
        if ("loanInform".equals(smsType)) {
            message = ret(smsType);
            message = message.replace("{$time}",time);
        }
        if ("repayInform".equals(smsType)) {
            message = ret(smsType);
            message = message.replace("{$time}",time);
            message = message.replace("{$loan}", StringUtil.isNull(map.get("loan")));
        }
        if ("repayBefore".equals(smsType)) {
            message = ret(smsType);
            message = message.replace("{$time}",time);
            message = message.replace("{$loan}", StringUtil.isNull(map.get("loan")));
        }
        if ("refuse".equals(smsType)) {
            message = ret(smsType);
            message = message.replace("{$day}", StringUtil.isNull(map.get("day")));
        }
        if ("activePayment".equals(smsType)) {
            message = ret(smsType);
            message = message.replace("{$cardNo}", StringUtil.isNull(map.get("cardNo")));
            message = message.replace("{$time}",time);
            message = message.replace("{$loan}", StringUtil.isNull(map.get("loan")));
            message = message.replace("{$bank}", StringUtil.isNull(map.get("bank")));
        }
        if ("payment".equals(smsType)) {
            message = ret(smsType);
            message = message.replace("{$time}",time);
            message = message.replace("{$loan}", StringUtil.isNull(map.get("loan")));
        }
        if("creditsUpgrade".equals(smsType)){
            message = ret(smsType);
            message = message.replace("{$credits}", StringUtil.isNull(map.get("credits")));
            message = message.replace("{$validPeriod}", StringUtil.isNull(map.get("validPeriod")));
        }
        if("register".equals(smsType)||"findReg".equals(smsType)||"bindCard".equals(smsType)||"verifyCode".equals(smsType)){
            message = ret(smsType);
            message = message.replace("{$vcode}", StringUtil.isNull(map.get("vcode")));
            message = message.replace("{$timelimit}", StringUtil.isNull(map.get("timelimit")));
        }
        logger.info("短信发送内容:" + message);
        return message;
    }

    private int sendSmsByType(String smsNo, Map<String, Object> payload, String phone,String type,String vcode) {
        payload.put("vcode",vcode);
        payload.put("type",type);
        if("dev".equals(Global.getValue("app_environment"))){
            SmsApi api = new SmsApi();
            return result(api.sendMsg(payload),phone,type);
        }else{
            payload.put("smsPlatform",Global.getValue("sms_platform"));
            payload.put("smsType",type);
            Integer retCode = jmsActiveTaskDomain.addSmsNotify(payload).getRet_code();
            if(retCode != 100){
                return 0;
            }
            return 1;
        }
    }

    private int resultToSmsType(JSONObject resultJson,String phoneOrEmail,String type){
        Sms sms = new Sms();
        if(StringUtil.isMail(phoneOrEmail)){
            sms.setEmail(phoneOrEmail);
        }else{
            sms.setPhone(phoneOrEmail);
        }
        sms.setSendTime(DateUtil.getNow());
        sms.setContent(resultJson.getString("content"));
        sms.setResp("短信已发送");
        sms.setSmsType(type);
        sms.setCode(resultJson.getString("vcode"));
        sms.setOrderNo(resultJson.getString("orderNo"));
        if(resultJson.getString("msgid")!=null) {
            sms.setMsgid(resultJson.getString("msgid"));
        }
        sms.setState("10");
        sms.setSmsPlatform(resultJson.getString("smsPlatform"));
        sms.setVerifyTime(0);
        return smsMapper.insertSelective(sms);
    }

    private int errorSms(JSONObject resultJson,String phoneOrEmail,String type){
        Sms sms = new Sms();
        if(StringUtil.isMail(phoneOrEmail)){
            sms.setEmail(phoneOrEmail);
        }else {
            sms.setPhone(phoneOrEmail);
        }
        sms.setSendTime(DateUtil.getNow());
        String content;
        content = resultJson.getString("error");
        sms.setContent(resultJson.getString("content"));
        logger.error("[新短信平台发送异常][phone:"+phoneOrEmail+",异常信息:"+content+"]");
        sms.setSmsPlatform(resultJson.getString("smsPlatform"));
        sms.setRespTime(DateUtil.getNow());
        sms.setResp(content);
        sms.setSmsType(type);
        sms.setCode(resultJson.getString("vcode"));
        sms.setOrderNo(resultJson.getString("orderNo"));
        sms.setState("20");
        sms.setVerifyTime(0);
        if(resultJson.getString("msgid")!=null) {
            sms.setMsgid(resultJson.getString("msgid"));
        }
        smsMapper.insertSelective(sms);
        return 0;
    }
    
   

    //平台短信
    private int result(String result,String phoneOrEmail,String type){
        JSONObject resultJson = JSONObject.parseObject(result);
        logger.info("------------->result:{},phone:{},type={}",new Object[]{result,phoneOrEmail,type});
        if(null!=resultJson && !resultJson.isEmpty()){
            if ((resultJson.getInteger("code") != null && resultJson.getInteger("code") == 0)||
                    (resultJson.getInteger("status") !=null && resultJson.getInteger("status") == 0)) {
                return resultToSmsType(resultJson, phoneOrEmail, type);
            }
        }
        return errorSms(resultJson, phoneOrEmail, type);
    }
}
