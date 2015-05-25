
package com.doubisanyou.baseproject.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * 用于{@link DBBean} 类的成员变量，指定成员变量对应数据库表的列名及是否主键}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
	/** 与{@link DBBean} 类的成员变量对应的列名*/
	String name() default "";
	/** 指定{@link DBBean} 类的成员变量对应的列,是否为主键*/
	boolean primaryKey() default false;
}
