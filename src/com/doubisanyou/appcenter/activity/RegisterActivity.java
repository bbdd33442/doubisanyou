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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.bean.User;
import com.doubisanyou.appcenter.date.Config;
import com.doubisanyou.baseproject.base.BaseActivity;
import com.doubisanyou.baseproject.utilsResource.ImageLoader;
import com.doubisanyou.baseproject.utilsResource.ImageLoader.Type;

public class RegisterActivity extends BaseActivity implements OnClickListener{
	
	Button back;
	Button getCheckCode;
	Button registeOk;
	TextView title;
	ImageView userAvatars;
	int second;
	EditText registePhoneNumber;
	EditText registeCheckCode;
	EditText registeNickName;
	EditText registePass;
	EditText registePassCheck;
	RadioGroup registeUserType;
	User user;
	ArrayList<String> selectedImage = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_register);
		iniView();
	}

	private void iniView() {
		user = new User();
		getCheckCode =(Button) findViewById(R.id.get_check_code);
		getCheckCode.setOnClickListener(this);
		userAvatars = (ImageView) findViewById(R.id.registe_user_avartar);
		userAvatars.setOnClickListener(this);
		back = (Button) findViewById(R.id.btn_left);
		back.setOnClickListener(this);
		back.setVisibility(View.VISIBLE);
		title = (TextView) findViewById(R.id.default_title);
		title.setText("注册");
		registeOk =(Button) findViewById(R.id.btn_registe_ok);
		registeOk.setOnClickListener(this);
		if(getIntent().getStringArrayListExtra(TeaSayImageSelectedViewActivity.SELECTED_IMAGE_PATH)!=null){
			selectedImage.clear();
			selectedImage = getIntent().getStringArrayListExtra(TeaSayImageSelectedViewActivity.SELECTED_IMAGE_PATH);
			ImageLoader.getInstance(3,Type.LIFO).loadImage(selectedImage.get(0),userAvatars);
		}
		registePhoneNumber = (EditText) findViewById(R.id.registe_phone_number);
		registeCheckCode = (EditText) findViewById(R.id.registe_check_code);
		registeNickName = (EditText) findViewById(R.id.registe_nick_name);
		registePass = (EditText) findViewById(R.id.registe_pass);
		registePassCheck = (EditText) findViewById(R.id.registe_pass_check);
		registeUserType = (RadioGroup) findViewById(R.id.registe_user_type);
		registeUserType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.user_type_tea_business:
					user.user_type = "1";
					break;
				case R.id.user_type_tea_company:
					user.user_type = "2";
					break;
				case R.id.user_type_tea_friend:
					user.user_type = "0";
					break;
				default:
					break;
				}
				
				
			}
		});
	}
   
	Handler handler = new Handler(){

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				getCheckCode.setText(second+"秒后重新获取");
				break;
			case 2:
				getCheckCode.setText("获取验证码");
				getCheckCode.setClickable(true);
				break;
			default:
				break;
			}
		}
		
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_left:
			finish();
			break;
		case R.id.get_check_code:
			getCheckCode.setClickable(false);
			 new Thread(new Runnable() {
				@Override
				public void run() {
					second = 60;
					while(second>=0){
						handler.sendEmptyMessage(1);
						second--;
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					handler.sendEmptyMessage(2);
				
				}
			}).start(); 
			break;
		case R.id.btn_registe_ok:
			//进行网络通讯，将用户加入到数据库中
			
			Config.user = user;
			finish();
			break;
		case R.id.registe_user_avartar:
			Intent i = new Intent(this,TeaSayPublushImageFolderListActivity.class);
			i.putStringArrayListExtra(TeaSayImageSelectedViewActivity.SELECTED_IMAGE_PATH, selectedImage);
			i.putExtra(TeaSayPublushImageFolderListActivity.ACTIVITYTYPE,TeaSayPublushImageFolderListActivity.USERAVATARS);
			startActivity(i);
			finish();
		default:
			break;
		}
		
	}
	
	
}
