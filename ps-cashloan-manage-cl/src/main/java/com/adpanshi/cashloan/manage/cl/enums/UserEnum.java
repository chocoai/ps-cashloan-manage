package com.adpanshi.cashloan.manage.cl.enums;

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
public class UserEnum {

	/**
	 * 用户黑名单状态
	 * */
	public enum USER_STATE{
		YES_BLACKLIST               				("10","YES/是"),
		ON_BLACKLIST               					("20","NO/不是");
		private String EnumKey;
		private String EnumValue;
		USER_STATE(String EnumKey, String EnumValue){
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
			USER_STATE[] enums = values();
			for(USER_STATE e:enums){
				outMap.put(e.EnumKey,e.EnumValue);
			}
			return outMap;
		}

		public static USER_STATE getByEnumKey(String EnumKey){
			USER_STATE[] enums = values();
			for(USER_STATE e:enums){
				if(e.EnumKey.equals(EnumKey)){
					return e;
				}
			}
			return null;
		}
	}


	/**
	 * 用户注册客户端
	 * */
	public enum USER_CLIENT{
		H5_CLIENT									("h5","h5"),
		ANDROID_CLIENT								("android","android"),
		IOS_CLIENT									("ios","ios"),
		WX_CLIENT									("wx","wx");

		private String EnumKey;
		private String EnumValue;
		USER_CLIENT(String EnumKey, String EnumValue){
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
			USER_CLIENT[] enums = values();
			for(USER_CLIENT e:enums){
				outMap.put(e.EnumKey,e.EnumValue);
			}
			return outMap;
		}

		public static USER_CLIENT getByEnumKey(String EnumKey){
			USER_CLIENT[] enums = values();
			for(USER_CLIENT e:enums){
				if(e.EnumKey.equals(EnumKey)){
					return e;
				}
			}
			return null;
		}
	}
}

