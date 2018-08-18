package com.adpanshi.cashloan.manage.arc.enums;

import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * @Description : manageuser Enum
 * @author nmnl
 * @version 1.1
 * @Date 201701206
 *
 */
public class SysUserEnum {
	/**
	 * 角色状态
	 * */
	public enum SYS_USER_STATE{

		UNUSED               				("0","启用"),
		USED               					("1","禁用");
		private String EnumKey;
		private String EnumValue;
		SYS_USER_STATE(String EnumKey, String EnumValue){
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
			SYS_USER_STATE[] enums = values();
			for(SYS_USER_STATE e:enums){
				outMap.put(e.EnumKey,e.EnumValue);
			}
			return outMap;
		}

		public static SYS_USER_STATE getByEnumKey(String EnumKey){
			SYS_USER_STATE[] enums = values();
			for(SYS_USER_STATE e:enums){
				if(e.EnumKey.equals(EnumKey)){
					return e;
				}
			}
			return null;
		}
	}
}

