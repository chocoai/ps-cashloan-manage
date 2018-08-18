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
public class SysRoleEnum {
	/**
	 * 角色状态
	 */
	public enum ROLE_STATE {

		IS_DELETE("0", "启用"),
		NOT_DELETE("1", "停用");
		private String EnumKey;
		private String EnumValue;

		ROLE_STATE(String EnumKey, String EnumValue) {
			this.EnumKey = EnumKey;
			this.EnumValue = EnumValue;
		}

		public String EnumKey() {
			return EnumKey;
		}

		public String EnumValue() {
			return EnumValue;
		}

		public static Map<String, Object> EnumValueS() {
			Map<String, Object> outMap = new TreeMap<>();
			ROLE_STATE[] enums = values();
			for (ROLE_STATE e : enums) {
				outMap.put(e.EnumKey, e.EnumValue);
			}
			return outMap;
		}

		public static ROLE_STATE getByEnumKey(String EnumKey) {
			ROLE_STATE[] enums = values();
			for (ROLE_STATE e : enums) {
				if (e.EnumKey.equals(EnumKey)) {
					return e;
				}
			}
			return null;
		}
	}
}

