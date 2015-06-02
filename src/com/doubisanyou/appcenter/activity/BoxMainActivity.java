package com.doubisanyou.appcenter.activity;
 
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;

public class BoxMainActivity extends TabActivity {

	public static TabHost tabHost;
	// 各tab的标题
	private String[] itemsName = new String[] { "茶知识", "聊茶", "茶说", "设置" };
	// 各个tab对应的activity页面
	private Class[] classes = new Class[] { TeaKnowledgeActivity.class,
			TeaChatActivity.class, TeaSayActivity.class,
			ManagerActivity.class };
	// tab选中时显示的图片
	private int[] tab_sel_png = new int[] { R.drawable.tea_knowledge_selected,
			R.drawable.chat_selected, R.drawable.tea_say_selected,
			R.drawable.set_selected };
	// tab未选中时显示的图片
	private int[] tab_unsel_png = new int[] { R.drawable.tea_knowledge_unselected,
			R.drawable.chat_unselected, R.drawable.tea_say_unselected,
			 R.drawable.set_unselected };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_home_main); 
		initView();
	}

	private void initView() {
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup(this.getLocalActivityManager());

		// 为tab添加图标和文字
		for (int i = 0; i < itemsName.length; i++) {
			if (i == 0) {
				addNewBotTab(i, tab_sel_png[i], classes[i], itemsName[i]);
			} else {
				addNewBotTab(i, tab_unsel_png[i], classes[i], itemsName[i]);
			}
		}
		
		final TabWidget tabwidget = tabHost.getTabWidget();
		tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			
			//当切换tab时，更改图标颜色和文字颜色
			@Override
			public void onTabChanged(String tabId) {
				 for (int i =0; i < tabwidget.getChildCount(); i++) {
					 LinearLayout tabIndicator = (LinearLayout) tabwidget.getChildAt(i);
					 ImageView iv = (ImageView)tabIndicator.findViewById(R.id.item_TabImg);
					 TextView tv = (TextView) tabIndicator.findViewById(R.id.item_TabName);
					 
					/* ImageView abLayout = (ImageView)tabIndicator.findViewById(R.id.tabimg_back_);*/
					 LayoutParams params = iv.getLayoutParams();
				      if(tabHost.getCurrentTab()==i){
				    	 /* abLayout.setVisibility(View.VISIBLE);*/
				    	  tv.setTextColor(getResources().getColor(R.color.green));
				    	  iv.setImageResource(tab_sel_png[i]);
				    	  LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(params.width,params.height);  
				    	  lParams.gravity = 17;
							iv.setLayoutParams(lParams); 
				      }
				      else {
				    	  iv.setImageResource(tab_unsel_png[i]);
				    	  tv.setTextColor(getResources().getColor(R.color.grey));
				    /*	  abLayout.setVisibility(View.INVISIBLE);*/
				    	  LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(params.width,params.height);  
				    	  lParams.gravity = 17;
				    	  iv.setLayoutParams(lParams);  
				      }
				 }     	
			}
		});
		
		tabHost.setCurrentTab(0);
	}
	
	//设置tab标签上的图标和文字
		public void addNewBotTab(int index,int resId, Class<?> cls, String tag){
	    	
			LinearLayout tabIndicator = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tab_indicator, tabHost.getTabWidget(), false);
			
			ImageView iv = (ImageView)tabIndicator.findViewById(R.id.item_TabImg);
			iv.setImageResource(resId);
			/* ImageView abLayout = (ImageView)tabIndicator.findViewById(R.id.tabimg_back_);*/
			 TextView tv = (TextView) tabIndicator.findViewById(R.id.item_TabName);
			 tv.setText(tag);
			 LayoutParams params = iv.getLayoutParams();
			if (index == 0) {
			/*	abLayout.setVisibility(View.VISIBLE);*/
				tv.setTextColor(getResources().getColor(R.color.green));
				LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(params.width,params.height);  
				lParams.gravity = 17;
					iv.setLayoutParams(lParams); 
			} else {
				/*abLayout.setVisibility(View.INVISIBLE);*/
				tv.setTextColor(getResources().getColor(R.color.grey));
				LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(params.width,params.height);  
				lParams.gravity = 17;
		    	iv.setLayoutParams(lParams);
			}
			
	        TabSpec tabSpec = tabHost.newTabSpec(tag)
				.setIndicator(tabIndicator)
				.setContent(new Intent(this,cls));
	        tabHost.addTab(tabSpec);
		}
}
