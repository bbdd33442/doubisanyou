package com.doubisanyou.appcenter.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class ActivityBaseAdapter<T> extends BaseAdapter {
	
	Context mContext;
	List<T> mDatas;
	LayoutInflater layoutInflater;
	public ActivityBaseAdapter(Context context, List<T> mDatas)
	{
		this.mContext = context;
		this.layoutInflater = LayoutInflater.from(mContext);
		this.mDatas = mDatas;
	}
	
	@Override
	public int getCount() {
		if (mDatas == null||mDatas.size()==0) {
			return 0;
		}
		return mDatas.size();
	}

	@Override
	public T getItem(int i) {
		if (mDatas == null||mDatas.size()==0) {
			return null;
		}
		return mDatas.get(i);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public abstract View getView(int position, View convertView, ViewGroup parent);
	

}
