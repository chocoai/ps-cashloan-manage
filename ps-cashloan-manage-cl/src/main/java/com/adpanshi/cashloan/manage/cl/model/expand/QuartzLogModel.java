package com.adpanshi.cashloan.manage.cl.model.expand;


import com.adpanshi.cashloan.manage.cl.model.QuartzLog;

/**
 * 定时任务记录Model
 * 

 * @version 1.0.0
 * @date 2017年3月27日 上午10:04:47
 * Copyright 粉团网路 All Rights Reserved
 *
 * 
 *
 */
public class QuartzLogModel extends QuartzLog {

	private static final long serialVersionUID = 1L;

	/** 执行结果 - 成功 */
	public static final String RESULT_SUCCESS = "10";
	/** 执行结果 - 失败 */
	public static final String RESULT_FAIL = "20";
	
	/**
	 * 任务名称
	 */
	private String name;

	/**
	 * 任务执行结果中文描述
	 */
	private String resultStr;

	/**
	 * 获取任务名称
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置任务名称
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取任务执行结果中文描述
	 * 
	 * @return resultStr
	 */
	public String getResultStr() {
		this.resultStr = convertResult(this.getResult());
		return resultStr;
	}

	/**
	 * 设置任务执行结果中文描述
	 * 
	 * @param resultStr
	 */
	public void setResultStr(String resultStr) {
		this.resultStr = resultStr;
	}
	
	/**
	 * 执行结果中文描述转换
	 * @param result
	 * @return
	 */
	public static String convertResult(String result) {
		String resultStr = " - ";
		if (QuartzLogModel.RESULT_SUCCESS.equals(result)) {
			resultStr = "执行成功";
		} else if (QuartzLogModel.RESULT_FAIL.equals(result)) {
			resultStr = "执行失败";
		}
		return resultStr;
	}
}
