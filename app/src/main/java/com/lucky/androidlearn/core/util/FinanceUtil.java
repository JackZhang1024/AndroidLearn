package com.lucky.androidlearn.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FinanceUtil {
	/**
	 * 获取截取指定小数点后几位的单精度数值
	 * @param number digits 是确定截取小数点后几位
	 * @return
	 */
	public static float getDefinedNumber(String number,int digits){
		BigDecimal bd=new BigDecimal(number);
		return bd.setScale(digits,RoundingMode.DOWN).floatValue();
	}
}
