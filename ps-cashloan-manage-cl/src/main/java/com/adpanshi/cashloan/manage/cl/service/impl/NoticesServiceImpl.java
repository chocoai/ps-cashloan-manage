package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.manage.cl.mapper.AppMsgTplMapper;
import com.adpanshi.cashloan.manage.cl.mapper.BorrowMainMapper;
import com.adpanshi.cashloan.manage.cl.mapper.BorrowRepayMapper;
import com.adpanshi.cashloan.manage.cl.mapper.NoticesMapper;
import com.adpanshi.cashloan.manage.cl.model.AppMsgTpl;
import com.adpanshi.cashloan.manage.cl.model.AppMsgTplExample;
import com.adpanshi.cashloan.manage.cl.model.BorrowMain;
import com.adpanshi.cashloan.manage.cl.model.Notices;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowMainModel;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowRepayModel;
import com.adpanshi.cashloan.manage.cl.service.NoticesService;
import com.adpanshi.cashloan.manage.core.common.util.DateUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
 * @Description: 消息serviceImpl
 * @date 2018/8/4 0:15
 */
@Service("noticesService")
public class NoticesServiceImpl implements NoticesService {
    private static final Logger logger = LoggerFactory.getLogger(NoticesServiceImpl.class);
    @Resource
    private NoticesMapper noticesMapper;
    @Resource
    private BorrowMainMapper borrowMainMapper;
    @Resource
    private AppMsgTplMapper appMsgTplMapper;
    @Resource
    private BorrowRepayMapper borrowRepayMapper;
    @Override
    public Page<Notices> queryNoticesList(Map<String, Object> params, int currentPage, int pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        Page<Notices> page = (Page<Notices>)noticesMapper.listSelective(params);
        return page;
    }

    @Override
    public int insert(Notices notices) {
        return noticesMapper.insertSelective(notices);
    }

    @Override
    public int updateById(Notices notices) {
        return noticesMapper.updateByPrimaryKeySelective(notices);
    }

    @Override
    public Notices findByPrimary(Long id) {
        return noticesMapper.selectByPrimaryKey(id);
    }

    @Override
    public void payment(Long userId, Long borrowMainId, BigDecimal amount) {
        try{
            BorrowMain borrowMain = borrowMainMapper.selectByPrimaryKey(borrowMainId);
            if(borrowMain != null){
                AppMsgTplExample example = new AppMsgTplExample();
                example.createCriteria().andTypeEqualTo("payment").andStateEqualTo(10);
                List<AppMsgTpl> infos = appMsgTplMapper.selectByExample(example);
                if(infos.size() > 0){
                    AppMsgTpl tpl = infos.get(0);
                    String[] date = borrowMain.getRepayTime().toString().split(" ");
                    String time = date[1]+" "+date[2]+" "+date[5];
                    String content = tpl.getContent().replace("{$loan}",amount+"")
                            .replace("{$time}",time);
                    save(userId,tpl.getName(),content);
                }
            }
        } catch (Exception e) {
            logger.error("生成消息异常，异常原因:",e);
        }
    }

    @Override
    public void activePayment(long userId, long borrowMainId, Date settleTime, BigDecimal repayAmount) {
        try{
            BorrowMainModel borrowMain = borrowMainMapper.findById(borrowMainId);
            if( borrowMain!= null){
                AppMsgTplExample example = new AppMsgTplExample();
                example.createCriteria().andTypeEqualTo("activePayment").andStateEqualTo(10);
                List<AppMsgTpl> infos = appMsgTplMapper.selectByExample(example);
                if(infos.size() > 0){
                    AppMsgTpl tpl = infos.get(0);
                    String[] date = settleTime.toString().split(" ");
                    String time = date[1]+" "+date[2]+" "+date[5];
                    String content = tpl.getContent().replace("{$bank}",borrowMain.getBank())
                            .replace("{$cardNo}",borrowMain.getCardNo().substring(borrowMain.getCardNo().length()-4))
                            .replace("{$loan}",repayAmount+"")
                            .replace("{$time}",time);
                    save(userId,tpl.getName(),content);
                }
            }
        } catch (Exception e) {
            logger.error("生成消息异常，异常原因:",e);
        }
    }

    @Override
    public void refuse(long userId, int day) {
        try{
            AppMsgTplExample example = new AppMsgTplExample();
            example.createCriteria().andTypeEqualTo("refuse").andStateEqualTo(10);
            List<AppMsgTpl> infos = appMsgTplMapper.selectByExample(example);
            if(infos.size() > 0){
                AppMsgTpl tpl = infos.get(0);
                String content = tpl.getContent().replace("{$day}",day+"");
                save(userId,tpl.getName(),content);
            }

        } catch (Exception e) {
            logger.error("生成消息异常，异常原因:",e);
        }
    }

    @Override
    public void overdue(long borrowId, long userId) {
        try{
            BorrowRepayModel brm = borrowRepayMapper.findOverdue(borrowId);
            if(brm != null){
                AppMsgTplExample example = new AppMsgTplExample();
                example.createCriteria().andTypeEqualTo("overdue").andStateEqualTo(10);
                List<AppMsgTpl> infos = appMsgTplMapper.selectByExample(example);
                if(infos.size() > 0) {
                    AppMsgTpl tpl = infos.get(0);
                    String content = tpl.getContent().replace("{$day}", brm.getPenaltyDay()+"");
                    save(userId, tpl.getName(), content);
                }
            }
        } catch (Exception e) {
            logger.error("生成消息异常，异常原因:",e);
        }
    }

    @Override
    public void repayBefore(long userId, long borrowId) {
        try{
            BorrowRepayModel brm = borrowRepayMapper.findOverdue(borrowId);
            if(brm != null){
                AppMsgTplExample example = new AppMsgTplExample();
                example.createCriteria().andTypeEqualTo("repayBefore").andStateEqualTo(10);
                List<AppMsgTpl> infos = appMsgTplMapper.selectByExample(example);
                if(infos.size() > 0) {
                    AppMsgTpl tpl = infos.get(0);
                    String[] date = brm.getRepayTime().toString().split(" ");
                    String time = date[1]+" "+date[2]+" "+date[5];
                    String content = tpl.getContent().replace("{$time}",time)
                            .replace("{$loan}",brm.getAmount()+"");
                    save(userId,tpl.getName(),content);
                }
            }
        } catch (Exception e) {
            logger.error("生成消息异常，异常原因:",e);
        }
    }

    private void save(Long userId, String title, String content) throws Exception{
        Notices notices = new Notices();
        notices.setUserId(userId);
        notices.setTitle(title);
        notices.setType("1");
        notices.setContent(content);
        notices.setCreateTime(DateUtil.getNow());
        notices.setState("10");
        noticesMapper.insertSelective(notices);
    }
}
