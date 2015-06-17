package com.doubisanyou.baseproject.utilCommon;
 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date; 

/**
 *  时间处理函数  
 */
public class DateUtil
{   
	//FIXME 此为常用日期格式，如有特殊需要请自行修改
	static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 将字符串转换为日期
	 */
	public static Date getTimeStemp(String str){
        Date d = null;
        try {
            d = dateformat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }
 
    /**
     * 日期比大小
     * @param DATE1 当前日期
     * @param DATE2 选择日期
     * @return
     */
    public static boolean compare_date(String DATE1, String DATE2) {
        
        try {
            Date dt1 = dateformat.parse(DATE1);
            Date dt2 = dateformat.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return true;
            } else if (dt1.getTime() < dt2.getTime()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * 字符串转化为日期
     * @param date
     * @return
     */
    public static Date strToDate(String date){
		Date t;
		try {
			t = dateformat.parse(date);
			return t;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
    
	/**
	 * 得到当前日期
	 * @return
	 */
	public static String getCurDate(){
		Calendar  date	  = Calendar.getInstance();
		String curDate	 =  dateformat.format( date.getTime());
		return curDate;
	} 

	/**
	 * 当前日期格式化输出
	 * @param pattern
	 * @return
	 */
	public static String getDateFormat(String pattern) {
		if (pattern == null)
			pattern = "yyyy-MM-dd HH:mm";
		long currentTime = System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		Date date = new Date(currentTime); 
		return formatter.format(date);
	}  
}
