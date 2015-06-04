package com.doubisanyou.appcenter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.bean.User;
import com.doubisanyou.appcenter.date.Config;

public class UserInfoEditeActivity extends Activity implements OnClickListener{
  
	public static final String TITILENAME="titlename";
	public static final String NICKNAME="NICKNAME";
	public static final String FAVORITETEA = "favoritetea";
	public static final String SIGNATURE="signature";
	
	private String titleText;
	private EditText nickName;
	private EditText signature;
	private TextView title;
	private Button backBtn;
	private Button rightBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info_edite);
		iniView();
		
	} 
	
	void iniView(){
		titleText="";
		titleText=getIntent().getStringExtra(TITILENAME);
		title = (TextView) findViewById(R.id.default_title);
		if(titleText.equals(NICKNAME)){
			title.setText("昵称");
			nickName = (EditText) findViewById(R.id.edit_user_nick_name);
			nickName.setVisibility(View.VISIBLE);
		}else if(titleText.equals(FAVORITETEA)){
			title.setText("我喜欢的茶");
		}else if(titleText.equals(SIGNATURE)){
			title.setText("个性签名");
			signature = (EditText) findViewById(R.id.edit_singnature);
			signature.setVisibility(View.VISIBLE);
		}
		backBtn = (Button) findViewById(R.id.btn_left);
		backBtn.setVisibility(View.VISIBLE);
		rightBtn = (Button) findViewById(R.id.btn_right_btn);
		rightBtn.setText("确定");
		rightBtn.setVisibility(View.VISIBLE);
		backBtn.setOnClickListener(this);
		rightBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_left:
			finish();
			break;
		case R.id.btn_right_btn:
			if(titleText.equals(NICKNAME)){
				Config.user.user_nick_name=nickName.getText().toString();
			}else if(titleText.equals(FAVORITETEA)){
			
			}else if(titleText.equals(SIGNATURE)){
				Config.user.user_signature = signature.getText().toString();
			}
			finish();
			break;
		default:
			break;
		}
		
	}
	
}
