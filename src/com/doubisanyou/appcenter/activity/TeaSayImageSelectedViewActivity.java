package com.doubisanyou.appcenter.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.adapter.ImageListAdapter;
import com.doubisanyou.appcenter.date.Config;
import com.doubisanyou.baseproject.base.BaseActivity;

public class TeaSayImageSelectedViewActivity extends BaseActivity implements OnClickListener{

	public static final String FOLDER_DIR="folder_dir";
	public static final String SELECTED_IMAGE_PATH="selected_image_path";
	public static final String FOLDER_IMAGE_NAME="folder_image_name";
	
	TextView title;
	private String folderDir;
	private ArrayList<String> selectedImagePath;
	private ArrayList<String> folderImageName;
	private GridView mGirdView;
	private TextView imageViewFinish;
	private Button back;
	private ImageListAdapter ila;
	private String type;
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
		title = (TextView) findViewById(R.id.default_title);
		
		mGirdView=(GridView) findViewById(R.id.image_view_gridview);
		imageViewFinish = (TextView) findViewById(R.id.image_view_finish);
		imageViewFinish.setOnClickListener(this);
		Intent i = getIntent();
		
		folderDir = i.getCharSequenceExtra(FOLDER_DIR).toString();
		selectedImagePath = i.getStringArrayListExtra(SELECTED_IMAGE_PATH);
		folderImageName = i.getStringArrayListExtra(FOLDER_IMAGE_NAME);
		title.setText(folderDir.subSequence(folderDir.lastIndexOf("/")+1, folderDir.length()));
		type = i.getStringExtra(TeaSayPublushImageFolderListActivity.ACTIVITYTYPE);
		int num = 0;
		if(type.equals(TeaSayPublushImageFolderListActivity.TEASYPUBLISH)){
			num = 9;
		}else if(type.equals(TeaSayPublushImageFolderListActivity.USERAVATARS)||type.equals(TeaSayPublushImageFolderListActivity.EDITEUSERAVATARS)){
			num = 1;
		}
		if(selectedImagePath!=null){
			imageViewFinish.setText("完成(已选择"+selectedImagePath.size()+"张)");
		}
		ila= new ImageListAdapter(getApplicationContext(),folderImageName,
					   folderDir,selectedImagePath,TeaSayImageSelectedViewActivity.this,mHandler,num);
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
			Intent i1 = null ;
			if(type.equals(TeaSayPublushImageFolderListActivity.TEASYPUBLISH)){
				i1 = new Intent(TeaSayImageSelectedViewActivity.this,TeaSayPublishActivity.class);
			}else if(type.equals(TeaSayPublushImageFolderListActivity.USERAVATARS)){
				i1 = new Intent(TeaSayImageSelectedViewActivity.this,RegisterActivity.class);
			}else if(type.equals(TeaSayPublushImageFolderListActivity.EDITEUSERAVATARS)){
				Bundle b = new Bundle();
				Message msg = Config.handler.obtainMessage();
				msg.what=1;
				msg.setData(b);
				b.putStringArrayList(SELECTED_IMAGE_PATH, selectedImagePath);
				Config.handler.sendMessage(msg);
				setResult(1, i);
				finish();
				break;
			}
			i1.putStringArrayListExtra(SELECTED_IMAGE_PATH, selectedImagePath);
			i1.putExtra(TeaSayPublishActivity.PUBLISHTYPE, TeaSayPublishActivity.IMAGE);
			startActivity(i1);
			setResult(1, i);
			finish();
			break;
		default:
			break;
		}
	}

	
}
