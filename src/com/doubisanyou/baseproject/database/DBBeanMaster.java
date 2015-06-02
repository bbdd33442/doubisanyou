
package com.doubisanyou.baseproject.database;

/**
 * 用于数据库表的版本管理，用于记录{@link DBBean}子类的版本号
 */
public class DBBeanMaster  extends DBBean {

	public int version = 0;
	
	public String name = "";
}
