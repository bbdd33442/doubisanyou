package com.doubisanyou.appcenter.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.adapter.FragmentAdapter;
import com.doubisanyou.appcenter.bean.EBEvents;
import com.doubisanyou.appcenter.fragment.TeaAddressListFragment;
import com.doubisanyou.appcenter.fragment.TeaChatListFragment;
import com.doubisanyou.appcenter.service.XmppService;
import com.doubisanyou.baseproject.base.BaseActivity;

import de.greenrobot.event.EventBus;

public class TeaChatActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = TeaChatActivity.class.getSimpleName();
	public static String JID; // openfire id
	public static String ACCOUNT_NAME; // 账户名
	private List<Fragment> mFragmentList = new ArrayList<Fragment>();
	private TeaChatListFragment mChatListFragment;
	private TeaAddressListFragment mAddressListFragment;
	private FragmentAdapter mFragmentAdapter;
	private TextView teaChatTv, teaContactTv;
	private ViewPager mViewPager;
	/**
	 * ViewPager的当前选中页
	 */
	private int currentIndex;
	/**
	 * 屏幕的宽度
	 */
	private int screenWidth;
	private ImageView mTabLineIv;
	private TextView titile;
	private ImageButton addFriendBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tea_chat);
//		startService(new Intent(this, XmppService.class));
		initView();
		initTabLineWidth();
	}

	@Override
	protected void onDestroy() {
		stopService(new Intent(this, XmppService.class));
		super.onDestroy();
	}

	private void initView() {
		teaChatTv = (TextView) findViewById(R.id.tea_chat_tv);
		teaContactTv = (TextView) findViewById(R.id.tea_contact_tv);
		mTabLineIv = (ImageView) findViewById(R.id.tea_chat_tabline_iv);
		mViewPager = (ViewPager) findViewById(R.id.chat_layout_vp);
		titile = (TextView) findViewById(R.id.default_title);
		titile.setText("茶聊");
		addFriendBtn = (ImageButton) findViewById(R.id.btn_right);
		addFriendBtn.setVisibility(View.VISIBLE);
		addFriendBtn.setOnClickListener(this);
		teaChatTv.setOnClickListener(this);
		teaContactTv.setOnClickListener(this);
		mChatListFragment = new TeaChatListFragment();
		mAddressListFragment = new TeaAddressListFragment();

		mFragmentList.add(mChatListFragment);
		mFragmentList.add(mAddressListFragment);

		mFragmentAdapter = new FragmentAdapter(
				this.getSupportFragmentManager(), mFragmentList);
		mViewPager.setAdapter(mFragmentAdapter);
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

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

				// Log.d("offset:", offset + "");
				/**
				 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
				 * 设置mTabLineIv的左边距 滑动场景： 记3个页面, 从左到右分别为0,1,2 0->1; 1->2; 2->1;
				 * 1->0
				 */

				if (currentIndex == 0 && position == 0)// 0->1
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 2) + currentIndex
							* (screenWidth / 2));

				} else if (currentIndex == 1 && position == 0) // 1->0
				{
					lp.leftMargin = (int) (-(1 - offset)
							* (screenWidth * 1.0 / 2) + currentIndex
							* (screenWidth / 2));

				}
				mTabLineIv.setLayoutParams(lp);
			}

			@Override
			public void onPageSelected(int position) {
				resetTextView();
				switch (position) {
				case 0:
					teaChatTv.setTextColor(Color.BLUE);
					break;
				case 1:
					teaContactTv.setTextColor(Color.BLUE);
					break;
				}
				currentIndex = position;
			}
		});
	}

	private void initTabLineWidth() {
		DisplayMetrics dpMetrics = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(dpMetrics);
		screenWidth = dpMetrics.widthPixels;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
				.getLayoutParams();
		lp.width = screenWidth / 2;
		mTabLineIv.setLayoutParams(lp);
	}

	private void resetTextView() {
		teaChatTv.setTextColor(Color.BLACK);
		teaContactTv.setTextColor(Color.BLACK);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tea_chat_tv:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.tea_contact_tv:
			mViewPager.setCurrentItem(1);
			break;
		case R.id.btn_right:
			// startActivity(new Intent(this, AddFriendActivity.class));
			showAddFriendDialog(this);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onStart() {
		super.onStart();
		if (!XmppService.IS_LOGIN) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}
	}
/*	@Override
	protected void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onStop() {
		EventBus.getDefault().unregister(this);
		super.onStop();
	}*/

/*	public void onEventMainThread(
			EBEvents.ReceiveChatMsgEvent receiveChatMsgEvent) {
		String from = receiveChatMsgEvent.getChatMsgTransferEntity().getFrom();
		Log.i(TAG, from);
	}*/

	private void showAddFriendDialog(Context context) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(R.layout.dialog_add_friend, null);
		final EditText jidEt = (EditText) v
				.findViewById(R.id.add_friend_jid_et);
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle("加好友");
		dialog.setView(v);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				Log.i(TAG, "确定");
				EBEvents.AddFriendEvent addFriendEvent = EBEvents
						.instanceAddFriendEvent();
				addFriendEvent.setJid(jidEt.getText().toString());
				EventBus.getDefault().post(addFriendEvent);
			}
		});
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Log.i(TAG, "取消");
			}
		});
		dialog.create();
		dialog.show();
	}
}