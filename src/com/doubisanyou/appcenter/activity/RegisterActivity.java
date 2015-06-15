package com.doubisanyou.appcenter.activity;

import java.util.ArrayList;

import org.jivesoftware.smack.util.StringUtils;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.bean.EBEvents;
import com.doubisanyou.appcenter.bean.EBEvents.RequestRegisterEvent;
import com.doubisanyou.appcenter.bean.EBEvents.ResponseRegisterEvent;
import com.doubisanyou.appcenter.bean.User;
import com.doubisanyou.appcenter.date.Config;
import com.doubisanyou.baseproject.base.BaseActivity;
import com.doubisanyou.baseproject.network.NetConnect;
import com.doubisanyou.baseproject.network.NetConnect.FailCallBack;
import com.doubisanyou.baseproject.network.NetConnect.SuccessCallBack;
import com.doubisanyou.baseproject.utilCommon.JsonUtil;
import com.doubisanyou.baseproject.utilsResource.ImageLoader;
import com.doubisanyou.baseproject.utilsResource.ImageLoader.Type;

import de.greenrobot.event.EventBus;

public class RegisterActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = RegisterActivity.class.getSimpleName();
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
	public ProgressDialog pDlg;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_register);
		iniView();
	}

	private void iniView() {
		user = new User();
		getCheckCode = (Button) findViewById(R.id.get_check_code);
		getCheckCode.setOnClickListener(this);
		userAvatars = (ImageView) findViewById(R.id.registe_user_avartar);
		userAvatars.setOnClickListener(this);
		back = (Button) findViewById(R.id.btn_left);
		back.setOnClickListener(this);
		back.setVisibility(View.VISIBLE);
		title = (TextView) findViewById(R.id.default_title);
		title.setText("注册");
		registeOk = (Button) findViewById(R.id.btn_registe_ok);
		registeOk.setOnClickListener(this);
		if (getIntent().getStringArrayListExtra(
				TeaSayImageSelectedViewActivity.SELECTED_IMAGE_PATH) != null) {
			selectedImage.clear();
			selectedImage = getIntent().getStringArrayListExtra(
					TeaSayImageSelectedViewActivity.SELECTED_IMAGE_PATH);
			
			ImageLoader.getInstance(3, Type.LIFO).loadImage(
					selectedImage.get(0), userAvatars);
			user.user_avartars = selectedImage.get(0);
		}
		registePhoneNumber = (EditText) findViewById(R.id.registe_phone_number);
		registeCheckCode = (EditText) findViewById(R.id.registe_check_code);
		registeNickName = (EditText) findViewById(R.id.registe_nick_name);
		registePass = (EditText) findViewById(R.id.registe_pass);
		registePassCheck = (EditText) findViewById(R.id.registe_pass_check);
		registeUserType = (RadioGroup) findViewById(R.id.registe_user_type);
		registeUserType
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

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
		
		pDlg = new ProgressDialog(this);
		pDlg.setOnCancelListener(this);
		pDlg.setTitle("提示");
		pDlg.setMessage("正在进行网络连接，请稍后...");
	}


	Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				getCheckCode.setText(second + "秒后重新获取");
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
					while (second >= 0) {
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
			// 进行网络通讯，将用户加入到数据库中
			String username = registePhoneNumber.getText().toString();
			String password = registePass.getText().toString();
			String checkPasswrod = registePassCheck.getText().toString();
			if (StringUtils.isNullOrEmpty(user.user_type)) {
				Toast.makeText(this, "必须选择一个身份", Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringUtils.isNullOrEmpty(registeNickName.getText().toString())) {
				Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringUtils.isNullOrEmpty(username)
					|| StringUtils.isNullOrEmpty(password)
					|| StringUtils.isNullOrEmpty(checkPasswrod)) {
				Toast.makeText(this, "用户密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!password.equals(checkPasswrod)) {
				Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
				return;
			}
			RequestRegisterEvent reqRegisterEvent = EBEvents
					.instanceRequestRegisterEvent();
			reqRegisterEvent.setUsername(username);
			reqRegisterEvent.setPassword(password);
			EventBus.getDefault().post(reqRegisterEvent);
			//finish();
			break;
		case R.id.registe_user_avartar:
			Intent i = new Intent(this,
					TeaSayPublushImageFolderListActivity.class);
			i.putStringArrayListExtra(
					TeaSayImageSelectedViewActivity.SELECTED_IMAGE_PATH,
					selectedImage);
			i.putExtra(TeaSayPublushImageFolderListActivity.ACTIVITYTYPE,
					TeaSayPublushImageFolderListActivity.USERAVATARS);
			startActivity(i);
			finish();
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

	public void onEventMainThread(ResponseRegisterEvent respRegisterEvent) {
		int respCode = respRegisterEvent.getRespCode();
		String errorText = null;
		switch (respCode) {
		case 0:
			errorText = "连接错误";
			break;
		case 1:
			showToast("ok");
			user.user_id = registePhoneNumber.getText().toString();
			user.user_nick_name=registeNickName.getText().toString();
			user.user_integral="0";
			user.user_count_num="0";
			
			if(selectedImage.size()>0){
				user.user_avartars=selectedImage.get(0);
			}
			String parameter = JsonUtil.ObjectToJson(user);
			
			pDlg.show();
			
			NetConnect task = new NetConnect(Config.SERVICE_URL+"/mobile/user/registe",new SuccessCallBack() {
				@Override
				public void onSuccess(String result) {
					Config.user = user;
					showToast(result);
					pDlg.dismiss();
					finish();
				}
			},new FailCallBack() {
				@Override
				public void onFail() {
					showToast("错误");
					pDlg.dismiss();
				}
			}, parameter);
			finish();
			break;
		default:
			errorText = "未知错误";
			break;
		}
		Toast.makeText(this, errorText, Toast.LENGTH_LONG).show();
	}
}