package com.doubisanyou.appcenter.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.adapter.SimpleAdapter;
import com.doubisanyou.appcenter.adapter.TeaSayGridAdapter;
import com.doubisanyou.appcenter.bean.TeaSay;
import com.doubisanyou.appcenter.widget.BadgeView;
import com.doubisanyou.baseproject.base.BaseActivity;
import com.doubisanyou.baseproject.utilCommon.StringAndDataUtil;
import com.doubisanyou.baseproject.utilCommon.TimeUtil;
import com.doubisanyou.baseproject.utilsResource.ImageLoader;
import com.doubisanyou.baseproject.utilsResource.ImageLoader.Type;

public class TeaSayReplyActivity extends BaseActivity implements
		OnClickListener {

	public static final String TEASAYINFO = "teasayinfo";

	TeaSay ts;

	TextView replyTitle;
	Button back;
	ListView replyListView;
	Button teaSayCommentButton;
	SimpleAdapter sla;
	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> replyItem;
	EditText commentText;

	TextView tea_say_publisher_name;
	TextView tea_say_content;
	TextView tea_say_publisher_id;
	TextView tea_say_publish_date;
	TextView tea_say_time;
	ImageView tea_say_publisher_avatar;
	ImageView tea_say_image;
	ImageView tea_say_praise_img;
	GridView tea_say_grid;
	LinearLayout ll;
	ScrollView scrollView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_tea_say_reply);
		iniView();
	}

	void iniView() {
		ll =  (LinearLayout) findViewById(R.id.tea_say_reply_listview);
		ts = (TeaSay) getIntent().getSerializableExtra(TEASAYINFO);
		scrollView = (ScrollView) findViewById(R.id.scroll_view);
		replyItem = new ArrayList<HashMap<String, Object>>();
		map = new HashMap<String, Object>();
		map.put("useravatars", R.drawable.user_default_avatars);
		map.put("replycontent", "12345上山打老虎,老虎不在家");
		
		for (int i = 0; i < 6; i++) {
			View view =LayoutInflater.from(this).inflate(R.layout.listitem_tea_say_repley, null);
			TextView x = (TextView) view.findViewById(R.id.tea_say_reply_content);
			ImageView iv = (ImageView) view.findViewById(R.id.tea_say_reply_user_avatars);
			TextView x1 = (TextView) view.findViewById(R.id.tea_say_reply_user_name);
			x1.setText("中南大学");
			x.setText("1234566");
			iv.setImageResource(R.drawable.csu);
			ll.addView(view);
		}
		
		replyItem.add(map);
		replyItem.add(map);
		replyItem.add(map);
		replyItem.add(map);
		replyItem.add(map);
		replyItem.add(map);
		replyItem.add(map);
		replyItem.add(map);
		replyItem.add(map);
		replyItem.add(map);
		replyItem.add(map);
		replyItem.add(map);
		sla = new SimpleAdapter(getApplicationContext(), replyItem,
				R.layout.listitem_tea_say_repley, new String[] { "useravatars",
						"replycontent" }, new int[] {
						R.id.tea_say_reply_user_avatars,
						R.id.tea_say_reply_content });
		replyTitle = (TextView) findViewById(R.id.default_title);
		replyTitle.setText("回复");
		replyTitle.setOnClickListener(this);
		back = (Button) findViewById(R.id.btn_left);
		back.setOnClickListener(this);
		back.setVisibility(View.VISIBLE);
		/*replyListView = (ListView)*/ 
		
		/*replyListView.setAdapter(sla);
		*/
		teaSayCommentButton = (Button) findViewById(R.id.tea_say_comment_btn);
		teaSayCommentButton.setOnClickListener(this);
		commentText = (EditText) findViewById(R.id.tea_say_comment_content);

		tea_say_publisher_name = (TextView) findViewById(R.id.tea_say_publisher_name);
		tea_say_publisher_name.setText(ts.tea_say_publisher_name);
		tea_say_content = (TextView) findViewById(R.id.tea_say_content);
		tea_say_content.setText(ts.tea_say_content);
		tea_say_publisher_id = (TextView) findViewById(R.id.tea_say_publisher_id);
		tea_say_publisher_id.setText(ts.tea_say_publisher_id);
		tea_say_publish_date = (TextView) findViewById(R.id.tea_say_publish_date);
		tea_say_publish_date.setText(ts.tea_say_publish_date);
		tea_say_time = (TextView) findViewById(R.id.tea_say_time);
		tea_say_time.setText(TimeUtil.getDescriptionTimeFromTimestamp(Long
				.parseLong(ts.tea_say_time)));
		tea_say_publisher_avatar = (ImageView) findViewById(R.id.tea_say_publisher_avatar);
		tea_say_image = (ImageView) findViewById(R.id.tea_say_image);
		tea_say_praise_img = (ImageView) findViewById(R.id.tea_say_praise_img);
		final BadgeView badge = new BadgeView(getApplicationContext());
		 badge.setTargetView(tea_say_praise_img);
		 badge.setText("8");
		 tea_say_praise_img.setBackgroundResource(R.drawable.dispraise_icon);
		 
		 tea_say_praise_img.setOnClickListener(new OnClickListener() {
				 @Override
				public void onClick(View v) {
					 badge.incrementBadgeCount(1);
					 tea_say_praise_img.setBackgroundResource(R.drawable.praise_icon);
				}
			});
		tea_say_grid = (GridView) findViewById(R.id.tea_say_grid);
		 if(ts.tea_say_images.size()==1){
			 if(!StringAndDataUtil.isNullOrEmpty(ts.tea_say_images.get(0))){
				 tea_say_image.setVisibility(View.VISIBLE);
				 ImageLoader.getInstance(3,Type.LIFO).loadImage(ts.tea_say_images.get(0), tea_say_image);
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
			 TeaSayGridAdapter tsg = new TeaSayGridAdapter(getApplicationContext(),ts.tea_say_images);
			 tea_say_grid.setAdapter(tsg);
		 }
	}
	
	Handler handler = new Handler();
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_left:
			finish();
			break;
		case R.id.tea_say_comment_btn:
			View view =LayoutInflater.from(this).inflate(R.layout.listitem_tea_say_repley, null);
			TextView x = (TextView) view.findViewById(R.id.tea_say_reply_content);
			x.setText(commentText.getText().toString());
			ll.addView(view);
			commentText.setText("");
		    handler.post(new Runnable() {  
		        @Override  
		        public void run() {  
		            scrollView.fullScroll(ScrollView.FOCUS_DOWN);  
		        }  
		    });  
			break;
		case R.id.default_title:
			 
		     scrollView.fullScroll(ScrollView.FOCUS_UP);  
		   
			break;
	
		default:
			break;
		}

	}
}
