package com.doubisanyou.appcenter.activity;


import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.service.XmppService;
import com.doubisanyou.baseproject.base.BaseActivity;



public class SplashScreen extends BaseActivity {

	/*UserInfo user;
	*/
	@Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        setContentView(R.layout.splash_screen);
        startService(new Intent(this, XmppService.class));
        new Handler().postDelayed(new Runnable() {  
            @Override  
            public void run() {  
            	  Intent mainIntent = new Intent(SplashScreen.this, BoxMainActivity.class);
                  SplashScreen.this.startActivity(mainIntent);
                  SplashScreen.this.finish(); 
            }  
  
        }, 3000);  
      
    }
    
/*	
	
	// 调用服务器接口
		private void linkNetWork() { 
			HashMap<String, String> rigisterHM = new HashMap<String, String>();
			rigisterHM.put("UUID", "10");
			rigisterHM.put("isPad", "0");
			rigisterHM.put("serviceId", "GET_GUEST_TOKEN");
			rigisterHM.put("userName","");
			rigisterHM.put("password","");
			
			NetTask registerTask = new NetTask(
					ProtocalCommon
							.getProtocalByPID(ProtocalCommon.SYS_POST_LOGININFO),
					rigisterHM, "/mds/sinotrans/Phone_login") {
				@Override
				protected void onResponse(NetTask task, Object result) {
					user = (UserInfo) result;
					CommonContent.CATCH.put("defaultuser",user);
					Intent mainIntent = new Intent(SplashScreen.this, BoxMainActivity.class);
		            SplashScreen.this.startActivity(mainIntent);
		            SplashScreen.this.finish();
					
				}
			};
			
			if (NetUtil.checkNet(getApplicationContext())) {
				sendTask(registerTask,false);
			} else {
				Toast.makeText(getApplicationContext(), "网络未开启", 1).show();
			}
		}*/
		
}