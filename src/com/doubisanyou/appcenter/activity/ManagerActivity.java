package com.doubisanyou.appcenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.date.Config;
import com.doubisanyou.baseproject.base.BaseActivity;

public class ManagerActivity extends BaseActivity implements OnClickListener {
	
	Button clearnCatcheBtn;
	Button logOutBtn;
	TextView title;
	String token;
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
		title.setText("设置");
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
		default:
			break;
		}
		
	}
	
	
}
