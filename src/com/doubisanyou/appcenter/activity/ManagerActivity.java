package com.doubisanyou.appcenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.date.Config;
import com.doubisanyou.baseproject.base.BaseActivity;

public class ManagerActivity extends BaseActivity implements OnClickListener {
	
	private Button clearnCatcheBtn;
	private Button logOutBtn;
	private TextView title;
	private String token;
	private RelativeLayout my_publish_tea_say;
	private TextView nickName;
	private TextView signature;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager);
		token = Config.getToken(getApplicationContext());
		if(token==null||token.equals("")){
			Intent i = new Intent(this,LoginActivity.class);
			startActivity(i);
		}else{
			//将token传给服务器，从服务器上拿到当前用户的信息
		}
		
		iniView();
	}
	
	void iniView(){
		clearnCatcheBtn = (Button) findViewById(R.id.btn_clear_catche);
		clearnCatcheBtn.setOnClickListener(this);
		logOutBtn = (Button) findViewById(R.id.btn_log_out);
		logOutBtn.setOnClickListener(this);
		title = (TextView) findViewById(R.id.default_title);
		my_publish_tea_say = (RelativeLayout) findViewById(R.id.my_publish_tea_say);
		my_publish_tea_say.setOnClickListener(this);
		title.setText("设置");
		nickName = (TextView) findViewById(R.id.manager_user_nick_name);
		nickName.setOnClickListener(this);
		signature = (TextView) findViewById(R.id.manager_user_signature);
		signature.setOnClickListener(this);
	}

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
		default:
			break;
		}
		
	}
	
	
}
