package com.doubisanyou.appcenter.activity;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.bean.TeaKnowledge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SearchThirdActivity extends Activity {
	private TextView title;
	private TextView name;
	private TextView simpleName;
	private TextView productArea;
	private TextView type;
	private TextView introduce;
	private TextView brew;
	private TextView quality;

	public static final String TEAKNOWLEDGE = "teaknowledge";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tea_search_third);
		Button back = (Button) findViewById(R.id.btn_left);
		back.setVisibility(View.VISIBLE);
		TeaKnowledge tk = (TeaKnowledge) getIntent().getSerializableExtra(
				TEAKNOWLEDGE);
		title = (TextView) this.findViewById(R.id.default_title);
		title.setText(tk.tea_knowledge_name);

		name = (TextView) this.findViewById(R.id.tv01);
		name.setText("祁门红茶");
		
		simpleName = (TextView) this.findViewById(R.id.tv02);
		simpleName.setText("祁红");
		
		productArea = (TextView) this.findViewById(R.id.tv03);
		productArea.setText("祁门、池州");
		
		type = (TextView) this.findViewById(R.id.tv04);
		type.setText("红茶");
		
		introduce = (TextView) this.findViewById(R.id.bn);
		introduce.setText("这里是简介啦啦啦");
		
		brew = (TextView) this.findViewById(R.id.brewinfo);
		brew.setText("这里是冲泡方法啦啦啦");
		
		quality = (TextView) this.findViewById(R.id.qualityinfo);
		quality.setText("这里是质量评审啦啦啦");
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View source) {
				finish();
			}
		});
	}
}
