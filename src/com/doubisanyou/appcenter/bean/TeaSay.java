package com.doubisanyou.appcenter.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class TeaSay implements Serializable{
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6644748378394344291L;
	
	public String tea_say_publisher_name;
	public String tea_say_content;
	public String tea_say_publisher_id;
	public String tea_say_publish_date;
	public String tea_say_time;
	public String tea_say_publisher_avatar;
	public List<String> tea_say_images = new ArrayList<String>();
		
}
