package com.adpanshi.cashloan.manage.cl.model.moxie;


/**
 * 通话记录
 * @author ppchen
 * 2017年8月18日 下午12:01:09
 * 
 */
public class CallingRecord {

	private String phone_card;

	private String phone;

	private String destination;

	private String stop_call;

	private String call_details;


	public String getPhone_card() {
		return phone_card;
	}

	public void setPhone_card(String phone_card) {
		this.phone_card = phone_card;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getStop_call() {
		return stop_call;
	}

	public void setStop_call(String stop_call) {
		this.stop_call = stop_call;
	}

	public String getCall_details() {
		return call_details;
	}

	public void setCall_details(String call_details) {
		this.call_details = call_details;
	}
}
