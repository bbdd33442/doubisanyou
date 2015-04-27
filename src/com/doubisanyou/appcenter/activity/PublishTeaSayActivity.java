package com.doubisanyou.appcenter.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.baseproject.base.BaseActivity;

public class PublishTeaSayActivity extends BaseActivity implements OnClickListener{
	
	public static final String TEXT="text";
	public static final String IMAGE="image";
	public static final String PUBLISHTYPE="publishtype";
	
	private String publishType;
	private ImageView addImage;
	private Button backBtn;
	private TextView titleBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_publish_tea_say);
		
		publishType = getIntent().getStringExtra(PUBLISHTYPE);
		if(publishType.equals(TEXT)){
			addImage = (ImageView) findViewById(R.id.tea_say_add_image);
			addImage.setVisibility(View.GONE);
		}else if(publishType.equals(IMAGE)){
			addImage.setOnClickListener(this);
		}
		iniView();
	}

	
	void iniView(){
		backBtn = (Button) findViewById(R.id.btn_left);
		backBtn.setOnClickListener(this);
		backBtn.setVisibility(View.VISIBLE);
		titleBar =  (TextView) findViewById(R.id.default_title);
		titleBar.setText("新建茶说");
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tea_say_add_image:
			 
			break;
		case R.id.btn_left:
			finish();
			break;

		default:
			break;
		}
		
	}
}
