package com.doubisanyou.baseproject.utilCommon;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.widget.TextView;

/**
 *  字符串,数字处理函数 包含是些验证 
 *
 */
public class StringAndDataUtil
{
	
    /**
     * 判断是否为null或空值
     *
     * @param str String
     * @return true or false
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断str1和str2是否相同
     *
     * @param str1 str1
     * @param str2 str2
     * @return true or false
     */
    public static boolean equals(String str1, String str2) {
        return str1 == str2 || str1 != null && str1.equals(str2);
    }

    /**
     * 判断str1和str2是否相同(不区分大小写)
     *
     * @param str1 str1
     * @param str2 str2
     * @return true or false
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 != null && str1.equalsIgnoreCase(str2);
    }

    /**
     * 判断字符串str1是否包含字符串str2
     *
     * @param str1 源字符串
     * @param str2 指定字符串
     * @return true源字符串包含指定字符串，false源字符串不包含指定字符串
     */
    public static boolean contains(String str1, String str2) {
        return str1 != null && str1.contains(str2);
    }

    /**
     * 判断字符串是否为空，为空则返回一个空值，不为空则返回原字符串
     *
     * @param str 待判断字符串
     * @return 判断后的字符串
     */
    public static String getString(String str) {
        return str == null ? "" : str;
    }
	
	 
	public static boolean checkInput(String aString,String reg)
	{
		
		if(aString == null || aString.length() == 0)
			return false;
		Pattern pattern = Pattern.compile(reg);
		Matcher isNum = pattern.matcher(aString);
		if( !isNum.matches() )
		{
			return false;
		}
		return true;
	}
   
  
    /**
     * 日期比较
     * @param inputDate
     * @return
     */
    public static  boolean getDateTest(Date inputDate){
		Calendar c = Calendar.getInstance();
		Calendar c1 = Calendar.getInstance();
		
		c.setTime(inputDate);
        c1.setTime(new Date());
        
		int year = c.get(Calendar.YEAR);
		int year_now = c1.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH);
        int month_now = c1.get(Calendar.MONTH);
        boolean flag  = false;
		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
		int dayofMonth_now = c1.get(Calendar.DAY_OF_MONTH);
		if(year_now - year > 18){
			flag = true;
		}
		if(year_now - year == 18){
			if(month_now - month >= 0 ){
				if(dayofMonth_now - dayOfMonth >= 0){
					flag = true;
				}
			}
		}
 		
		return flag;
	}
    
  
    public static double getD(TextView tv){
		double ret = 0;
		if( tv.getText().length() == 0){
			return 0;
		}
		try{
			String str = tv.getText().toString();
			ret = Double.parseDouble(str);
		}catch (Exception e) {
		}
		return ret;
	}
	
	public static int getI(TextView tv){
		int ret = 0;
		if( tv.getText().length() == 0){
			return 0;
		}
		try{
			String str = tv.getText().toString();
			ret = Integer.parseInt(str);
		}catch (Exception e) {
		}
		return ret;
	}
	
	/** 四啥五入，精确到小数点后2位 */
	public static double forShort(double num) {
		BigDecimal   b   =   new   BigDecimal(num); 
		double   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
		return f1;
	}
	/** 四啥五入，精确到小数点后几位 */
	public static double forShort(double num,int newScale ) {
		BigDecimal   b   =   new   BigDecimal(num); 
		double   f1   =   b.setScale(newScale,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
		return f1;
	}
}
