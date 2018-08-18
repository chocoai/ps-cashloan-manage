package com.adpanshi.cashloan.manage.cl.model.moxie;


/**
 * 短信记录
 * @author ppchen
 * 2017年8月18日 下午3:17:43
 * 
 */
public class MessageBill {

	/**
	 * 短信是否已读
	 */
	private String read;

	/**
	 * 短信发送方
	 */
	private String destination;

	/**
	 * 短信内容
	 */
	private String content;

	/**
	 * 短信发送时间
	 */
	private String time;

	public String getRead() {
		return read;
	}

	public void setRead(String read) {
		this.read = read;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
