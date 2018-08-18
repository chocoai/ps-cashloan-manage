package com.adpanshi.cashloan.manage.cl.model.moxie;

/**
 * 通话记录详情
 * @author 8470
 *
 */
public class CallingDetail {

	/**
	 * 通话日期 mm/dd/yyyy
	 */
	private String date;

	/**
	 * 通话时长 (s)
	 */
	private String duration;

	/**
	 * 通话时间 HH:mm:ss
	 */
	private String time;

	/**
	 * 呼入=1、呼出=2、未接=3、语音=4、拒接=5、未接通=6
	 */
	private String call_type;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getCall_type() {
		return call_type;
	}

	public void setCall_type(String call_type) {
		this.call_type = call_type;
	}
}
