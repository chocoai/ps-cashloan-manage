package com.adpanshi.cashloan.manage.cl.enums;

import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * @Description :ManageQuartzEnum
 * @author huangqin
 * @version 1.0
 * @Date 2018-01-01
 *
 */
public class QuartzEnum {
	/**
	 * 执行状态
	 * */
	public enum EXCUTE_RESULT{

		SUCCESS               				("10","执行成功"),
		FAIL               					("20","执行失败");
		private String EnumKey;
		private String EnumValue;
		EXCUTE_RESULT(String EnumKey, String EnumValue){
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
			EXCUTE_RESULT[] enums = values();
			for(EXCUTE_RESULT e:enums){
				outMap.put(e.EnumKey,e.EnumValue);
			}
			return outMap;
		}

		public static EXCUTE_RESULT getByEnumKey(String EnumKey){
			EXCUTE_RESULT[] enums = values();
			for(EXCUTE_RESULT e:enums){
				if(e.EnumKey.equals(EnumKey)){
					return e;
				}
			}
			return null;
		}
	}
}

