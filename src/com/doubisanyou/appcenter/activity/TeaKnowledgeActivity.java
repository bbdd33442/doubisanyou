package com.doubisanyou.appcenter.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.adapter.FragmentAdapter;
import com.doubisanyou.appcenter.date.Config;
import com.doubisanyou.appcenter.fragment.TeaGeneralFragment;
import com.doubisanyou.appcenter.fragment.TeaHistoryFragment;
import com.doubisanyou.appcenter.fragment.TeaKnowledgeFragment;
import com.doubisanyou.appcenter.fragment.TeaSetFragment;
import com.doubisanyou.appcenter.widget.LoadingDialog;
import com.doubisanyou.baseproject.base.BaseActivity;
import com.doubisanyou.baseproject.network.ConnectMethd;
import com.doubisanyou.baseproject.network.NetConnect;
import com.doubisanyou.baseproject.network.NetConnect.FailCallBack;
import com.doubisanyou.baseproject.network.NetConnect.SuccessCallBack;

/**
 * @Title 首页
 * @Description 查询模块
 * @author xy
 * @date 2015-04-11
 * 
 */
public class TeaKnowledgeActivity extends BaseActivity implements OnClickListener {

	private List<Fragment> mFragmentList = new ArrayList<Fragment>();
	private FragmentAdapter mFragmentAdapter;

	private ViewPager mPageVp;
	/**
	 * Tab显示内容TextView
	 */
	private TextView teaKowledgeTilte, teaHistoryTitle, teaGeralTitle, teaSetTitle;
	private TextView title;
	/**
	 * Tab的那个引导线
	 */
	private ImageView mTabLineIv;
	/**
	 * Fragment
	 */
	private TeaKnowledgeFragment teaKnowledgeFg;
	private TeaHistoryFragment teaHistoryFg;
	private TeaGeneralFragment teaGeralFg;
	private TeaSetFragment teaSetfg;
	/**
	 * ViewPager的当前选中页
	 */
	private int currentIndex;
	/**
	 * 屏幕的宽度
	 */
	private int screenWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tea_knowledge);

		findById();
		init();
		initTabLineWidth();

	}
	
	private void findById() {
		carLoadingDialog = new LoadingDialog(this);
		teaKowledgeTilte = (TextView) this.findViewById(R.id.id_tea_knowledge_text);
		teaHistoryTitle = (TextView) this.findViewById(R.id.id_tea_history_text);
		teaGeralTitle = (TextView) this.findViewById(R.id.id_tea_general_text);
		teaSetTitle = (TextView) this.findViewById(R.id.id_teaset_text);
		
		title = (TextView) this.findViewById(R.id.default_title);
		
		title.setText("茶知识");
		
		teaGeralTitle.setOnClickListener(this);
		teaSetTitle.setOnClickListener(this);
		teaKowledgeTilte.setOnClickListener(this);
		teaHistoryTitle.setOnClickListener(this);
		mTabLineIv = (ImageView) this.findViewById(R.id.id_tab_line_iv);

		mPageVp = (ViewPager) this.findViewById(R.id.id_page_vp);
	}

	private void init() {
		teaKnowledgeFg = new TeaKnowledgeFragment();
		teaHistoryFg = new TeaHistoryFragment();
		teaGeralFg = new TeaGeneralFragment();
		teaSetfg = new TeaSetFragment();
		mFragmentList.add(teaKnowledgeFg);
		mFragmentList.add(teaHistoryFg);
		mFragmentList.add(teaGeralFg);
		mFragmentList.add(teaSetfg);
		mFragmentAdapter = new FragmentAdapter(
				this.getSupportFragmentManager(), mFragmentList);
		mPageVp.setAdapter(mFragmentAdapter);
		mPageVp.setCurrentItem(0);

		mPageVp.setOnPageChangeListener(new OnPageChangeListener() {

			/**
			 * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
			 */
			@Override
			public void onPageScrollStateChanged(int state) {

			}

			/**
			 * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
			 * offsetPixels:当前页面偏移的像素位置
			 */
			@Override
			public void onPageScrolled(int position, float offset,
					int offsetPixels) {
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
						.getLayoutParams();

				Log.e("offset:", offset + "");
				/**
				 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
				 * 设置mTabLineIv的左边距 滑动场景： 记3个页面, 从左到右分别为0,1,2 0->1; 1->2; 2->1;
				 * 1->0
				 */

				if (currentIndex == 0 && position == 0)// 0->1
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 4) + currentIndex
							* (screenWidth / 4));

				} else if (currentIndex == 1 && position == 0) // 1->0
				{
					lp.leftMargin = (int) (-(1 - offset)
							* (screenWidth * 1.0 / 4) + currentIndex
							* (screenWidth / 4));

				} else if (currentIndex == 1 && position == 1) // 1->2
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 4) + currentIndex
							* (screenWidth / 4));
				} else if (currentIndex == 2 && position == 1) // 2->1
				{
					lp.leftMargin = (int) (-(1 - offset)
							* (screenWidth * 1.0 / 4) + currentIndex
							* (screenWidth / 4));
				} else if (currentIndex == 2 && position == 2) // 2->3
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 4) + currentIndex
							* (screenWidth / 4));
				} else if (currentIndex == 3 && position == 2) // 3->2
				{
					lp.leftMargin = (int) (-(1 - offset)
							* (screenWidth * 1.0 / 4) + currentIndex
							* (screenWidth / 4));
				}
				mTabLineIv.setLayoutParams(lp);
			}

			@Override
			public void onPageSelected(int position) {
				resetTextView();
				switch (position) {
				case 0:
					teaKowledgeTilte.setTextColor(Color.BLUE);
					break;
				case 1:
					teaHistoryTitle.setTextColor(Color.BLUE);
					break;
				case 2:
					teaGeralTitle.setTextColor(Color.BLUE);
					break;
				case 3:
					teaSetTitle.setTextColor(Color.BLUE);
					break;
				}
				currentIndex = position;
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_tea_knowledge_text:
			mPageVp.setCurrentItem(0);
			break;
		case R.id.id_tea_history_text:
			mPageVp.setCurrentItem(1);
			break;
		case R.id.id_tea_general_text:
			mPageVp.setCurrentItem(2);
			break;
		case R.id.id_teaset_text:
			mPageVp.setCurrentItem(3);
			break;
		default:
			break;
		}

	}

	private void initTabLineWidth() {
		DisplayMetrics dpMetrics = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(dpMetrics);
		screenWidth = dpMetrics.widthPixels;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
				.getLayoutParams();
		lp.width = screenWidth / 4;
		mTabLineIv.setLayoutParams(lp);
	}

	private void resetTextView() {
		teaKowledgeTilte.setTextColor(Color.BLACK);
		teaGeralTitle.setTextColor(Color.BLACK);
		teaHistoryTitle.setTextColor(Color.BLACK);
		teaSetTitle.setTextColor(Color.BLACK);
	}

}
