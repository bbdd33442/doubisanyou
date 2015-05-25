package com.doubisanyou.baseproject.base;

import android.content.Context;
import android.os.Handler;

/** 
 */
public interface BaseActivityInterface {

	/**
	 * 得到该Activity绑定的Handler类
	 * 
	 * @return
	 */
	public Handler getHandler();

	/**
	 * 得到上下文环境
	 * 
	 * @return
	 */
	public Context getContext();
}
