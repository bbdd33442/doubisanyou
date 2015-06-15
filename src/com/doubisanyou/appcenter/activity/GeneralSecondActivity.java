package com.doubisanyou.appcenter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.bean.TeaGeneral;
import com.doubisanyou.appcenter.bean.TeaHistory;

public class GeneralSecondActivity extends Activity{

	private TextView title;
	private TextView tea_general_name;
	private TextView tea_general_function;
	private TextView tea_general_type;

	public static final String TEAGENERAL = "teaGeneral";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tea_general_second);
		Button back = (Button) findViewById(R.id.btn_left);
		back.setVisibility(View.VISIBLE);
		TeaGeneral tk = (TeaGeneral) getIntent().getSerializableExtra(
				TEAGENERAL);
		title = (TextView) this.findViewById(R.id.default_title);
		title.setText(tk.tea_general_title);

		tea_general_name = (TextView) this.findViewById(R.id.content);
		tea_general_name.setText("茶叶命名是茶叶分类的重要程序之一。一种茶叶必须有一个名称以为标志。不论作为分类研究或实际应用，茶叶皆有一专门名称。命名与分类可以联系一起，如工夫红茶，前者是命名，后者是分类；又如白毫银针或岩茶水仙，前者是分类，后者是命名。茶叶名称通常都带有描写性，名称文雅也是其他商品所不及的。");

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View source) {
				finish();
			}
		});
	}
}