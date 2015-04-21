package com.doubisanyou.appcenter.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.doubisanyou.appcenter.R;

public class PopMenu {
	private Context context;
	private PopupWindow popupWindow ;
	private ListView listView;
	//private OnItemClickListener listener;
	

	public PopMenu(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		
		View view = LayoutInflater.from(context).inflate(R.layout.popmenu, null);
		view.findViewById(R.id.pop_menu_1).setOnClickListener((OnClickListener) context);
		view.findViewById(R.id.pop_menu_2).setOnClickListener((OnClickListener) context);
		view.findViewById(R.id.pop_menu_3).setOnClickListener((OnClickListener) context);
        popupWindow = new PopupWindow(view, 100, LayoutParams.WRAP_CONTENT);
        popupWindow = new PopupWindow(view, 
        		context.getResources().getDimensionPixelSize(R.dimen.popmenu_width), 
        		LayoutParams.WRAP_CONTENT);
        
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	//设置菜单项点击监听器
	public void setOnItemClickListener(OnItemClickListener listener) {
		//this.listener = listener;
		listView.setOnItemClickListener(listener);
	}

	//下拉式 弹出 pop菜单 parent 右下角
	public void showAsDropDown(View parent) {
		popupWindow.showAsDropDown(parent, 10, 
				//保证尺寸是根据屏幕像素密度来的
				context.getResources().getDimensionPixelSize(R.dimen.popmenu_yoff));
		
		// 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        //刷新状态
        popupWindow.update();
	}
	
	//隐藏菜单
	public void dismiss() {
		popupWindow.dismiss();
	}

}
