package com.doubisanyou.appcenter.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.bean.TeaSay;
import com.doubisanyou.appcenter.widget.BadgeView;
import com.doubisanyou.appcenter.widget.ViewHolder;

public class TeaSayAdaptor extends BaseAdapter {
	
	private LayoutInflater layoutInflater;
	private List<TeaSay> mTys;
	private Context context;

	public TeaSayAdaptor(Context context, List<TeaSay> tys) {
		this.mTys = tys;
		this.context= context;
		layoutInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		if(mTys==null||mTys.size()==0){
			return 1;
		}
		return mTys.size();
	}

	@Override
	public Object getItem(int arg0) {
		if (mTys == null||mTys.size()==0) {
			return null;
		}
		return mTys.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

	    if (convertView == null) {
	        convertView = layoutInflater.inflate(R.layout.listitem_tea_say, parent, false);
	    }

	  
	     /*TeaSay ts = (TeaSay) getItem(position);*/
	     TextView tea_say_publisher_name = ViewHolder.get(convertView, R.id.tea_say_publisher_name);
		 TextView  tea_say_content = ViewHolder.get(convertView, R.id.tea_say_content);
		 TextView tea_say_publisher_id = ViewHolder.get(convertView, R.id.tea_say_publisher_id);
		 TextView tea_say_publish_date = ViewHolder.get(convertView, R.id.tea_say_publish_date);
		 TextView tea_say_time = ViewHolder.get(convertView, R.id.tea_say_time);
		 ImageView teasay_publisher_avatar = ViewHolder.get(convertView, R.id.tea_say_publisher_avatar);
		 ImageView tea_say_image = ViewHolder.get(convertView, R.id.tea_say_image);
		 ImageView praise_img = ViewHolder.get(convertView, R.id.tea_say_praise_img);
		/* Button tea_say_list_reply =  ViewHolder.get(convertView, R.id.tea_say_list_reply);
		 Button tea_say_list_delete = ViewHolder.get(convertView, R.id.tea_say_list_delete);	*/
		 
		 final BadgeView goodBadge = new BadgeView(context, praise_img);
		 goodBadge.setText("8");
		 goodBadge.setTextSize(7);
		 goodBadge.show();
		 
		 final BadgeView badBadge = new BadgeView(context, praise_img);
		 badBadge.setTextSize(7);
		
			 praise_img.setOnClickListener(new OnClickListener() {
				 int s=8;
				 @Override
				public void onClick(View v) {
					if(goodBadge.isShown()){
						goodBadge.increment(1);
						s+=1;
						goodBadge.hide();
						badBadge.setText(goodBadge.getText());
						badBadge.show();
					}else if(badBadge.isShown()){
						badBadge.increment(-1);
						s-=1;
						badBadge.hide();
						goodBadge.setText(badBadge.getText());
						goodBadge.show();
					}
					
					
				}
			});
			 
			
		
		/*	 praise_img.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					nobadge.increment(-1);
					nobadge.show();
				}
			});*/
			 tea_say_publisher_name.setText("1111");
			 tea_say_publisher_id.setText("1");
	    return convertView;
	}
	

}
