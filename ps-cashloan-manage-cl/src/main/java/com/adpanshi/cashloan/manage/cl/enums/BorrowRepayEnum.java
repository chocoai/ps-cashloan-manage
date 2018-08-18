package com.adpanshi.cashloan.manage.cl.enums;

import com.adpanshi.cashloan.common.enums.ICommonEnum;

import java.util.Map;
import java.util.TreeMap;

/***
 ** @category 请用一句话来描述其用途...
 ** @author qing.yunhui
 ** @email: 280672161@qq.com
 ** @createTime: 2017年12月25日下午10:48:12
 **/
public class BorrowRepayEnum {

	/**还款方式*/
	public enum REPAY_WAY implements ICommonEnum {
		REPAY_WAY_CHARGE("10","代扣"),
		REPAY_WAY_TRANSFER("20","银行卡转账"),
		REPAY_WAY_ALIPAY_TRANSFER("30","支付宝转账"),
		REPAY_WAY_ACTIVE_TRANSFER("40","主动还款");
			
	    private final String code;
	    private final String name;
	    
	    REPAY_WAY( String code,String name) {
	        this.code = code;
	        this.name = name;
	    }
		@Override
		public String getCode() {
			return code;
		}
		@Override
		public String getName() {
			return name;
		}
		@Override
		public Integer getKey() {
			return Integer.parseInt(code);
		}

		public static Map<String,Object> EnumValueS(){
			Map<String,Object> outMap = new TreeMap<>();
			REPAY_WAY[] enums = values();
			for(REPAY_WAY e:enums){
				outMap.put(e.code,e.name);
			}
			return outMap;
		}

		public static REPAY_WAY getByEnumKey(String EnumKey){
			REPAY_WAY[] enums = values();
			for(REPAY_WAY e:enums){
				if(e.code.equals(EnumKey)){
					return e;
				}
			}
			return null;
		}
	}

	/**
	 * 还款状态
	 * */
	public enum REPAY_STATE{
		NOTICED             				("10","已还款"),
		NONOTICED               			("20","未还款");
		private String EnumKey;
		private String EnumValue;
		REPAY_STATE(String EnumKey, String EnumValue){
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
			REPAY_STATE[] enums = values();
			for(REPAY_STATE e:enums){
				outMap.put(e.EnumKey,e.EnumValue);
			}
			return outMap;
		}

		public static REPAY_STATE getByEnumKey(String EnumKey){
			REPAY_STATE[] enums = values();
			for(REPAY_STATE e:enums){
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
	public enum REPAY_BORROW_STATE {

		STATE_REPAY("30", "放款成功"),
		STATE_FINISH("40", "还款成功"),
		STATE_REMISSION_FINISH("41", "还款成功-金额减免"),
		STATE_DELAY("50", "逾期"),
		STATE_BAD("90", "坏账");

		private String EnumKey;
		private String EnumValue;

		REPAY_BORROW_STATE(String EnumKey, String EnumValue) {
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
			REPAY_BORROW_STATE[] enums = values();
			for (REPAY_BORROW_STATE e : enums) {
				outMap.put(e.EnumKey, e.EnumValue);
			}
			return outMap;
		}

		public static Long[] EnumKeyS() {
			REPAY_BORROW_STATE[] enums = values();
			Long[] outLongs = new Long[enums.length];
			int i = 0;
			for (REPAY_BORROW_STATE e : enums) {

				outLongs[i] = Long.valueOf(e.EnumKey);
				i++;
			}
			return outLongs;
		}
	}
	
}
