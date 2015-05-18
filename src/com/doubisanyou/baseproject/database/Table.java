
package com.doubisanyou.baseproject.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.database.sqlite.SQLiteDatabase;

/** 指定{@link DBBean}类的对应表信息 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
	/** 对应的表名*/
	public String name() default "";
	
	/** 版本号，如果版本号与数据库中的不对应 ，会调用{@link DBBean#onUpgrade(SQLiteDatabase, int, int)}*/
	public int version() default 0;
}
