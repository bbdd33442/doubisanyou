package com.doubisanyou.appcenter.activity;

import java.util.ArrayList;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.adapter.TeaSayPublishImageGridAdapter;
import com.doubisanyou.baseproject.base.BaseActivity;

public class TeaSayPublishActivity extends BaseActivity implements OnClickListener,OnItemClickListener{
	
	public static final String TEXT="text";
	public static final String IMAGE="image";
	public static final String PUBLISHTYPE="publishtype";
	
	private String publishType;
	private Button backBtn;
	private TextView titleBar;
	private GridView publishImageGridview;
	private TeaSayPublishImageGridAdapter tpiAdapter;
	private ArrayList<String> selectedImage = new ArrayList<String>();
	private Button publishBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_publish_tea_say);
		publishType = getIntent().getStringExtra(PUBLISHTYPE);
		
		if(getIntent().getStringArrayListExtra(TeaSayImageSelectedViewActivity.SELECTED_IMAGE_PATH)!=null){
			selectedImage.clear();
			selectedImage = getIntent().getStringArrayListExtra(TeaSayImageSelectedViewActivity.SELECTED_IMAGE_PATH);
		}		
		if(selectedImage.size()<9){
			selectedImage.add(String.valueOf(R.drawable.tea_say_publish_add_image));
		}
		tpiAdapter = new TeaSayPublishImageGridAdapter(getApplicationContext(),selectedImage);

		tpiAdapter.notifyDataSetChanged();
		iniView();
	}

	
	void iniView(){
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
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tea_say_publish_image:
			 
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
