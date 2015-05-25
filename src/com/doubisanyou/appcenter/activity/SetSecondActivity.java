package com.doubisanyou.appcenter.activity;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.bean.TeaHistory;
import com.doubisanyou.appcenter.bean.TeaKnowledge;
import com.doubisanyou.appcenter.bean.TeaSet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SetSecondActivity extends Activity {

	private TextView title;
	private TextView tea_set_name;
	private TextView tea_set_introduction;

	public static final String TEASET = "teaSet";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tea_set_second);
		Button back = (Button) findViewById(R.id.btn_left);
		back.setVisibility(View.VISIBLE);
		TeaSet tk = (TeaSet) getIntent().getSerializableExtra(
				TEASET);
		title = (TextView) this.findViewById(R.id.default_title);
		title.setText(tk.tea_set_introduction);

		tea_set_introduction = (TextView) this.findViewById(R.id.content);
		tea_set_introduction.setText("中国茶文化源远流长，巴蜀常被称为中国茶业和茶文化的摇篮。六朝以前的茶史资料表明，中国的茶业最初兴起于巴蜀。茶叶文化的形成，与巴蜀地区早期的政治、风俗及茶叶饮用有着密切的关系。");

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View source) {
				finish();
			}
		});
	}
}
