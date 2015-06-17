package com.doubisanyou.baseproject.utilsInputVerify;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 界面校验规则 
 */
public class CheckPageInputUtil {
	
	/**
	 * @param aString
	 * @return
	 */
	public static boolean isPFloatNumeric(String aString){
		if(aString == null || aString.length() == 0)
			return false;
		Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?$");
		Matcher isPFloat = pattern.matcher(aString);
		if(isPFloat.matches()){
			return true;
		}
		return false;
	}
	/**
	 * @param aString
	 * @return
	 */
	public static boolean isPNumeric(String aString){
		if(aString == null || aString.length() == 0)
			return false;
		Pattern pattern = Pattern.compile("^\\d+$");
		Matcher isPNum = pattern.matcher(aString);
		if(isPNum.matches()){
			return true;
		}
		return false;
	}
	
	/** 
	 * @param aString
	 * @return
	 */
	public static boolean isNumeric(String aString)
	{
		if(aString == null || aString.length() == 0)
			return false;
		Pattern pattern = Pattern.compile("^-?\\d+$");
		Matcher isNum = pattern.matcher(aString);
		if(isNum.matches() )
		{
			return true;
		}
		return false;
	}
	/**
	 * @param aString
	 * @return
	 */
	public static boolean isNumericPhone(String aString)
	{
		if(aString == null || aString.length() == 0)
			return false;
		Pattern pattern = Pattern.compile("^[1][3-8]+\\d{9}");
		Matcher isNum = pattern.matcher(aString);
		if( !isNum.matches() )
		{
			return false;
		}
		return true;
	}

	  
	public static boolean isPostCode(String input) {
		String strPattern = "^[1-9][0-9]{5}";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(input);
		return m.matches();
	}
	
	/**
	 * 验证字符串是否是email
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str) {
		String strPattern = "[\\w[.-]]+@[\\w[.-]]+\\.[\\w]+";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 验证姓名 名 1-4个汉字，不能少于1个，不能超过4个。
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isPersonName(String input) {
		Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]*$");
		Matcher matcher = pattern.matcher(input);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	/** 
	 * 检测输入的用户名不可以为非法字符
	 * @param aString
	 * @return
	 */
	public static boolean checkInputName(String aString)
	{
		if(aString == null || aString.length() == 0)
			return false;
		Pattern pattern = Pattern.compile("^[A-Za-z.\\u4e00-\\u9fa5]*$");
		Matcher isNum = pattern.matcher(aString);
		if( !isNum.matches() )
		{
			return false;
		}
		return true;
	}
	

	/**
	 * 验证是否是手机号码 130-139 140-149 150-153,155-159 180-183,185-189
	 * 
	 * @param str
	 * @return
	 */

	public static boolean isCellphone(String str) {
		// Pattern pattern = Pattern.compile("1[0-9]{10}");
		Pattern pattern = Pattern
				.compile("^(13[0-9]|14[0-9]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		} 
	}

 
  

	/**
	 * 检查日期格式
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static boolean checkDate(String year, String month, String day) {
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd");
		try {
			String s3 = year + month + day;
			simpledateformat.setLenient(false);
			simpledateformat.parse(s3);
		} catch (java.text.ParseException parseexception) {
			return false;
		}
		return true;
	}
 

	/**
	 * 检查字符串是否全为数字 
	 * @param certiCode
	 * @return
	 */
	public static boolean checkFigure(String certiCode) {
		try {
			Long.parseLong(certiCode);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
 
}
