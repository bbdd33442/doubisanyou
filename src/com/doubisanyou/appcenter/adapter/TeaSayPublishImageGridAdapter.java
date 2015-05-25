package com.doubisanyou.appcenter.adapter;

import java.util.List;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.widget.ViewHolder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TeaSayPublishImageGridAdapter extends ActivityBaseAdapter<String>{

	public TeaSayPublishImageGridAdapter(Context context, List<String> mDatas) {
		super(context, mDatas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
	        convertView = layoutInflater.inflate(R.layout.griditem_publish_tea_say, parent, false);
	    }
		//判斷如果当前位置为列表最后一個，并且路径是为添加图片的ID那么就显示添加图片的图标，否则显示图片
		if((mDatas.get(position).equals(String.valueOf(R.drawable.tea_say_publish_add_image)))){
			ViewHolder.setImageResource(convertView, R.id.tea_say_publish_image, R.drawable.tea_say_publish_add_image);
		}else{
			ViewHolder.setImageByUrl(convertView, R.id.tea_say_publish_image,mDatas.get(position));
		}
		return convertView;
	}

}
