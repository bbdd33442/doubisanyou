package com.doubisanyou.appcenter.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.date.Config;
import com.doubisanyou.baseproject.base.BaseActivity;
import com.doubisanyou.baseproject.utilCommon.StringAndDataUtil;
import com.doubisanyou.baseproject.utilsResource.ImageLoader;
import com.doubisanyou.baseproject.utilsResource.ImageLoader.Type;

public class ManagerActivity extends BaseActivity implements OnClickListener {
	
	private Button clearnCatcheBtn;
	private Button logOutBtn;
	private TextView title;
	private String token;
	private RelativeLayout my_publish_tea_say;
	private TextView nickName;
	private TextView signature;
	private ImageView userAvatars;
	ArrayList<String> selectedImage = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_manager);
		iniView();
		token = Config.getToken(getApplicationContext());
		if(token==null||token.equals("")){
			Intent i = new Intent(this,LoginActivity.class);
			startActivity(i);
		}else{
			//将token传给服务器，从服务器上拿到当前用户的信息
		}
		
	}
	
	void iniView(){
		clearnCatcheBtn = (Button) findViewById(R.id.btn_clear_catche);
		clearnCatcheBtn.setOnClickListener(this);
		logOutBtn = (Button) findViewById(R.id.btn_log_out);
		logOutBtn.setOnClickListener(this);
		userAvatars = (ImageView) findViewById(R.id.manager_user_avatars);
		userAvatars.setOnClickListener(this);
		title = (TextView) findViewById(R.id.default_title);
		my_publish_tea_say = (RelativeLayout) findViewById(R.id.my_publish_tea_say);
		my_publish_tea_say.setOnClickListener(this);
		title.setText("设置");
		nickName = (TextView) findViewById(R.id.manager_user_nick_name);
		nickName.setOnClickListener(this);
		signature = (TextView) findViewById(R.id.manager_user_signature);
		signature.setOnClickListener(this);
	}
	
	protected void onResume() {
		super.onResume();
		dateToView();
	}
	
	void dateToView(){
		if(Config.user.equals(null)){
			return;
		}
		String nickNameText = Config.user.user_nick_name;
		if(!StringAndDataUtil.isNullOrEmpty(nickNameText)){
			nickName.setText(nickNameText);
		}
		String signatureText = Config.user.user_signature;
		if(!StringAndDataUtil.isNullOrEmpty(signatureText)){
			signature.setText(signatureText);
		}
		
	}
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			selectedImage = (ArrayList<String>) msg.getData().get(TeaSayImageSelectedViewActivity.SELECTED_IMAGE_PATH);
			ImageLoader.getInstance(3,Type.LIFO).loadImage(selectedImage.get(0),userAvatars);
		}
		
	};
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_clear_catche:
			//清除本地的缓存图片
			Config.BITMAPCATCH.clear();
			break;
		case R.id.btn_log_out:
			Config.setToken(this, "");
			Intent i = new Intent(this,LoginActivity.class);
			startActivity(i);
			break;
		case R.id.my_publish_tea_say:
			Intent i1 = new Intent(this,TeaSayActivity.class);
			i1.putExtra(TeaSayActivity.USERID, "1");
			startActivity(i1);
			break;
		case R.id.manager_user_nick_name:
			Intent i2 = new Intent(this,UserInfoEditeActivity.class);
			i2.putExtra(UserInfoEditeActivity.TITILENAME, UserInfoEditeActivity.NICKNAME);
			startActivity(i2);
			break;
		case R.id.manager_user_signature:
			Intent i3 = new Intent(this,UserInfoEditeActivity.class);
			i3.putExtra(UserInfoEditeActivity.TITILENAME, UserInfoEditeActivity.SIGNATURE);
			startActivity(i3);
			break;
		case R.id.manager_user_avatars:
			Config.handler = mHandler;
			Intent i4 =  new Intent(this,TeaSayPublushImageFolderListActivity.class);
			i4.putStringArrayListExtra(TeaSayImageSelectedViewActivity.SELECTED_IMAGE_PATH, selectedImage);
			i4.putExtra(TeaSayPublushImageFolderListActivity.ACTIVITYTYPE,TeaSayPublushImageFolderListActivity.EDITEUSERAVATARS);
			startActivity(i4);
			break;
		default:
			break;
		}
		
	}
	
	
}
