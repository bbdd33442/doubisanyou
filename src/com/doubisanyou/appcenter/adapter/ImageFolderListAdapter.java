package com.doubisanyou.appcenter.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.bean.ImageFloder;
import com.doubisanyou.appcenter.widget.ViewHolder;
public class ImageFolderListAdapter extends ActivityBaseAdapter<ImageFloder>{
	
	public ImageFolderListAdapter(List<ImageFloder> folders,Context context){
		super(context,folders);
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		  if (convertView == null) {
		        convertView = layoutInflater.inflate(R.layout.griditem_tea_say_folder_list, parent, false);
		  }
		  ViewHolder.setText(convertView,R.id.dir_item_name, mDatas.get(position).getName());
		  ViewHolder.setImageByUrl(convertView,R.id.dir_item_image,
				  mDatas.get(position).getFirstImagePath());	  
		  ViewHolder .setText(convertView,R.id.dir_item_count, mDatas.get(position).getCount() + "张");
		return convertView;
	}

}
