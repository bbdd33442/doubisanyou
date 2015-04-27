package com.doubisanyou.appcenter.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class ActivityBaseAdaptor<T> extends BaseAdapter {
	
	Context mContext;
	List<T> mDatas;
	LayoutInflater mInflater;
	public ActivityBaseAdaptor(Context context, List<T> mDatas, int itemLayoutId)
	{
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mDatas = mDatas;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}

	@Override
	public T getItem(int i) {
		// TODO Auto-generated method stub
		return mDatas.get(i);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public abstract View getView(int arg0, View arg1, ViewGroup arg2);
	

}
