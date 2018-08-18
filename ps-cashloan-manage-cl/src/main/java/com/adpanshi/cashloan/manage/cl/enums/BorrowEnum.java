package com.adpanshi.cashloan.manage.cl.enums;

import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * @Description : BorrowEnum
 * @author nmnl
 * @version 1.1
 * @Date 201701219
 *
 */
public class BorrowEnum {

	/**
	 * 人审订单状态
	 * */
	public enum AUDITING_BORROW_STATE{
		ARTIFICIAL_HANG							("25","复审挂起"),
		ARTIFICIAL_YES							("26","复审通过"),
		ARTIFICIAL_NO							("27","复审拒绝");
		private String EnumKey;
		private String EnumValue;
		AUDITING_BORROW_STATE(String EnumKey, String EnumValue){
			this.EnumKey = EnumKey;
			this.EnumValue = EnumValue;
		}
		public String EnumKey(){
			return EnumKey;
		}

		public String EnumValue(){
			return EnumValue;
		}

		public static Map<String,Object> EnumValueS(){
			Map<String,Object> outMap = new TreeMap<>();
			AUDITING_BORROW_STATE[] enums = values();
			for(AUDITING_BORROW_STATE e:enums){
				outMap.put(e.EnumKey,e.EnumValue);
			}
			return outMap;
		}

		public static AUDITING_BORROW_STATE getByEnumKey(String EnumKey){
			AUDITING_BORROW_STATE[] enums = values();
			for(AUDITING_BORROW_STATE e:enums){
				if(e.EnumKey.equals(EnumKey)){
					return e;
				}
			}
			return null;
		}
	}

	/**
	 * 订单状态
	 * */
	public enum BORROW_STATE{

		WAIT_ARTIFICIAL_REVIEW             		("22","待复审"),
		ARTIFICIAL_HANG							("25","复审挂起"),
		ARTIFICIAL_YES							("26","复审通过"),
		ARTIFICIAL_NO							("27","复审拒绝");
		private String EnumKey;
		private String EnumValue;
		BORROW_STATE(String EnumKey, String EnumValue){
			this.EnumKey = EnumKey;
			this.EnumValue = EnumValue;
		}
		public String EnumKey(){
			return EnumKey;
		}

		public String EnumValue(){
			return EnumValue;
		}

		public static Map<String,Object> EnumValueS(){
			Map<String,Object> outMap = new TreeMap<>();
			BORROW_STATE[] enums = values();
			for(BORROW_STATE e:enums){
				outMap.put(e.EnumKey,e.EnumValue);
			}
			return outMap;
		}

		public static BORROW_STATE getByEnumKey(String EnumKey){
			BORROW_STATE[] enums = values();
			for(BORROW_STATE e:enums){
				if(e.EnumKey.equals(EnumKey)){
					return e;
				}
			}
			return null;
		}
	}

	/**
	 * borrow风控结果
	 * */
	public enum BORROW_RISK_STATE{

		ACCEPT             				("Accept","建议通过"),
		REVIEW      					("Review","建议审核"),
		REJECT							("Reject","建议拒绝");
		private String EnumKey;
		private String EnumValue;
		BORROW_RISK_STATE(String EnumKey, String EnumValue){
			this.EnumKey = EnumKey;
			this.EnumValue = EnumValue;
		}
		public String EnumKey(){
			return EnumKey;
		}

		public String EnumValue(){
			return EnumValue;
		}

		public static Map<String,Object> EnumValueS(){
			Map<String,Object> outMap = new TreeMap<>();
			BORROW_RISK_STATE[] enums = values();
			for(BORROW_RISK_STATE e:enums){
				outMap.put(e.EnumKey,e.EnumValue);
			}
			return outMap;
		}

		public static BORROW_RISK_STATE getByEnumKey(String EnumKey){
			BORROW_RISK_STATE[] enums = values();
			for(BORROW_RISK_STATE e:enums){
				if(e.EnumKey.equals(EnumKey)){
					return e;
				}
			}
			return null;
		}
	}

	/**
	 * borrow审核类型
	 * */
	public enum BORROW_AUDITING_TYPE{

		AUDITING_WAIT             				("10","待机审"),
		AUDITING_NO_ONE      					("21","机审拒绝"),
		AUDITING_YES_ONE						("20","机审通过"),
		AUDITING_YES_TWO             			("26","人审通过"),
		AUDITING_NO_TWO							("27","人审拒绝");
		private String EnumKey;
		private String EnumValue;
		BORROW_AUDITING_TYPE(String EnumKey, String EnumValue){
			this.EnumKey = EnumKey;
			this.EnumValue = EnumValue;
		}
		public String EnumKey(){
			return EnumKey;
		}

		public String EnumValue(){
			return EnumValue;
		}

		public static Map<String,Object> EnumValueS(){
			Map<String,Object> outMap = new TreeMap<>();
			BORROW_AUDITING_TYPE[] enums = values();
			for(BORROW_AUDITING_TYPE e:enums){
				outMap.put(e.EnumKey,e.EnumValue);
			}
			return outMap;
		}

		public static BORROW_AUDITING_TYPE getByEnumKey(String EnumKey){
			BORROW_AUDITING_TYPE[] enums = values();
			for(BORROW_AUDITING_TYPE e:enums){
				if(e.EnumKey.equals(EnumKey)){
					return e;
				}
			}
			return null;
		}
	}

	/**
	 * borrow审核类型
	 * */
	public enum BORROW_AUTO_TYPE{

		AUDITING_WAIT             				("10","待机审"),
		AUDITING_NO_ONE      					("21","机审拒绝"),
		AUDITING_YES_ONE						("20","机审通过");

		private String EnumKey;
		private String EnumValue;
		BORROW_AUTO_TYPE(String EnumKey, String EnumValue){
			this.EnumKey = EnumKey;
			this.EnumValue = EnumValue;
		}
		public String EnumKey(){
			return EnumKey;
		}

		public String EnumValue(){
			return EnumValue;
		}

		public static Map<String,Object> EnumValueS(){
			Map<String,Object> outMap = new TreeMap<>();
			BORROW_AUTO_TYPE[] enums = values();
			for(BORROW_AUTO_TYPE e:enums){
				outMap.put(e.EnumKey,e.EnumValue);
			}
			return outMap;
		}

		public static BORROW_AUTO_TYPE getByEnumKey(String EnumKey){
			BORROW_AUTO_TYPE[] enums = values();
			for(BORROW_AUTO_TYPE e:enums){
				if(e.EnumKey.equals(EnumKey)){
					return e;
				}
			}
			return null;
		}
	}

	/**
	 * 订单状态
	 * */
	public enum ALL_BORROW_STATE{

		STATE_PRE             				("10","申请成功待审核"),
//		STATE_TEMPORARY_AUTO_PASS           ("12","待活体自动放款"),
//		STATE_TEMPORARY_NEED_REVIEW         ("14","待活体自动人审"),
		STATE_AUTO_PASS      				("20","自动审核通过"),
		STATE_AUTO_REFUSED					("21","自动审核不通过"),
		STATE_NEED_REVIEW					("22","自动审核未决待人工复审"),
		STATE_HANGUP						("25","人工复审挂起"),
		STATE_PASS							("26","人工复审通过"),
		STATE_REFUSED						("27","人工复审不通过"),
//		STATE_WAIT_PASS						("28","审核成功(待银程同意)"),
//		STATE_WAIT_REPAY					("29","银程同意(待连连放款)"),
		STATE_REPAY							("30","放款成功"),
		STATE_REPAY_FAIL					("31","放款失败"),
		STATE_FINISH						("40","还款成功"),
		STATE_REMISSION_FINISH				("41","还款成功-金额减免"),
		STATE_DELAY							("50","逾期"),
		STATE_BAD							("90","坏账");

		private String EnumKey;
		private String EnumValue;
		ALL_BORROW_STATE(String EnumKey, String EnumValue){
			this.EnumKey = EnumKey;
			this.EnumValue = EnumValue;
		}
		public String EnumKey(){
			return EnumKey;
		}

		public String EnumValue(){
			return EnumValue;
		}

		public static Map<String,Object> EnumValueS(){
			Map<String,Object> outMap = new TreeMap<>();
			ALL_BORROW_STATE[] enums = values();
			for(ALL_BORROW_STATE e:enums){
				outMap.put(e.EnumKey,e.EnumValue);
			}
			return outMap;
		}

		public static Long[] EnumKeyS(){
			ALL_BORROW_STATE[] enums = values();
			Long [] outLongs = new Long[enums.length];
			int i = 0;
			for(ALL_BORROW_STATE e:enums){

				outLongs[i] = Long.valueOf(e.EnumKey);
				i++;
			}
			return outLongs;
		}

		public static ALL_BORROW_STATE getByEnumKey(String EnumKey){
			ALL_BORROW_STATE[] enums = values();
			for(ALL_BORROW_STATE e:enums){
				if(e.EnumKey.equals(EnumKey)){
					return e;
				}
			}
			return null;
		}
	}

	/**
	 * 订单状态
	 * */
	public enum REFUND_BORROW_STATE{
		STATE_AUTO_PASS      				("20","机审通过"),
		STATE_PASS							("26","人工复审通过"),
		STATE_REPAY							("30","放款成功"),
		STATE_REPAY_FAIL					("31","放款失败"),
		STATE_FINISH						("40","还款成功"),
		STATE_REMISSION_FINISH				("41","还款成功-金额减免"),
		STATE_DELAY							("50","逾期"),
		STATE_BAD							("90","坏账");

		private String EnumKey;
		private String EnumValue;
		REFUND_BORROW_STATE(String EnumKey, String EnumValue){
			this.EnumKey = EnumKey;
			this.EnumValue = EnumValue;
		}
		public String EnumKey(){
			return EnumKey;
		}

		public String EnumValue(){
			return EnumValue;
		}

		public static Map<String,Object> EnumValueS(){
			Map<String,Object> outMap = new TreeMap<>();
			REFUND_BORROW_STATE[] enums = values();
			for(REFUND_BORROW_STATE e:enums){
				outMap.put(e.EnumKey,e.EnumValue);
			}
			return outMap;
		}

		public static Long[] EnumKeyS(){
			REFUND_BORROW_STATE[] enums = values();
			Long [] outLongs = new Long[enums.length];
			int i = 0;
			for(REFUND_BORROW_STATE e:enums){

				outLongs[i] = Long.valueOf(e.EnumKey);
				i++;
			}
			return outLongs;
		}

		public static REFUND_BORROW_STATE getByEnumKey(String EnumKey){
			REFUND_BORROW_STATE[] enums = values();
			for(REFUND_BORROW_STATE e:enums){
				if(e.EnumKey.equals(EnumKey)){
					return e;
				}
			}
			return null;
		}
	}
	/**
	 * 借款详情 -> 限制显示内容.
	 * */
	public enum LOAN_INFO_TYPE{
		USER_MANAGE							("1","用户管理"),
		REVIEW_MANAGE_EDIT             		("20","复审管理->人工复审"),
		REVIEW_MANAGE_INFO             		("21","复审管理->复审详情"),
		BORROW_MANAGE           			("3","借款管理"),
		BORROW_AFTER_MANAGE         		("4","贷后管理"),
		COLLECTION_MANAGE      				("5","催收管理");

		private String EnumKey;
		private String EnumValue;
		LOAN_INFO_TYPE(String EnumKey, String EnumValue){
			this.EnumKey = EnumKey;
			this.EnumValue = EnumValue;
		}
		public String EnumKey(){
			return EnumKey;
		}

		public String EnumValue(){
			return EnumValue;
		}

		public static Map<String,Object> EnumValueS(){
			Map<String,Object> outMap = new TreeMap<>();
			LOAN_INFO_TYPE[] enums = values();
			for(LOAN_INFO_TYPE e:enums){
				outMap.put(e.EnumKey,e.EnumValue);
			}
			return outMap;
		}

		public static LOAN_INFO_TYPE getByEnumKey(String EnumKey){
			LOAN_INFO_TYPE[] enums = values();
			for(LOAN_INFO_TYPE e:enums){
				if(e.EnumKey.equals(EnumKey)){
					return e;
				}
			}
			return null;
		}
	}
	
}

