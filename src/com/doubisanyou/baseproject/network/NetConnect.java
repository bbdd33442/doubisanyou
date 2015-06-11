package com.doubisanyou.baseproject.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;

import com.doubisanyou.appcenter.date.Config;

public class NetConnect {
	HttpURLConnection uc=null;
	public NetConnect(final String url,SuccessCallBack successcallback,FailCallBack failcallback,String params){
		this(url,ConnectMethd.POST,successcallback,failcallback,params);
	}
	
	public NetConnect(final String url,final ConnectMethd methed,final SuccessCallBack successcallback,final FailCallBack failcallback,final String params){
		
	
		new AsyncTask<Void, Void, String>(){


			@Override
			protected String doInBackground(Void... v) {
				try {
					switch (methed) {
					case POST:
						uc= (HttpURLConnection) new URL(url).openConnection();
						uc.setDoOutput(true);
						uc.setRequestProperty("Content-type", "text/plain");
						BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(uc.getOutputStream(),Config.CHARSET_UTF8));
						bw.write(params);
						bw.flush();
						break;
					default:
						uc= (HttpURLConnection) new URL(url+"?"+params).openConnection();
						break;
					}
					 System.out.println("请求的url:"+uc.getURL());
					 System.out.println("参数为:"+params);
					 System.out.println(uc.getResponseCode());
					 BufferedReader bd = new BufferedReader(new InputStreamReader(uc.getInputStream(),Config.CHARSET_UTF8));
					 String line;
					 StringBuffer result = new StringBuffer();
					 while((line=bd.readLine())!=null){
						 result.append(line);
					 }
					 System.out.println("返回的结果:"+result.toString());
					 return result.toString();
				} catch (MalformedURLException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return null;
			}
			
			
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if(result!=null){
					successcallback.onSuccess(result);
				}else{
					failcallback.onFail();
				}
			}
			
		}.execute();
	}
	public void cancelTask(){
		uc.disconnect();
	}

	public interface SuccessCallBack{
		public void onSuccess(String result);
	}
	
	public interface FailCallBack{
		public void onFail();
	}
}
