package com.adpanshi.cashloan.manage.cl.enums;

import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * @Description : ManageOpinionEnum Enum
 * @author nmnl
 * @version 1.1
 * @Date 201701206
 *
 */
public class OpinionEnum {

	/**
	 * Manage_Opinion
	 * 10待确认，20已确认
	 * */
	public enum OPINION_STATE{
		YES_OPINION               				("10","待确认"),
		ON_OPINION               				("20","已确认");
		private String EnumKey;
		private String EnumValue;
		OPINION_STATE(String EnumKey, String EnumValue){
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
			OPINION_STATE[] enums = values();
			for(OPINION_STATE e:enums){
				outMap.put(e.EnumKey,e.EnumValue);
			}
			return outMap;
		}

		public static OPINION_STATE getByEnumKey(String EnumKey){
			OPINION_STATE[] enums = values();
			for(OPINION_STATE e:enums){
				if(e.EnumKey.equals(EnumKey)){
					return e;
				}
			}
			return null;
		}
	}

}

