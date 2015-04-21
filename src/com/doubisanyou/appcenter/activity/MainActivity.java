package com.doubisanyou.appcenter.activity;



import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.widget.LoadingDialog;
import com.doubisanyou.appcenter.widget.PullToRefreshBase.OnRefreshListener;
import com.doubisanyou.appcenter.widget.PullToRefreshListView;
import com.doubisanyou.baseproject.base.BaseActivity;


public class MainActivity extends BaseActivity {

	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;

	private int mLoadingTpye = LOAGDING_LOADING_MORE;
	private final static int LOAGDING_NORMAL = 0;
	private final static int LOAGDING_REFRESH = 1;
	private final static int LOAGDING_LOADING_MORE = 2;
	
	private	int	pagesize=10;
	private	int	pageNumber	=1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.pull_to_refresh_test);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.task_list);
		mPullRefreshListView.setOnRefreshListener(mOnrefreshListener);
		mListView = mPullRefreshListView.getRefreshableView();
		/*carLoadingDialog.show();*/
		mPullRefreshListView.setUpRefreshEnabled(true);
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
				pageNumber ++;
				mLoadingTpye = LOAGDING_LOADING_MORE;
				mPullRefreshListView.setUpRefreshEnabled(true);
				mPullRefreshListView.onRefreshComplete();
				break;
			default:
				break;
			}

		}
	};

	
/*	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Bundle bundle = new Bundle();
		bundle.putString(TaskDetailAcitivity.TASK_ID, mTasks.get(position-1).getObjectId());
		Intent intent = new Intent(this,TaskDetailAcitivity.class);
		intent.putExtra(TaskDetailAcitivity.Bundle_ID, bundle);
		startActivity(intent);
	}*/
}
