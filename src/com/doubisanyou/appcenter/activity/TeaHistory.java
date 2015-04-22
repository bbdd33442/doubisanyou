package com.doubisanyou.appcenter.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.date.Config;
import com.doubisanyou.baseproject.network.ConnectMethd;
import com.doubisanyou.baseproject.network.NetConnect;
import com.doubisanyou.baseproject.network.NetConnect.FailCallBack;
import com.doubisanyou.baseproject.network.NetConnect.SuccessCallBack;

public class TeaHistory extends Fragment {
  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    super.onCreateView(inflater, container, savedInstanceState);
    View chatView = inflater.inflate(R.layout.tea_search_fragment, container,false);
    final TextView  x= (TextView) chatView.findViewById(R.id.test);
/*
    new NetConnect(Config.SERVICE_URL, ConnectMethd.GET, new SuccessCallBack() {
		
		@Override
		public void onSuccess(String result) {
			// TODO Auto-generated method stub
			System.out.println(result);
			x.setText(result+"2");
		}
	}, new FailCallBack() {
		
		@Override
		public void onFail() {
			// TODO Auto-generated method stub
			
		}
	}, "xx:1");*/
    
    return chatView;	
  }
  @Override
  public void onActivityCreated(Bundle savedInstanceState){
    super.onActivityCreated(savedInstanceState);
  }
}