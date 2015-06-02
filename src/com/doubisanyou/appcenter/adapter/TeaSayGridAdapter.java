package com.doubisanyou.appcenter.adapter;

import java.util.List;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.widget.ViewHolder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class TeaSayGridAdapter extends ActivityBaseAdapter<String> {

	public TeaSayGridAdapter(Context context, List<String> mDatas) {
		super(context, mDatas);
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 if (convertView == null) {
		        convertView = layoutInflater.inflate(R.layout.griditem_publish_tea_say, parent, false);
		    }
	    ViewHolder.setImageByUrl(convertView, R.id.tea_say_publish_image, mDatas.get(position));
		return convertView;
	}

}
