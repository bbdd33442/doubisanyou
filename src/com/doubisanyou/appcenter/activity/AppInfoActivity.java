package com.doubisanyou.appcenter.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.date.Config;
import com.doubisanyou.baseproject.network.ConnectMethd;
import com.doubisanyou.baseproject.network.NetConnect;
import com.doubisanyou.baseproject.network.NetConnect.FailCallBack;
import com.doubisanyou.baseproject.network.NetConnect.SuccessCallBack;

public class AppInfoActivity extends NearByPeopleActivity {
	
	TextView app_detail_name;
	TextView app_type;
	TextView app_size;
	TextView app_summary;
	TextView app_ver;
	TextView app_description;
	
	Button app_download;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.app_detail);
		
		}
	
	//1111
	//222
}
