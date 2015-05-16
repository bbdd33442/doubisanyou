package com.doubisanyou.appcenter.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.adapter.ImageListAdapter;

public class TeaSayImageSelectedViewActivity extends Activity implements OnClickListener{

	public static final String FOLDER_DIR="folder_dir";
	public static final String SELECTED_IMAGE_PATH="selected_image_path";
	public static final String FOLDER_IMAGE_NAME="folder_image_name";
	
	private String folderDir;
	private ArrayList<String> selectedImagePath;
	private ArrayList<String> folderImageName;
	private GridView mGirdView;
	private TextView imageViewFinish;
	private Button back;
	private ImageListAdapter ila;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tea_say_folder_image_view);
	
		iniView();

	}
	
	void iniView(){
		back  = (Button) findViewById(R.id.btn_left);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		mGirdView=(GridView) findViewById(R.id.image_view_gridview);
		imageViewFinish = (TextView) findViewById(R.id.image_view_finish);
		imageViewFinish.setOnClickListener(this);
		Intent i = getIntent();
		folderDir = i.getCharSequenceExtra(FOLDER_DIR).toString();
		selectedImagePath = i.getStringArrayListExtra(SELECTED_IMAGE_PATH);
		folderImageName = i.getStringArrayListExtra(FOLDER_IMAGE_NAME);
		if(selectedImagePath!=null){
			imageViewFinish.setText("完成(已选择"+selectedImagePath.size()+"张)");
		}
		ila= new ImageListAdapter(getApplicationContext(),folderImageName,
					   folderDir,selectedImagePath,TeaSayImageSelectedViewActivity.this,mHandler);
		mGirdView.setAdapter(ila);
	};
	
	Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				imageViewFinish.setText("完成(已选择"+selectedImagePath.size()+"张)");
				break;

			default:
				break;
			}
		};
	};
	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		switch (v.getId()) {
		case R.id.btn_left:
			i.putStringArrayListExtra(SELECTED_IMAGE_PATH, selectedImagePath);
			setResult(0, i);
			finish();
			break;
		case R.id.image_view_finish:
			Intent i1 = new Intent(TeaSayImageSelectedViewActivity.this,PublishTeaSayActivity.class);
			i1.putStringArrayListExtra(SELECTED_IMAGE_PATH, selectedImagePath);
			i1.putExtra(PublishTeaSayActivity.PUBLISHTYPE, PublishTeaSayActivity.IMAGE);
			startActivity(i1);
			setResult(1, i);
			finish();
			break;
		default:
			break;
		}
	}

	
}
