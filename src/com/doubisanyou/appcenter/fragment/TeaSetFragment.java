package com.doubisanyou.appcenter.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.activity.SetSecondActivity;
import com.doubisanyou.appcenter.bean.TeaSet;
import com.doubisanyou.appcenter.widget.PullToRefreshBase.OnRefreshListener;
import com.doubisanyou.appcenter.widget.PullToRefreshListView;

public class TeaSetFragment extends Fragment {
	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> replyItem = new ArrayList<HashMap<String, Object>>();
	private PullToRefreshListView mPullRefreshListView;
	SimpleAdapter sla;
	private ListView mListView;
	List<TeaSet> teaSet = new ArrayList<TeaSet>();;
	private int mLoadingTpye = LOAGDING_LOADING_MORE;
	private final static int LOAGDING_NORMAL = 0;
	private final static int LOAGDING_REFRESH = 1;
	private final static int LOAGDING_LOADING_MORE = 2;

	private int pagesize = 10;
	private int pageNumber = 1;

	private TextView title;

	@Override
	public View onCreateView(LayoutInflater inflater,
			final ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View chatView = inflater.inflate(R.layout.tea_set_list, container,
				false);

		mPullRefreshListView = (PullToRefreshListView) chatView
				.findViewById(R.id.task_list);
		mPullRefreshListView.setOnRefreshListener(mOnrefreshListener);
		mListView = mPullRefreshListView.getRefreshableView();
		mPullRefreshListView.setUpRefreshEnabled(true);

		ListView lv = mPullRefreshListView.getRefreshableView();
		replyItem.clear();
		teaSet.clear();
		TeaSet tk = new TeaSet();
		tk.tea_set_name = "紫砂壶";
		teaSet.add(tk);
		//replyItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < teaSet.size(); i++) {
			map = new HashMap<String, Object>();
			map.put("searchImage", R.drawable.zishahu);
			map.put("searchContent", teaSet.get(i).tea_set_name);
			replyItem.add(map);
		}

		sla = new SimpleAdapter(container.getContext(), replyItem,
				R.layout.listitem_tea_set,
				new String[] { "searchImage","searchContent" },
				new int[] { R.id.tea_set_image,R.id.tea_set_content });

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
				TeaSet c = teaSet.get(arg2-1);
				Intent intent = new Intent(container.getContext(),
						SetSecondActivity.class);
				intent.putExtra(SetSecondActivity.TEASET, c);
				startActivity(intent);
			}
		});
		return chatView;
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}