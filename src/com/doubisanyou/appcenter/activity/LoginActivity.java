package com.doubisanyou.appcenter.activity;

import org.jivesoftware.smack.util.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.bean.EBEvents;
import com.doubisanyou.appcenter.bean.EBEvents.RequestVCardEvent;
import com.doubisanyou.appcenter.bean.User;
import com.doubisanyou.appcenter.bean.EBEvents.RequestLoginEvent;
import com.doubisanyou.appcenter.bean.EBEvents.ResponseLoginEvent;
import com.doubisanyou.appcenter.bean.EBEvents.ResponseVCardEvent;
import com.doubisanyou.appcenter.date.Config;
import com.doubisanyou.baseproject.base.BaseActivity;
import com.doubisanyou.baseproject.network.NetConnect;
import com.doubisanyou.baseproject.network.NetConnect.FailCallBack;
import com.doubisanyou.baseproject.network.NetConnect.SuccessCallBack;
import com.doubisanyou.baseproject.utilCommon.JsonUtil;

import de.greenrobot.event.EventBus;

public class LoginActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = LoginActivity.class.getSimpleName();
	EditText loginUserName;
	EditText loginPassWord;
	Button loginBtn;
	Button rigisterBtn;
	TextView title;
	String username;
	String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		iniView();
	}

	void iniView() {
		loginUserName = (EditText) findViewById(R.id.login_user_name);
		loginPassWord = (EditText) findViewById(R.id.login_pass_word);
		loginBtn = (Button) findViewById(R.id.btn_signin);
		loginBtn.setOnClickListener(this);
		rigisterBtn = (Button) findViewById(R.id.btn_register);
		rigisterBtn.setOnClickListener(this);
		title = (TextView) findViewById(R.id.default_title);
		title.setText("登录");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_signin:
			username = loginUserName.getText().toString();
			password = loginPassWord.getText().toString();
			if (StringUtils.isNullOrEmpty(username)
					|| StringUtils.isNullOrEmpty(password)) {
				Toast.makeText(this, "用户名或密码为空", Toast.LENGTH_SHORT).show();
				return;
			}
			RequestLoginEvent reqloginEvent = EBEvents
					.instanceRequestLoginEvent();
			reqloginEvent.setUsername(username);
			reqloginEvent.setPassword(password);
			EventBus.getDefault().post(reqloginEvent);
//			finish();
			break;
		case R.id.btn_register:
			Intent i = new Intent(this, RegisterActivity.class);
			startActivity(i);
			break;

		default:
			break;
		}

	}

	@Override
	protected void onStart() {
		Log.i(TAG, "start");
		super.onStart();
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onStop() {
		Log.i(TAG, "stop");
		EventBus.getDefault().unregister(this);
		super.onStop();
	}

	public void onEventMainThread(ResponseLoginEvent respLoginEvent) {
		int respCode = respLoginEvent.getRespCode();

		String errorText;
		switch (respCode) {
		case 0:
			errorText = "用户名或密码错误";
			break;
		case 1:
			NetConnect task = new NetConnect(Config.SERVICE_URL
					+ "/mobile/user/getuser?id=" + username,
					new SuccessCallBack() {
						@Override
						public void onSuccess(String result) {
							User u = (User) JsonUtil.JsonToObject(result,
									User.class);
							Config.setToken(getApplicationContext(),
									u.user_token);
							Config.user = u;
							finish();
						}
					}, new FailCallBack() {
						@Override
						public void onFail() {
							showToast("错误");
						}
					}, "");
//			finish();
			return;
		default:
			errorText = "未知错误";
			break;
		}
		Toast.makeText(this, errorText, Toast.LENGTH_LONG).show();
	}
	/*
	 * public void onEventMainThread(ResponseVCardEvent responseVCardEvent){
	 * responseVCardEvent.getvCard(); RequestVCardEvent requestVCardEvent =
	 * EBEvents.instanceRequestVCardEvent();
	 * requestVCardEvent.setUsername(username);
	 * EventBus.getDefault().post(requestVCardEvent); }
	 */
}