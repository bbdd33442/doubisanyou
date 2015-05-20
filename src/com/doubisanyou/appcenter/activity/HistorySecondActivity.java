package com.doubisanyou.appcenter.activity;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.bean.TeaHistory;
import com.doubisanyou.appcenter.bean.TeaKnowledge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HistorySecondActivity extends Activity {

	private TextView title;
	private TextView tea_history_origin;
	private TextView tea_history_develop;
	private TextView tea_history_legend;

	public static final String TEAHISTORY = "teaHistory";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tea_history_second);
		Button back = (Button) findViewById(R.id.btn_left);
		back.setVisibility(View.VISIBLE);
		TeaHistory tk = (TeaHistory) getIntent().getSerializableExtra(
				TEAHISTORY);
		title = (TextView) this.findViewById(R.id.default_title);
		title.setText(tk.tea_history_origin);

		tea_history_origin = (TextView) this.findViewById(R.id.content);
		tea_history_origin.setText("中国茶文化源远流长，巴蜀常被称为中国茶业和茶文化的摇篮。六朝以前的茶史资料表明，中国的茶业最初兴起于巴蜀。茶叶文化的形成，与巴蜀地区早期的政治、风俗及茶叶饮用有着密切的关系。");

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View source) {
				finish();
			}
		});
	}
}
