package com.doubisanyou.appcenter.activity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.sasl.provided.SASLDigestMD5Mechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.doubisanyou.appcenter.bean.User;
import com.doubisanyou.appcenter.date.Config;
import com.doubisanyou.baseproject.base.BaseActivity;
import com.doubisanyou.baseproject.network.ConnectMethd;
import com.doubisanyou.baseproject.network.NetConnect;
import com.doubisanyou.baseproject.network.NetConnect.FailCallBack;
import com.doubisanyou.baseproject.network.NetConnect.SuccessCallBack;
import com.doubisanyou.baseproject.utilCommon.JsonUtil;
import com.doubisanyou.baseproject.utilsResource.ImageLoader;
import com.doubisanyou.baseproject.utilsResource.ImageLoader.Type;

public class RegisterActivity extends BaseActivity implements OnClickListener{
	String re = "";
	Bitmap b;
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
     
		new Thread(){
			@Override
			public void run(){
			/*	SASLAuthentication.supportSASLMechanism("PLAIN",0);*/
				 SmackConfiguration.DEBUG = true;
				 VCard vcard = new VCard();  
					XMPPTCPConnectionConfiguration conf = XMPPTCPConnectionConfiguration
							.builder()
							.setSendPresence(false)
							.setSecurityMode(SecurityMode.disabled)
							.setServiceName("localhost")
							.setHost("192.168.1.122").setPort(5222).build();
				
					AbstractXMPPConnection  con = new XMPPTCPConnection(conf);
					
			        try {
			        	if (con.isConnected()) {
			        		con.disconnect();
						}
			        	con.connect();
			        	con.login("xy198989", "xy198989");
			        	System.out.println(con.getUser());
			        	
			        	vcard.load(con);
					} catch (NoResponseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (XMPPErrorException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NotConnectedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SmackException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (XMPPException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  
			          
			       
			        ByteArrayInputStream bais = new ByteArrayInputStream(  
			                vcard.getAvatar());  
			        b = BitmapFactory.decodeStream(bais);
			        handler.sendEmptyMessage(3);
			       
		
			}
			}.start();
			
			
				
	
		
	   
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
			case 3:
				userAvatars.setImageBitmap(b);
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
			this.showProgressDialog();
			user.user_id = registePhoneNumber.getText().toString();
			user.user_nick_name=registeNickName.getText().toString();
			String parameter = JsonUtil.ObjectToJson(user);	
			task = new NetConnect(Config.SERVICE_URL+"mobile/user/registe",ConnectMethd.POST,new SuccessCallBack() {
				@Override
				public void onSuccess(String result) {
					re= result;
					pDlg.dismiss();
					Toast.makeText(getApplicationContext(), re, Toast.LENGTH_LONG).show();
					finish();
				}
			},new FailCallBack() {
				
				@Override
				public void onFail() {
					re="错误！";
					Toast.makeText(getApplicationContext(), re, Toast.LENGTH_LONG).show();
					pDlg.dismiss();
					
				}
			}, parameter);
			
		
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
