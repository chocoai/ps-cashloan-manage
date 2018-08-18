package com.adpanshi.cashloan.manage.schedule.job;

import com.adpanshi.cashloan.manage.cl.model.*;
import com.adpanshi.cashloan.manage.cl.model.expand.*;
import com.adpanshi.cashloan.manage.cl.pojo.BorrowAndRepayModel;
import com.adpanshi.cashloan.manage.cl.service.*;
import com.adpanshi.cashloan.manage.core.common.context.Global;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.util.BeanUtil;
import tool.util.DateUtil;
import tool.util.StringUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 计算逾期任务（加入分期和最大逾期限制）
 * <p>
 * 1.最大逾期开启：
 * (1)最大逾期罚金 = max_penalty[cl_borrow_main(template_info)]  * amount[cl_borrow_main]
 * (2)先计算所有相关还款计划的逾期罚金是否达到最大值，是则结束，否则往下继续。
 * (3)penalty_amout1 = Max_penalty_amout - SUM(penalty_amout[other])
 * (4)penalty_amout2[cl_borrow_repay] = penalty[cl_borrow_main(template_info)] * amount[cl_borrow] * penalty_day[cl_borrow_repay]
 * (5)penalty_amout = penalty_amout1 < penalty_amout2 ? penalty_amout1 : penalty_amout2
 * 2.最大逾期关闭
 * penalty_amout[cl_borrow_repay] = penalty[cl_borrow_main(template_info)] * amount[cl_borrow] * penalty_day[cl_borrow_repay]
 * </p>
 * 【逾期日罚率】cl_borrow_main表中template_info中的 penalty值；
 * 【兼容老数据】如果cl_borrow表中borrow_main_id 为null(或为0),算法与之前相同（从系统参数中取罚金）
 *
 * @author yecy
 * @date 2017/12/19 21:49
 */
public class QuartzLateInStage implements Job {
    private static final Logger logger = LoggerFactory.getLogger(QuartzLateInStage.class);

    private final BorrowRepayService borrowRepayService;
    private final BorrowProgressService borrowProgressService;
    private final BorrowService clBorrowService;
    private final UrgeRepayOrderService urgeRepayOrderService;

    private volatile AtomicInteger succeed = new AtomicInteger(0);
    private volatile AtomicInteger fail = new AtomicInteger(0);
    private volatile AtomicInteger noLate = new AtomicInteger(0);
    private volatile AtomicInteger other = new AtomicInteger(0);

    public QuartzLateInStage() {
        borrowRepayService = (BorrowRepayService) BeanUtil.getBean
                ("borrowRepayService");
        borrowProgressService = (BorrowProgressService) BeanUtil.getBean("borrowProgressService");
        urgeRepayOrderService = (UrgeRepayOrderService) BeanUtil.getBean("urgeRepayOrderService");
        clBorrowService = (BorrowService) BeanUtil.getBean("borrowService");
    }

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        QuartzInfoService quartzInfoService = (QuartzInfoService) BeanUtil.getBean("quartzInfoService");
        QuartzLogService quartzLogService = (QuartzLogService) BeanUtil.getBean("quartzLogService");
        QuartzLog ql = new QuartzLog();
//        Map<String, Object> qiData = new HashMap<>();
        QuartzInfo qiData = new QuartzInfo();
        QuartzInfo qi = quartzInfoService.findSelective("doLateInStage");
        //@remarks:只有启用状态才会调用.@date:20170814 @author:nmnl
        if (null == qi) {
            logger.info(" [新定时任务][定时计算逾期] 启动失败:原因未启用! ");
            return;
        }
        try {
            qiData.setId(qi.getId());
            ql.setQuartzId(qi.getId());
            ql.setStartTime(DateUtil.getNow());
            String remark = lateNew();
            ql.setTime(DateUtil.getNow().getTime() - ql.getStartTime().getTime());
            ql.setResult("10");
            ql.setRemark(remark);
            qiData.setSucceed(qi.getSucceed() + 1);
        } catch (Exception e) {
            ql.setResult("20");
            qiData.setFail(qi.getFail() + 1);
            logger.error(e.getMessage(), e);
        } finally {
            logger.info("保存定时任务日志");
            quartzLogService.save(ql);
            quartzInfoService.update(qiData);
        }
    }

    private String lateNew() {
        BorrowRepayModelService borrowRepayModelService = (BorrowRepayModelService) BeanUtil.getBean("borrowRepayModelService");
        logger.info("进入逾期计算...");

        Date now = new Date();
        List<BorrowAndRepayModel> repays = borrowRepayModelService.findExpireToRepay(BorrowRepayModel.STATE_REPAY_NO,
                now);
        if (CollectionUtils.isEmpty(repays)) {
            return "执行总次数0次";
        }

        Map<Long, List<BorrowAndRepayModel>> repayMap = new HashMap<>();

        for (BorrowAndRepayModel repay : repays) {
            BorrowMain borrowMain = repay.getBorrowMain();
            if (borrowMain != null) {
                Long mainId = borrowMain.getId();
                List<BorrowAndRepayModel> repayModels = (List<BorrowAndRepayModel>) MapUtils.getObject(repayMap, mainId, new ArrayList<>());
                repayModels.add(repay);
                repayMap.put(mainId, repayModels);
            }
        }

        int listNum = Global.getInt("doLate_num");
        if (listNum <= 0) {
            listNum = 1;
        }

        int threadSize = repayMap.size();
        if (listNum > threadSize) {
            listNum = threadSize;
        }

        CountDownLatch countDownLatch = new CountDownLatch(listNum);
        startStagesTask(listNum, repayMap, countDownLatch);
        try {
            countDownLatch.await(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.error("逾期计算计数器出错" + e.getMessage());
            Thread.currentThread().interrupt();
        }

        logger.info("逾期计算结束...");
        return "执行总次数" + (fail.get() + succeed.get() + other.get() + noLate.get()) + ",成功" + succeed.get() + "次,失败" +
                fail.get() + "次" + ",不用计算" + noLate.get() + "次,计算途中已还款" + other.get() + "次";
    }


    private List<Integer> getListSizeInThread(int threadSize, int allSize) {
        int listSize = allSize / threadSize;
        int moreOneSize = allSize % threadSize;

        List<Integer> lengthList = new ArrayList<>(threadSize);
        for (int i = 0; i < threadSize; i++) {
            if (moreOneSize > i) {
                lengthList.add(listSize + 1);
            } else {
                lengthList.add(listSize);
            }
        }
        return lengthList;
    }

    private void startStagesTask(Integer threadSize, Map<Long, List<BorrowAndRepayModel>> repayMap, CountDownLatch countDownLatch) {
        Integer mapSize = repayMap.size();
        List<Integer> mapLengthList = getListSizeInThread(threadSize, mapSize);
        Iterator<List<BorrowAndRepayModel>> repayModelListIt = repayMap.values().iterator();
        for (Integer mapLength : mapLengthList) {
            List<List<BorrowAndRepayModel>> repayModels = new ArrayList<>(mapLength);
            int i = 0;
            while (i < mapLength && repayModelListIt.hasNext()) {
                List<BorrowAndRepayModel> repayModel = repayModelListIt.next();
                repayModels.add(repayModel);
                repayModelListIt.remove();
                i++;
            }
            new StagesRepayThread(repayModels, countDownLatch).start();
        }
    }

    private Boolean dealWithOverdue(BorrowAndRepayModel repayModel, Integer day, BigDecimal penaltyAmount) throws Exception{
        BorrowRepay br = new BorrowRepay();
        br.setId(repayModel.getRepayId());
        br.setPenaltyAmout(penaltyAmount);
        br.setPenaltyDay(day);
        logger.info("id--" + repayModel.getRepayId() + " ==> 已经逾期 " + day + " 天,逾期费用 " + penaltyAmount + "元");
        int msg = borrowRepayService.updateLate(br);

        if (msg <= 0) {
            return false;
        }
        Long borrowId = repayModel.getBorrowId();
        //保存逾期进度
        List<String> stateList = Arrays.asList(BorrowProgressModel.PROGRESS_BILL_BAD, BorrowProgressModel.PROGRESS_REPAY_OVERDUE);
        List<BorrowProgress> bpList = borrowProgressService.findProcessByState(borrowId, stateList);
        if (CollectionUtils.isEmpty(bpList)) {
            logger.info("---------添加逾期进度---------");
            BorrowProgress bp = new BorrowProgress();
            bp.setBorrowId(borrowId);
            bp.setCreateTime(new Date());
            bp.setRemark("您已逾期,请尽快还款");
            bp.setState(BorrowModel.STATE_DELAY);
            bp.setUserId(repayModel.getUserId());
            borrowProgressService.save(bp);
            clBorrowService.modifyState(borrowId, BorrowModel.STATE_DELAY);
            logger.info("---------添加逾期结束---------");
        }
        //催收计划
        logger.info("---------修改催收计划start-------");
        UrgeRepayOrder uro = urgeRepayOrderService.findByBorrowId(borrowId);
        logger.info("[查询uro结束][uro:{}]", uro);
        if (StringUtil.isBlank(uro)) {
            urgeRepayOrderService.saveOrder(borrowId);
        } else {
            Map<String, Object> uroMap = new HashMap<>();
            uroMap.put("penaltyAmout", br.getPenaltyAmout());
            uroMap.put("penaltyDay", br.getPenaltyDay());
            uroMap.put("id", uro.getId());
            uroMap.put("level", UrgeRepayOrderModel.getLevelByDay(Long.valueOf(br.getPenaltyDay())));
            urgeRepayOrderService.updateLate(uroMap);
            logger.info("[更新uro结束][uro:" + uro.getId() + "]");
        }
        logger.info("---------修改催收计划end-------");

        return true;
    }

    /**
     * 分期借款的逾期计算任务
     */
    private class StagesRepayThread extends Thread {
        List<List<BorrowAndRepayModel>> repayModelList;

        private CountDownLatch countDownLatch;

        private StagesRepayThread(List<List<BorrowAndRepayModel>> repayModelList, CountDownLatch countDownLatch) {
            this.repayModelList = repayModelList;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                for (List<BorrowAndRepayModel> repayModels : repayModelList) {
                    BorrowMain borrowMain = repayModels.get(0).getBorrowMain();
                    String templateInfo = borrowMain.getTemplateInfo();
                    BorrowTemplateModel template = JSONObject.parseObject(templateInfo, BorrowTemplateModel.class);
                    BigDecimal penalty = template.getPenalty();

                    if (!template.getMaxPenaltyOpen()) {
                        for (BorrowAndRepayModel repayModel : repayModels) {
                            Integer day = DateUtil.daysBetween(repayModel.getRepayTime(), new Date());
                            if (day > Integer.parseInt(repayModel.getPenaltyDay())) {
                                if (BorrowModel.STATE_FINISH.equals(repayModel.getBorrowState())
                                        || BorrowModel.STATE_REMISSION_FINISH.equals(repayModel.getBorrowState())) {
                                    other.incrementAndGet();
                                    continue;
                                }
                                BigDecimal penaltyAmount = penalty.multiply(new BigDecimal(repayModel.getAmount())).multiply(new BigDecimal(day));
                                Boolean updateSuccess = dealWithOverdue(repayModel, day, penaltyAmount);
                                if (updateSuccess) {
                                    succeed.incrementAndGet();
                                } else {
                                    fail.incrementAndGet();
                                }
                            } else {
                                noLate.incrementAndGet();
                            }
                        }
                    } else {
                        BigDecimal allPenaltyAmount = BigDecimal.ZERO;
                        for (BorrowAndRepayModel repayModel : repayModels) {
                            allPenaltyAmount = allPenaltyAmount.add(new BigDecimal(repayModel.getPenaltyAmout()));
                        }
                        // 超过最大逾期金额，则金额不再增加
                        BigDecimal maxPenaltyAmount = borrowMain.getAmount().multiply(template.getMaxPenalty());
                        if (allPenaltyAmount.compareTo(maxPenaltyAmount) >= 0) {
                            logger.info("BorrowMain(id:{})总利息超过最大逾期罚金，利息不再增加。", borrowMain.getId());
                            noLate.incrementAndGet();
                            continue;
                        }

                        for (BorrowAndRepayModel repayModel : repayModels) {
                            Integer day = DateUtil.daysBetween(repayModel.getRepayTime(), new Date());
                            if (day > Integer.parseInt(repayModel.getPenaltyDay())) {
                                if (BorrowModel.STATE_FINISH.equals(repayModel.getBorrowState())
                                        || BorrowModel.STATE_REMISSION_FINISH.equals(repayModel.getBorrowState())) {
                                    other.incrementAndGet();
                                    continue;
                                }

                                BigDecimal otherPenaltyAmount = allPenaltyAmount.subtract(new BigDecimal(repayModel.getPenaltyAmout()));
                                BigDecimal penaltyAmount1 = (maxPenaltyAmount.subtract(otherPenaltyAmount)).divide(BigDecimal.ONE, 2, 4);
                                BigDecimal penaltyAmount2 = penalty.multiply(new BigDecimal(repayModel.getAmount())).multiply(new BigDecimal(day)).divide(BigDecimal.ONE, 2, 4);
                                BigDecimal penaltyAmount = penaltyAmount1.compareTo(penaltyAmount2) > 0 ? penaltyAmount2 : penaltyAmount1;
                                Boolean updateSuccess = dealWithOverdue(repayModel, day, penaltyAmount);
                                if (updateSuccess) {
                                    succeed.incrementAndGet();
                                    allPenaltyAmount = otherPenaltyAmount.add(penaltyAmount);
                                } else {
                                    fail.incrementAndGet();
                                }
                            } else {
                                noLate.incrementAndGet();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        }
    }

}
