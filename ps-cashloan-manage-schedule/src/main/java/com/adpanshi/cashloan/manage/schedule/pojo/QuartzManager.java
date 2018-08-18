package com.adpanshi.cashloan.manage.schedule.pojo;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;

/**
 * 定时任务动态修改
 */
@SuppressWarnings({"rawtypes"})
public class QuartzManager {

    private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
    private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";
    private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";
    public static final Logger logger = LoggerFactory.getLogger(QuartzManager.class);

    /**
     * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
     *
     * @param jobName
     * @param cls
     * @param time
     */
    public static void addJob(String jobName, Class cls, String time) {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            JobDetail jobDetail = new JobDetail(jobName, JOB_GROUP_NAME, cls);// 任务名，任务组，任务执行类
            // 触发器
            CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);// 触发器名,触发器组
            trigger.setCronExpression(time);// 触发器时间设定
            sched.scheduleJob(jobDetail, trigger);
            // 启动
            if (!sched.isShutdown()) {
                sched.start();
            }
        } catch (SchedulerException | ParseException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
     *
     * @param jobName
     * @param time
     */
    public static void modifyJobTime(String jobName, String time) {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            CronTrigger trigger = (CronTrigger) sched.getTrigger(jobName,
                    TRIGGER_GROUP_NAME);
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
                JobDetail jobDetail = sched.getJobDetail(jobName, JOB_GROUP_NAME);
                Class objJobClass = jobDetail.getJobClass();
                removeJob(jobName);
                addJob(jobName, objJobClass, time);
            }
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
     *
     * @param jobName
     */
    public static void removeJob(String jobName) {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            sched.pauseTrigger(jobName, TRIGGER_GROUP_NAME);// 停止触发器
            sched.unscheduleJob(jobName, TRIGGER_GROUP_NAME);// 移除触发器
            sched.deleteJob(jobName, JOB_GROUP_NAME);// 删除任务
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 立即执行任务(使用默认的任务组名，触发器名，触发器组名)
     *
     * @param jobName
     */
    public static void startJobNow(String jobName) {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            sched.triggerJob(jobName, JOB_GROUP_NAME);
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }

    }

    /**
     * 启动所有定时任务
     */
    public static void startJobs() {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            sched.start();
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 关闭所有定时任务
     */
    public static void shutdownJobs() {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            if (!sched.isShutdown()) {
                sched.shutdown();
            }
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }
}