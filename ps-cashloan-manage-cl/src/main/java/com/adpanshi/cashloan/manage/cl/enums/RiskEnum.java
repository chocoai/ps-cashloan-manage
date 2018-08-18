package com.adpanshi.cashloan.manage.cl.enums;

import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * @Description : ManageRiskEnum
 * @author nmnl
 * @version 1.1
 * @Date 201701228
 *
 */
public class RiskEnum {

	/**
	 * 风控:支持
	 * */
	public enum DICTIONARY_TYPE{
		ONE               				("1","复审字典"),
		TWO               				("2","通讯录字典"),
		THREE               			("3","催收号字典"),
		FOUR               				("4","省份字典");
		private String EnumKey;
		private String EnumValue;
		DICTIONARY_TYPE(String EnumKey, String EnumValue){
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
			DICTIONARY_TYPE[] enums = values();
			for(DICTIONARY_TYPE e:enums){
				outMap.put(e.EnumKey,e.EnumValue);
			}
			return outMap;
		}

		public static DICTIONARY_TYPE getByEnumKey(String EnumKey){
			DICTIONARY_TYPE[] enums = values();
			for(DICTIONARY_TYPE e:enums){
				if(e.EnumKey.equals(EnumKey)){
					return e;
				}
			}
			return null;
		}
	}

	/**
	 * 复审字典:复审类型
	 * */
	public enum AUDITING_TYPE{
		AUDITING_HANGUP               				("25","复审挂起"),
		AUDITING_NO               					("26","复审通过"),
		AUDITING_YES               					("27","复审拒绝");
		private String EnumKey;
		private String EnumValue;
		AUDITING_TYPE(String EnumKey, String EnumValue){
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
			AUDITING_TYPE[] enums = values();
			for(AUDITING_TYPE e:enums){
				outMap.put(e.EnumKey,e.EnumValue);
			}
			return outMap;
		}

		public static AUDITING_TYPE getByEnumKey(String EnumKey){
			AUDITING_TYPE[] enums = values();
			for(AUDITING_TYPE e:enums){
				if(e.EnumKey.equals(EnumKey)){
					return e;
				}
			}
			return null;
		}
	}

	/**
	 * 复审字典:状态
	 * */
	public enum AUDITING_STATE{
		AUDITING_YES              				("10","启用"),
		AUDITING_NO               				("20","禁用");
		private String EnumKey;
		private String EnumValue;
		AUDITING_STATE(String EnumKey, String EnumValue){
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
			AUDITING_STATE[] enums = values();
			for(AUDITING_STATE e:enums){
				outMap.put(e.EnumKey,e.EnumValue);
			}
			return outMap;
		}

		public static AUDITING_STATE getByEnumKey(String EnumKey){
			AUDITING_STATE[] enums = values();
			for(AUDITING_STATE e:enums){
				if(e.EnumKey.equals(EnumKey)){
					return e;
				}
			}
			return null;
		}
	}


	/**
	 * 通讯录字典:类型
	 * */
	public enum CONTACT_TYPE{
		ONE              				("1","亲属"),
		TWO               				("2","专线");
		private String EnumKey;
		private String EnumValue;
		CONTACT_TYPE(String EnumKey, String EnumValue){
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
			CONTACT_TYPE[] enums = values();
			for(CONTACT_TYPE e:enums){
				outMap.put(e.EnumKey,e.EnumValue);
			}
			return outMap;
		}

		public static CONTACT_TYPE getByEnumKey(String EnumKey){
			CONTACT_TYPE[] enums = values();
			for(CONTACT_TYPE e:enums){
				if(e.EnumKey.equals(EnumKey)){
					return e;
				}
			}
			return null;
		}
	}
	/**
	 * 复审字典:状态
	 * */
	public enum PROVINCE_STATE{
		AUDITING_YES              				("1","启用"),
		AUDITING_NO               				("0","禁用");
		private String EnumKey;
		private String EnumValue;
		PROVINCE_STATE(String EnumKey, String EnumValue){
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
			PROVINCE_STATE[] enums = values();
			for(PROVINCE_STATE e:enums){
				outMap.put(e.EnumKey,e.EnumValue);
			}
			return outMap;
		}

		public static PROVINCE_STATE getByEnumKey(String EnumKey){
			PROVINCE_STATE[] enums = values();
			for(PROVINCE_STATE e:enums){
				if(e.EnumKey.equals(EnumKey)){
					return e;
				}
			}
			return null;
		}
	}

}

