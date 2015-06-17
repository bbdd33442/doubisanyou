package com.doubisanyou.baseproject.base;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.date.Config;
import com.doubisanyou.appcenter.widget.LoadingDialog;
import com.doubisanyou.baseproject.network.ConnectMethd;
import com.doubisanyou.baseproject.network.NetConnect;
import com.doubisanyou.baseproject.network.NetConnect.FailCallBack;
import com.doubisanyou.baseproject.network.NetConnect.SuccessCallBack;
import com.doubisanyou.baseproject.utilCommon.StringAndDataUtil;
 

/**
 * 一般基类 ，处理了自定义标题、底部toolbar、网络等待条等，另包含一些函数  
 */
public abstract class BaseActivity extends FragmentActivity implements
		OnCancelListener, BaseActivityInterface { 
	// 显示进度条的任务
	private List<NetConnect> mProgressTaskList = new ArrayList<NetConnect>(); 
	// 隐藏任务
	private List<NetConnect> mBgTaskList = new ArrayList<NetConnect>();

	private ProgressDialog mProgressDialog;
	
	String rs;
	
	public NetConnect task;
	
	protected LoadingDialog carLoadingDialog;


/*	*//** 发送任务,并显示相应进度条 *//*
	public String sendTask(Context context,String parameter,String url) {
		return sendTask(context,parameter,url, ConnectMethd.POST);
	}*/
	/*public ProgressDialog pDlg;*/
	/** 发送任务,并显示相应进度条 */
/*	public String sendTask(Context context,String parameter,String url, ConnectMethd methed) {
		final ProgressDialog pd = new ProgressDialog(context).show(context, "链接","与服务器通信中...");
		
		pDlg = new ProgressDialog(this);
		pDlg.setOnCancelListener(this);
		pDlg.setTitle("提示");
		pDlg.setMessage("正在进行网络连接，请稍后...");
		pDlg.setCancelable(true);
		pDlg.setButton(getResources().getString(R.string.cancel),
				onClickDlgCancel);
		pDlg.setIndeterminate(true);
		pDlg.show();
		task = new NetConnect(url,methed,new SuccessCallBack() {
			
			@Override
			public void onSuccess(String result) {
				rs = result;
				pDlg.dismiss();
				
			}
		},new FailCallBack() {
			
			@Override
			public void onFail() {
				rs="错误！";
				pDlg.dismiss();
				
			}
		}, parameter);
		return rs;
	}
	*/
	
	/** 发送任务,选择是否显示进度条 *//*
	public void sendTask(NetConnect task, boolean showProgress) {
		if (showProgress) {
			mProgressTaskList.add(task);
			if (mProgressDialog == null) {
				ProgressDialog pDlg;
				pDlg = new ProgressDialog(this);
				pDlg.setOnCancelListener(this);
				pDlg.setTitle("提示");
				pDlg.setMessage("正在进行网络连接，请稍后...");
				pDlg.setCancelable(true);
				pDlg.setButton(getResources().getString(R.string.cancel),
						onClickDlgCancel);
				pDlg.setIndeterminate(true);
				mProgressDialog = pDlg;
				mProgressDialog.show();
			}
		} else {
			mBgTaskList.add(task);
		}
		task.setContext(this);
		task.send();
	}
*/
	public OnClickListener onClickDlgCancel = new OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.cancel();
		}
	};

	@Override
	public void onCancel(DialogInterface dialog) {
		task.cancelTask();
	}

	/**
	 * 得到默认的prefercences 应用中的公共数据可以存储在此，如：用户名，密码等
	 * 
	 * @return
	 */
	public SharedPreferences getPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(this);
	}
	
	protected void showLoadingDialog() {
		carLoadingDialog.show();
	}
	
	protected void dissmissLoadingDialog() {
		carLoadingDialog.dismiss();
	}
/*
	*//** 处理服务器返回之错误代码 *//*
	public void handleResopnseCodeErr(int errCode, String errMsg) {
		int code;
		try {
			code = errCode;
		} catch (Exception e) {
			code = 0;
		}
		switch (code) {
		case 1:
			showToast(errMsg, Toast.LENGTH_LONG);
			break;
		default:
			break;
		}
		Log.w("ResopnseErr", "errCode:" + errCode + ",errMsg" + errMsg);
	}
*/
	/** 禁止编辑Filter */
	protected InputFilter[] mNoEditFilters = new InputFilter[] { new InputFilter() {
		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			return source.length() < 1 ? dest.subSequence(dstart, dend) : "";
		}
	} };

	/**
	 * 弹出提示
	 * 
	 * @param text
	 */
	public void showAlert(String text) {
		try {
			new AlertDialog.Builder(this)
					.setTitle("提示")
					.setMessage(text)
					.setNeutralButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							}).show();

		} catch (Exception e) { 
			e.printStackTrace();
		}

	}

	/**
	 * 弹出提示
	 * 
	 * @param text
	 */
	public void showAlert(String title, String text) {
		try {
			new AlertDialog.Builder(this)
					.setTitle(title)
					.setMessage(text)
					.setNeutralButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							}).show();
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}

	/**
	 * 弹出一个显示的Toast
	 * 
	 * @param text
	 */
	public void showToast(String text, int timeLength) {
		Toast.makeText(this, text, timeLength).show();
	}
	
	public void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
	

	/**
	 * 
	 */
	public Context getContext() {
		return this;
	}


	protected void onResume() {
		super.onResume(); 
	}

	 
	protected void onPause() {
		super.onPause(); 
	}
	


	/**
	 * 处理消息
	 */
	protected Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 一些公共的消息可以在这里处理，如一些错误处理
			boolean ret = BaseActivity.this.handleMessage(msg);
			if (!ret) {

			}
		}
	};
	

	/**
	 * 子类需要重写该函数来处理网络消息,一般是发送消息接收到的回应
	 * 
	 * @param msg
	 *            接收到的消息
	 * @return 如果返回false则未对该消息做响应，将传递给上层系统做默认处理
	 */
	public boolean handleMessage(Message msg) {
		return false;
	}
	
	public Handler getHandler() {
		return mHandler;
	}
}
