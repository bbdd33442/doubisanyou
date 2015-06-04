package com.doubisanyou.appcenter.adapter;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.activity.TeaSayReplyActivity;
import com.doubisanyou.appcenter.bean.TeaSay;
import com.doubisanyou.appcenter.widget.BadgeView;
import com.doubisanyou.appcenter.widget.ViewHolder;
import com.doubisanyou.baseproject.utilCommon.StringAndDataUtil;
import com.doubisanyou.baseproject.utilCommon.TimeUtil;

public class TeaSayAdapter extends ActivityBaseAdapter<TeaSay> {
    
	private Activity ac;
	private Handler handler;
	public TeaSayAdapter(Context context, List<TeaSay> tys,Activity ac,Handler handler) {
		super(context, tys);
		this.ac = ac;
		this.handler = handler;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

	    if (convertView == null) {
	        convertView = layoutInflater.inflate(R.layout.listitem_tea_say, parent, false);
	    }
	  
	     final TeaSay ts = (TeaSay) getItem(position);
	     TextView tea_say_publisher_name = ViewHolder.get(convertView, R.id.tea_say_publisher_name);
	     tea_say_publisher_name.setText(ts.tea_say_publisher_name);
		 TextView  tea_say_content = ViewHolder.get(convertView, R.id.tea_say_content);
		 tea_say_content.setText(ts.tea_say_content);
		 TextView tea_say_publisher_id = ViewHolder.get(convertView, R.id.tea_say_publisher_id);
		 tea_say_publisher_id.setText(ts.tea_say_publisher_id);
		 TextView tea_say_publish_date = ViewHolder.get(convertView, R.id.tea_say_publish_date);
		 tea_say_publish_date.setText(ts.tea_say_publish_date);
		 TextView tea_say_time = ViewHolder.get(convertView, R.id.tea_say_time);
		 tea_say_time.setText(TimeUtil.getDescriptionTimeFromTimestamp(Long.parseLong(ts.tea_say_time)));
		 ImageView teasay_publisher_avatar = ViewHolder.get(convertView, R.id.tea_say_publisher_avatar);
		 teasay_publisher_avatar.setImageResource(R.drawable.csu);
		 ImageView tea_say_image = ViewHolder.get(convertView, R.id.tea_say_image);
		 final ImageView praise_img = ViewHolder.get(convertView, R.id.tea_say_praise_img);
		 GridView tea_say_grid =  ViewHolder.get(convertView, R.id.tea_say_grid);
		
		 if(ts.tea_say_images.size()==1){
			 if(!StringAndDataUtil.isNullOrEmpty(ts.tea_say_images.get(0))){
				 tea_say_image.setVisibility(View.VISIBLE);
				 ViewHolder.setImageByUrl(convertView, R.id.tea_say_image, ts.tea_say_images.get(0));
				 tea_say_grid.setVisibility(View.GONE); 
			 }else{
				 tea_say_grid.setVisibility(View.GONE);
				 tea_say_image.setVisibility(View.GONE);
			 }
		 }else if(ts.tea_say_images.size()>1){
			 LayoutParams lp =tea_say_grid.getLayoutParams();
			 if(ts.tea_say_images.size()<=3){
				 lp.height = 300;
			 }else if(ts.tea_say_images.size()>3&&ts.tea_say_images.size()<=6){
				 lp.height = 650;
			 }else if(ts.tea_say_images.size()>6&&ts.tea_say_images.size()<=9){
				 lp.height = 1000;
			 }
			 tea_say_grid.setLayoutParams(lp);
			 tea_say_image.setVisibility(View.GONE);
			 tea_say_grid.setVisibility(View.VISIBLE);
			 TeaSayGridAdapter tsg = new TeaSayGridAdapter(mContext,ts.tea_say_images);
			 tea_say_grid.setAdapter(tsg);
		 }
		 ImageView tea_say_list_reply =  ViewHolder.get(convertView, R.id.tea_say_list_reply);
		 ImageView tea_say_list_delete = ViewHolder.get(convertView, R.id.tea_say_list_delete);
		 tea_say_list_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Builder b = new Builder(ac);
				b.setMessage("删除？");
				b.setPositiveButton("确定", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Bundle bd = new Bundle();
						bd.putString("POSITION", String.valueOf(position));
						Message msg = handler.obtainMessage();
						msg.setData(bd);
						handler.sendMessage(msg);
					}
					
				});
				b.setNegativeButton("取消", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
					
				});
				b.show();		
			}
		});
		 
		 tea_say_list_reply.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { 
				Intent i =new Intent(ac,TeaSayReplyActivity.class);
				i.putExtra(TeaSayReplyActivity.TEASAYINFO,ts);
				ac.startActivity(i);
			}
		});
		 
		 final BadgeView badge = new BadgeView(mContext);
		 badge.setTargetView(praise_img);
		 badge.setText("8");
		 praise_img.setBackgroundResource(R.drawable.dispraise_icon);
		 
		 praise_img.setOnClickListener(new OnClickListener() {
				 @Override
				public void onClick(View v) {
					 badge.incrementBadgeCount(1);
					 praise_img.setBackgroundResource(R.drawable.praise_icon);
				}
			});
	    return convertView;
	}

	
	

}
