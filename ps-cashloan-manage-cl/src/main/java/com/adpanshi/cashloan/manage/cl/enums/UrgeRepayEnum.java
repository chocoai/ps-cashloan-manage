package com.adpanshi.cashloan.manage.cl.enums;

import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * @Description : urgerepay Enum
 * @author huangqin
 * @version 1.0
 * @Date 201701226
 *
 */
public class UrgeRepayEnum {
	/**
	 * 催收状态
	 */
	public enum COLL_STATE {
		TOEBCOLLECTED("11", "待催收"),
		COLLECTING("20", "催收中"),
		COMMITREPAY("30", "承诺还款"),
		COLLSUCCESS("40", "催收成功"),
		BADDEPT("50", "坏账");
		private String EnumKey;
		private String EnumValue;

		COLL_STATE(String EnumKey, String EnumValue) {
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
			COLL_STATE[] enums = values();
			for (COLL_STATE e : enums) {
				outMap.put(e.EnumKey, e.EnumValue);
			}
			return outMap;
		}

		public static COLL_STATE getByEnumKey(String EnumKey) {
			COLL_STATE[] enums = values();
			for (COLL_STATE e : enums) {
				if (e.EnumKey.equals(EnumKey)) {
					return e;
				}
			}
			return null;
		}
	}

	/**
	 * 逾期等级
	 */
	public enum OVERDUE_LEVEL {

		M1("M1", "M1"),
		M2("M2", "M2"),
		M3("M3", "M3");
		private String EnumKey;
		private String EnumValue;

		OVERDUE_LEVEL(String EnumKey, String EnumValue) {
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
			OVERDUE_LEVEL[] enums = values();
			for (OVERDUE_LEVEL e : enums) {
				outMap.put(e.EnumKey, e.EnumValue);
			}
			return outMap;
		}

		public static OVERDUE_LEVEL getByEnumKey(String EnumKey) {
			OVERDUE_LEVEL[] enums = values();
			for (OVERDUE_LEVEL e : enums) {
				if (e.EnumKey.equals(EnumKey)) {
					return e;
				}
			}
			return null;
		}
	}

	/**
	 * 分配状态
	 */
	public enum DESTRIBUTE_STATE {

		ISDESTRIBUTE("10", "已分配"),
		UNDESTRIBUTE("20", "未分配");
		private String EnumKey;
		private String EnumValue;

		DESTRIBUTE_STATE(String EnumKey, String EnumValue) {
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
			DESTRIBUTE_STATE[] enums = values();
			for (DESTRIBUTE_STATE e : enums) {
				outMap.put(e.EnumKey, e.EnumValue);
			}
			return outMap;
		}

		public static DESTRIBUTE_STATE getByEnumKey(String EnumKey) {
			DESTRIBUTE_STATE[] enums = values();
			for (DESTRIBUTE_STATE e : enums) {
				if (e.EnumKey.equals(EnumKey)) {
					return e;
				}
			}
			return null;
		}
	}
}

