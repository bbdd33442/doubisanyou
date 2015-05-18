package com.doubisanyou.appcenter.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.adapter.TeaSayAdapter;
import com.doubisanyou.appcenter.bean.TeaSay;
import com.doubisanyou.appcenter.widget.LoadingDialog;
import com.doubisanyou.appcenter.widget.PopMenu;
import com.doubisanyou.appcenter.widget.PullToRefreshBase.OnRefreshListener;
import com.doubisanyou.appcenter.widget.PullToRefreshListView;
import com.doubisanyou.baseproject.base.BaseActivity;
/**
 * 茶说模块
 * @author xy 2015/04/22
 *
 */
public class TeaSayActivity extends BaseActivity implements OnClickListener{
	
	private int mLoadingTpye = LOAGDING_NORMAL;
	private final static int LOAGDING_NORMAL = 0;
	private final static int LOAGDING_REFRESH = 1;
	private final static int LOAGDING_LOADING_MORE = 2;
	
	private	int	pagesize=10;
	private	int	pageNumber	=1;
	
	
	
	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;
	private ArrayList<TeaSay> tys;
	private PopMenu popMenu;
	private TeaSayAdapter tsa;
	private ImageButton rightBtn;
	private TextView titleBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tea_say);
		iniView();
	}
	
	void iniView(){
		carLoadingDialog = new LoadingDialog(this);
		tys = new ArrayList<TeaSay>();
		tsa=new TeaSayAdapter(getApplicationContext(),tys,TeaSayActivity.this);
		popMenu = new PopMenu(TeaSayActivity.this);
		
	    mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.tea_say_list);
	    mPullRefreshListView.setOnRefreshListener(mOnrefreshListener);
	    mPullRefreshListView.setUpRefreshEnabled(true);
	    mListView = mPullRefreshListView.getRefreshableView();
		mListView.setAdapter(tsa);
	    
	    titleBar = (TextView) findViewById(R.id.default_title);
	    titleBar.setText("茶说");
	
		rightBtn = (ImageButton)findViewById(R.id.btn_right);
		rightBtn.setVisibility(View.VISIBLE);
	
		rightBtn.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		Intent i = new Intent(this,TeaSayPublishActivity.class);
		switch (v.getId()) {
		case R.id.btn_right:
			popMenu.showAsDropDown(v);
			break;
		case R.id.pop_menu_only_text:
			i.putExtra(TeaSayPublishActivity.PUBLISHTYPE, TeaSayPublishActivity.TEXT);
			startActivity(i);
			popMenu.dismiss();
		case R.id.pop_menu_with_image:
			i.putExtra(TeaSayPublishActivity.PUBLISHTYPE, TeaSayPublishActivity.IMAGE);
			startActivity(i);
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
	
	
}
