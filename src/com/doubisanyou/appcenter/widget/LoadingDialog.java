package com.doubisanyou.appcenter.widget;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.doubisanyou.appcenter.R;



public class LoadingDialog extends Dialog {
	public static final String LOGTAG = "LoadingDialog";

	private static final double SCALE = 0.85;

	public LoadingDialog(	Context context) {
		
		super(context, 0);
		if (context != null) {
			initUI(context);
		}
		setCancelable(false);
	}


	@SuppressWarnings("deprecation")
	private void initUI(Context context) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(false);
		View view = getLayoutInflater().cloneInContext(context).inflate(R.layout.dialog_loading, null);
		setContentView(view);
		WindowManager windowManager =((Activity) context).getWindowManager();
		Display display = windowManager.getDefaultDisplay();

		Window window = getWindow();
		WindowManager.LayoutParams attributes = window.getAttributes();
		attributes.gravity = Gravity.CENTER;
		attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
		attributes.width = (int) (display.getWidth() * SCALE);
		window.setAttributes(attributes);
	}
}