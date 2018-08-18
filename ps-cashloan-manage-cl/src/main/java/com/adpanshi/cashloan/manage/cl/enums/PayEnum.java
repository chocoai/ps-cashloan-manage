package com.adpanshi.cashloan.manage.cl.enums;

import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * @Description :ManagePayEnum
 * @author huangqin
 * @version 1.0
 * @Date 2018-01-05
 *
 */
public class PayEnum {
	/**
	 * 执行状态
	 * */
	public enum PAY_TYPE{

		SUCCESS               				("1","还款"),
		FAIL               					("2","放款");
		private String EnumKey;
		private String EnumValue;
		PAY_TYPE(String EnumKey, String EnumValue){
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
			PAY_TYPE[] enums = values();
			for(PAY_TYPE e:enums){
				outMap.put(e.EnumKey,e.EnumValue);
			}
			return outMap;
		}

		public static PAY_TYPE getByEnumKey(String EnumKey){
			PAY_TYPE[] enums = values();
			for(PAY_TYPE e:enums){
				if(e.EnumKey.equals(EnumKey)){
					return e;
				}
			}
			return null;
		}
	}

	/**
	 * 支付方式
	 * */
	public enum PAY_LOG_TYPE{

		LOAN               					("10","放款"),
		SHARE               				("20","分润"),
		RETURN               				("30","退还"),
		REPAYMENT               			("40","还款"),
		BUCKLEUP               				("50","补扣");
		private String EnumKey;
		private String EnumValue;
		PAY_LOG_TYPE(String EnumKey, String EnumValue){
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
			PAY_LOG_TYPE[] enums = values();
			for(PAY_LOG_TYPE e:enums){
				outMap.put(e.EnumKey,e.EnumValue);
			}
			return outMap;
		}

		public static PAY_LOG_TYPE getByEnumKey(String EnumKey){
			PAY_LOG_TYPE[] enums = values();
			for(PAY_LOG_TYPE e:enums){
				if(e.EnumKey.equals(EnumKey)){
					return e;
				}
			}
			return null;
		}
	}

	/**
	 * 支付状态
	 * */
	public enum PAY_LOG_STATE{

		TO_BE_PAID               			("10","待支付"),
		TO_BE_VERIFIED              		("15","待审核"),
		VERIFIED_AND_PASS               	("20","审核通过"),
		VERIFIED_AND_REFUSE               	("30","审核不通过"),
		PAY_SUCCESS               			("40","支付成功"),
		PAY_FAIL               			("50","支付失败");
		private String EnumKey;
		private String EnumValue;
		PAY_LOG_STATE(String EnumKey, String EnumValue){
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
			PAY_LOG_STATE[] enums = values();
			for(PAY_LOG_STATE e:enums){
				outMap.put(e.EnumKey,e.EnumValue);
			}
			return outMap;
		}

		public static PAY_LOG_STATE getByEnumKey(String EnumKey){
			PAY_LOG_STATE[] enums = values();
			for(PAY_LOG_STATE e:enums){
				if(e.EnumKey.equals(EnumKey)){
					return e;
				}
			}
			return null;
		}
	}
}

