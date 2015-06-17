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
		title.setText(tk.tea_set_name);

		tea_set_introduction = (TextView) this.findViewById(R.id.content);
		tea_set_introduction.setText(tk.tea_set_introduction);

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View source) {
				finish();
			}
		});
	}
}
