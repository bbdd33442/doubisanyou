package com.doubisanyou.appcenter.adapter;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.widget.ViewHolder;
public class ImageListAdapter extends ActivityBaseAdapter<String>{
	
	List<String> selectedImagePath;
	String dir;
	Activity ac;
	Handler mHandler;
	int canSelcetNum;
	public ImageListAdapter(Context context,List<String> imageNames,
			  String dir,List<String> selectedImagePath,Activity ac,Handler mHandler,int canSelcetNum){
		super(context,imageNames);
		this.dir = dir;
		this.selectedImagePath = selectedImagePath;
		this.ac = ac;
		this.mHandler = mHandler;
		this.canSelcetNum = canSelcetNum;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		  if (convertView == null) {
		        convertView = layoutInflater.inflate(R.layout.griditem_tea_say_publish_image_view, parent, false);
		  }
		    final String imageName = mDatas.get(position);
			ViewHolder.setImageResource(convertView,R.id.item_select,
					R.drawable.picture_unselected);
		    ViewHolder.setImageByUrl(convertView,R.id.item_image, dir + "/" + imageName);
			final ImageView mImageView = ViewHolder.get(convertView,R.id.item_image);
			final ImageView mSelect = ViewHolder.get(convertView,R.id.item_select);
		
			mImageView.setColorFilter(null);
			//设置ImageView的点击事件
			mImageView.setOnClickListener(new OnClickListener()
			{
				//选择，则将图片变暗，反之则反之
				@Override
				public void onClick(View v)
				{   //&&!selectedImagePath.contains(dir + "/" + imageName)
					if(selectedImagePath.size()==canSelcetNum){
						Builder builder = new Builder(ac);
						builder.setMessage("照片不能超过"+canSelcetNum+"张！");
						builder.setTitle("提示");
						builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {						
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						builder.create().show();
						return;
					}
					// 已经选择过该图片
					if (selectedImagePath.contains(dir + "/" + imageName))
					{
						selectedImagePath.remove(dir + "/" + imageName);
						mSelect.setImageResource(R.drawable.picture_unselected);
						mImageView.setColorFilter(null);
						mHandler.sendEmptyMessage(1);
					} else
					// 未选择该图片
					{
						selectedImagePath.add(dir + "/" + imageName);
						mSelect.setImageResource(R.drawable.pictures_selected);
						mImageView.setColorFilter(Color.parseColor("#77000000"));
						mHandler.sendEmptyMessage(1);
					}
				}
			});
			
			/**
			 * 已经选择过的图片，显示出选择过的效果
			 */
			if (selectedImagePath.contains(dir + "/" + imageName))
			{
				mSelect.setImageResource(R.drawable.pictures_selected);
				mImageView.setColorFilter(Color.parseColor("#77000000"));
			}
		  
		 
		return convertView;
	}

}
