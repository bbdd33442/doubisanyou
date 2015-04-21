package com.doubisanyou.appcenter.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.bean.AppInfo;

public class NearByPeopleActivity extends Activity  {

	ListView app_list = null;
	TextView app_list_title=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app);

		final List<AppInfo> appinfs = new ArrayList<AppInfo>();
		AppInfo a1 = new AppInfo();
		a1.appName = "淘宝";
		a1.organization = "北京";
		a1.appCategory = "1";

		appinfs.add(a1);

		app_list = (ListView) findViewById(R.id.app_list);
		app_list_title = (TextView) findViewById(R.id.life_default_title);
		app_list_title.setText("应用列表");
		final ArrayList<HashMap<String, Object>> appItem = new ArrayList<HashMap<String, Object>>();

		for (Iterator iterator = appinfs.iterator(); iterator.hasNext();) {
			AppInfo appinfo = (AppInfo) iterator.next();

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("appName", appinfo.appName);
			map.put("cate", appinfo.appCategory);
			map.put("org", appinfo.organization);
			appItem.add(map);
		}

		SimpleAdapter sa = new SimpleAdapter(this, appItem, R.layout.app_item,
				new String[] { "appName", "cate", "org" }, new int[] {
						R.id.app_name, R.id.app_cate, R.id.app_org });

		app_list.setAdapter(sa);

		app_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent it = new Intent(NearByPeopleActivity.this, AppInfoActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable("app", appinfs.get(arg2));
				it.putExtras(mBundle);
				startActivity(it);
			}
		});
	}

	
}
