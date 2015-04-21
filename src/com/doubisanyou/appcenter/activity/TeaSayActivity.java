package com.doubisanyou.appcenter.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.adapter.TeaSayAdaptor;
import com.doubisanyou.appcenter.bean.TeaSay;
import com.doubisanyou.appcenter.widget.LoadingDialog;
import com.doubisanyou.appcenter.widget.PopMenu;
import com.doubisanyou.appcenter.widget.PullToRefreshBase.OnRefreshListener;
import com.doubisanyou.appcenter.widget.PullToRefreshListView;
import com.doubisanyou.baseproject.base.BaseActivity;

public class TeaSayActivity extends BaseActivity implements OnClickListener,OnItemClickListener{
	
	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;
	private ArrayList<TeaSay> tys;
	private PopMenu popMenu;
	private TeaSayAdaptor tsa;
	public ImageButton rightBtn;
	private int mLoadingTpye = LOAGDING_NORMAL;
	private final static int LOAGDING_NORMAL = 0;
	private final static int LOAGDING_REFRESH = 1;
	private final static int LOAGDING_LOADING_MORE = 2;
	
	private	int	pagesize=10;
	private	int	pageNumber	=1;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tea_say);
		carLoadingDialog = new LoadingDialog(this);
		
		tys = new ArrayList<TeaSay>();
		
		tsa=new TeaSayAdaptor(getApplicationContext(), TeaSayActivity.this, tys);
	    mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.task_list);
		mPullRefreshListView.setOnRefreshListener(mOnrefreshListener);
		
		mListView = mPullRefreshListView.getRefreshableView();
		
		mListView.setOnItemClickListener(this);
		
		mPullRefreshListView.setUpRefreshEnabled(true);
		mListView.setAdapter(tsa);

		popMenu = new PopMenu(TeaSayActivity.this);
	
		rightBtn = (ImageButton)findViewById(R.id.btn_right);
	
	
		rightBtn.setOnClickListener(this);
		/*carLoadingDialog.show();*/
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_left:
			  this.finish();
			break;
		case R.id.btn_right:
			popMenu.showAsDropDown(v);
			break;
		case R.id.pop_menu_1:
			popMenu.dismiss();
		case R.id.pop_menu_2:
			popMenu.dismiss();
		case R.id.pop_menu_3:
			popMenu.dismiss();
		default:
			break;
		}
		
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
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Bundle bundle = new Bundle();
	System.out.println(position);
	/*	bundle.putString(TaskDetailAcitivity.TASK_ID, mTasks.get(position-1).getObjectId());
		Intent intent = new Intent(this,TaskDetailAcitivity.class);
		intent.putExtra(TaskDetailAcitivity.Bundle_ID, bundle);
		startActivity(intent);*/
	}
}
