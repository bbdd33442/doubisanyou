package com.doubisanyou.appcenter.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.adapter.SimpleAdapter;
import com.doubisanyou.appcenter.bean.TeaKnowledge;
import com.doubisanyou.appcenter.date.Config;
import com.doubisanyou.appcenter.widget.LoadingDialog;
import com.doubisanyou.appcenter.widget.PullToRefreshBase.OnRefreshListener;
import com.doubisanyou.appcenter.widget.PullToRefreshListView;
import com.doubisanyou.baseproject.network.NetConnect;
import com.doubisanyou.baseproject.network.NetConnect.FailCallBack;
import com.doubisanyou.baseproject.network.NetConnect.SuccessCallBack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/* carLoadingDialog.show(); */
// TODO Auto-generated method stub
// map.put("", xxx.cccc);
// 重写选项被选中事件的处理方法
// 重写选项被单击事件的处理方法
public class SearchSecondActivity extends Activity {

	public static final String TEATYPE = "teatype";
	public static final String TEAINFO = "teainfo";
	public static final int REDTEA = 1;
	public static final int GREENTEA = 2;
	public static final int BLACKTEA = 3;
	public static final int WHITETEA = 4;
	public static final int YELLOWTEA = 5;
	public static final int CYANTEA = 6;
	public static final int HEALTHTEA = 7;
	public static final int FLOWERTEA = 8;

	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> replyItem;
	private PullToRefreshListView mPullRefreshListView;
	SimpleAdapter sla;
	private ListView mListView;
	LoadingDialog carLoadingDialog;
	ListView lv;
	List<TeaKnowledge> teaKnowledges = new ArrayList<TeaKnowledge>();;
	private int mLoadingTpye = LOAGDING_LOADING_MORE;
	private final static int LOAGDING_NORMAL = 0;
	private final static int LOAGDING_REFRESH = 1;
	private final static int LOAGDING_LOADING_MORE = 2;

	private int pagesize = 10;
	private int pageNumber = 1;

	private TextView title;
	private TextView info;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		carLoadingDialog = new LoadingDialog(this);
		setContentView(R.layout.tea_search_second);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.task_list);
		mPullRefreshListView.setOnRefreshListener(mOnrefreshListener);
		mListView = mPullRefreshListView.getRefreshableView();
		/* carLoadingDialog.show(); */
		mPullRefreshListView.setUpRefreshEnabled(true);

		title = (TextView) this.findViewById(R.id.default_title);

		title.setText("茶知识");

		Button back = (Button) findViewById(R.id.btn_left);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		lv = mPullRefreshListView.getRefreshableView();
		int d = getIntent().getIntExtra(TEATYPE, 0);
	    getList(d);
	}
	 void getList(int type){
			info = (TextView) this.findViewById(R.id.bn);
			info.setText(getIntent().getStringExtra(
					SearchSecondActivity.TEAINFO));
			NetConnect task = new NetConnect(Config.SERVICE_URL+"mobile/teaknowledge/getlist?type="+type,new SuccessCallBack() {
				@Override
				public void onSuccess(String result) {
					Gson gson = new Gson();
					List<TeaKnowledge> tkls = gson.fromJson(result,  
			                new TypeToken<List<TeaKnowledge>>() {  
			                }.getType()); 
					iniList(tkls);
				}
			},new FailCallBack() {
				@Override
				public void onFail() {
					
				}
			},"");
	 }
	void iniList(final List<TeaKnowledge> tkls){
		replyItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < tkls.size(); i++) {
			map = new HashMap<String, Object>();
			map.put("searchImage",  tkls.get(i).tea_knowledge_pic);
			map.put("searchContent", tkls.get(i).tea_knowledge_name);
			replyItem.add(map);
		}

		// map.put("", xxx.cccc);

		sla = new SimpleAdapter(getApplicationContext(), replyItem,
				R.layout.listitem_tea_search_second, new String[] {
						"searchImage", "searchContent" }, new int[] {
						R.id.tea_search_second_image,
						R.id.tea_search_second_content });

		lv.setAdapter(sla);

		lv.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {// 重写选项被选中事件的处理方法
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {// 重写选项被单击事件的处理方法
				TeaKnowledge c = tkls.get(arg2 - 1);
				Intent intent = new Intent(SearchSecondActivity.this,
						SearchThirdActivity.class);
				intent.putExtra(SearchThirdActivity.TEAKNOWLEDGE, c);
				startActivity(intent);
			}
		});
	}
	
	OnRefreshListener mOnrefreshListener = new OnRefreshListener() {
		public void onRefresh() {
			switch (mPullRefreshListView.getRefreshType()) {
			case LOAGDING_REFRESH:
				mLoadingTpye = LOAGDING_REFRESH;
				pageNumber = 1;
				mPullRefreshListView.setUpRefreshEnabled(true);
				mPullRefreshListView.onRefreshComplete();
				break;
			case LOAGDING_LOADING_MORE:
				pageNumber++;
				mLoadingTpye = LOAGDING_LOADING_MORE;
				mPullRefreshListView.setUpRefreshEnabled(true);
				mPullRefreshListView.onRefreshComplete();
				break;
			default:
				break;
			}

		}
	};

}