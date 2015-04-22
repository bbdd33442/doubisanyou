package com.doubisanyou.appcenter.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.widget.PullToRefreshBase.OnRefreshListener;
import com.doubisanyou.appcenter.widget.*;

public class SearchSecondActivity extends Activity {

	int[] drawableIds = { R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher };
	int[] msgIds = { R.string.hello, R.string.hello, R.string.hello,
			R.string.hello };

	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;
	LoadingDialog carLoadingDialog;
	private int mLoadingTpye = LOAGDING_LOADING_MORE;
	private final static int LOAGDING_NORMAL = 0;
	private final static int LOAGDING_REFRESH = 1;
	private final static int LOAGDING_LOADING_MORE = 2;

	private int pagesize = 10;
	private int pageNumber = 1;

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

		Button previous = (Button) findViewById(R.id.previous);
		Button back = (Button) findViewById(R.id.btn_left);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		ListView lv = mPullRefreshListView.getRefreshableView();
		BaseAdapter ba = new BaseAdapter() {
			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public int getCount() {
				return drawableIds.length;
			}

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {

				LinearLayout ll = new LinearLayout(SearchSecondActivity.this);
				ll.setOrientation(LinearLayout.HORIZONTAL); // 设置朝向
				ll.setPadding(3, 3, 3, 3);// 设置四周留白
				ImageView ii = new ImageView(SearchSecondActivity.this);
				ii.setImageDrawable(getResources().getDrawable(
						drawableIds[arg0]));// 设置图片
				ii.setScaleType(ImageView.ScaleType.FIT_XY);
				ll.addView(ii);// 添加到LinearLayout中
				TextView tv = new TextView(SearchSecondActivity.this);
				tv.setText(getResources().getText(msgIds[arg0]));// 设置内容
				tv.setTextSize(24);// 设置字体大小
				tv.setPadding(5, 5, 5, 5);// 设置四周留白
				tv.setGravity(Gravity.LEFT);
				ll.addView(tv);// 添加到LinearLayout中
				return ll;
			}
		};
		lv.setAdapter(ba);// 为ListView设置内容适配器
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
				Intent intent = new Intent(SearchSecondActivity.this,
						SearchThirdActivity.class);
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