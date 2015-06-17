package com.doubisanyou.appcenter.activity;

import java.util.ArrayList;
import java.util.UUID;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.adapter.TeaSayPublishImageGridAdapter;
import com.doubisanyou.appcenter.bean.TeaSay;
import com.doubisanyou.appcenter.date.Config;
import com.doubisanyou.appcenter.db.TeaSayDBManager;
import com.doubisanyou.baseproject.base.BaseActivity;
import com.doubisanyou.baseproject.utilCommon.DateUtil;
import com.doubisanyou.baseproject.utilCommon.StringAndDataUtil;

public class TeaSayPublishActivity extends BaseActivity implements OnClickListener,OnItemClickListener{
	
	public static final String TEXT="text";
	public static final String IMAGE="image";
	public static final String PUBLISHTYPE="publishtype";
	
	TeaSay ts;
	private TeaSayDBManager tsdb;
	private String publishType;
	private Button backBtn;
	private TextView titleBar;
	private GridView publishImageGridview;
	private TeaSayPublishImageGridAdapter tpiAdapter;
	private ArrayList<String> selectedImage = new ArrayList<String>();
	private Button publishBtn;
	private EditText teaSayContent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_publish_tea_say);
		iniView();
	}
	

	void iniView(){
		ts = new TeaSay();
		publishType = getIntent().getStringExtra(PUBLISHTYPE);
		if(getIntent().getStringArrayListExtra(TeaSayImageSelectedViewActivity.SELECTED_IMAGE_PATH)!=null){
			selectedImage.clear();
			selectedImage = getIntent().getStringArrayListExtra(TeaSayImageSelectedViewActivity.SELECTED_IMAGE_PATH);
			ts.tea_say_images=selectedImage;
		}		
		if(selectedImage.size()<9){
			selectedImage.add(String.valueOf(R.drawable.tea_say_publish_add_image));
		}
		tpiAdapter = new TeaSayPublishImageGridAdapter(getApplicationContext(),selectedImage);
		tpiAdapter.notifyDataSetChanged();
		backBtn = (Button) findViewById(R.id.btn_left);
		backBtn.setOnClickListener(this);
		backBtn.setVisibility(View.VISIBLE);
		publishBtn = (Button) findViewById(R.id.btn_right_btn);
		publishBtn.setOnClickListener(this);
		publishBtn.setVisibility(View.VISIBLE);
		publishBtn.setText("发布");
		titleBar =  (TextView) findViewById(R.id.default_title);
		titleBar.setText("新建茶说");
		publishImageGridview = (GridView) findViewById(R.id.tea_say_publish_image_gridview);
		publishImageGridview.setOnItemClickListener(this);
		publishImageGridview.setAdapter(tpiAdapter);
		if(publishType.equals(TEXT)){
		    publishImageGridview.setVisibility(View.GONE);
		}else if(publishType.equals(IMAGE)){
			
		}
		teaSayContent = (EditText) findViewById(R.id.tea_say_publish_content);
		tsdb = new TeaSayDBManager(getApplicationContext());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_right_btn:
			ts.tea_say_id=UUID.randomUUID().toString();
			ts.tea_say_publisher_id=Config.user.user_id;
			ts.tea_say_publisher_avatar = Config.user.user_avartars;
			ts.tea_say_publisher_name=Config.user.user_nick_name;
			ts.tea_say_time=String.valueOf(System.currentTimeMillis());
			ts.tea_say_publish_date=DateUtil.getCurDate();
			ts.tea_say_content=teaSayContent.getText().toString();
			if(!StringAndDataUtil.isNullOrEmpty(Config.user.user_avartars)){
				ts.tea_say_publisher_avatar=Config.user.user_avartars;				
			}
			if(ts.tea_say_images.contains(String.valueOf(R.drawable.tea_say_publish_add_image))){
				ts.tea_say_images.remove(String.valueOf(R.drawable.tea_say_publish_add_image));
			}
			tsdb.addTeaSay(ts);
			finish();
			break;
		case R.id.btn_left:
			finish();
			break;

		default:
			break;
		}
		
	}


	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
		if(selectedImage.get(arg2).equals(String.valueOf(R.drawable.tea_say_publish_add_image))){
			Intent i = new Intent(TeaSayPublishActivity.this,TeaSayPublushImageFolderListActivity.class);
			selectedImage.remove(arg2);
			i.putStringArrayListExtra(TeaSayImageSelectedViewActivity.SELECTED_IMAGE_PATH, selectedImage);
			i.putExtra(TeaSayPublushImageFolderListActivity.ACTIVITYTYPE,TeaSayPublushImageFolderListActivity.TEASYPUBLISH);
			startActivity(i);
			finish();
		}else{
			Builder b = new Builder(TeaSayPublishActivity.this);
			b.setMessage("删除图片？");
			b.setPositiveButton("确定", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(selectedImage.size()==9&&!selectedImage.contains(String.valueOf(R.drawable.tea_say_publish_add_image))){
						selectedImage.add(String.valueOf(R.drawable.tea_say_publish_add_image));
					}
					selectedImage.remove(arg2);
					tpiAdapter.notifyDataSetChanged();
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
		
	}
}
