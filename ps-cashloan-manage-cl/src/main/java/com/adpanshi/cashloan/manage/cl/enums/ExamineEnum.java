package com.adpanshi.cashloan.manage.cl.enums;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @Description :ManageExamineEnum
 * @author huangqin
 * @version 1.0
 * @Date 2018-01-01
 *
 */
public class ExamineEnum {
	/**
	 * 信审订单状态
	 * */
	public enum EXAMINE_STATE{

		SUCCESS               				("1","启用"),
		FAIL               					("0","禁用");
		private String EnumKey;
		private String EnumValue;
		EXAMINE_STATE(String EnumKey, String EnumValue){
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
			EXAMINE_STATE[] enums = values();
			for(EXAMINE_STATE e:enums){
				outMap.put(e.EnumKey,e.EnumValue);
			}
			return outMap;
		}

		public static EXAMINE_STATE getByEnumKey(String EnumKey){
			EXAMINE_STATE[] enums = values();
			for(EXAMINE_STATE e:enums){
				if(e.EnumKey.equals(EnumKey)){
					return e;
				}
			}
			return null;
		}
	}
}

