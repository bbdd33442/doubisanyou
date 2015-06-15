package com.doubisanyou.appcenter.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.bean.TeaKnowledge;
import com.doubisanyou.baseproject.utilsResource.BitmapUtil;
import com.doubisanyou.baseproject.utilsResource.ImageLoader;
import com.doubisanyou.baseproject.utilsResource.ImageLoader.Type;

public class SearchThirdActivity extends Activity {
	private TextView title;
	private TextView name;
	private TextView simpleName;
	private TextView productArea;
	private TextView type;
	private TextView introduce;
	private TextView brew;
	private TextView quality;
    private ImageView pic;
	public static final String TEAKNOWLEDGE = "teaknowledge";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tea_search_third);
		Button back = (Button) findViewById(R.id.btn_left);
		back.setVisibility(View.VISIBLE);
		TeaKnowledge tk = (TeaKnowledge) getIntent().getSerializableExtra(
				TEAKNOWLEDGE);
		pic = (ImageView) findViewById(R.id.tea_pic);
		Bitmap bitmap = BitmapUtil.ReadBitmapById(getApplicationContext(), Integer.parseInt(tk.tea_knowledge_pic));
		pic.setImageBitmap(bitmap);
		title = (TextView) this.findViewById(R.id.default_title);
		title.setText(tk.tea_knowledge_name);

		name = (TextView) this.findViewById(R.id.tv01);
		name.setText(tk.tea_knowledge_name);

		simpleName = (TextView) this.findViewById(R.id.tv02);
		simpleName.setText(tk.tea_knowledge_simple_name);

		productArea = (TextView) this.findViewById(R.id.tv03);
		productArea.setText(tk.tea_knowledge_area);

		type = (TextView) this.findViewById(R.id.tv04);
		type.setText(tk.tea_knowledge_type);

		introduce = (TextView) this.findViewById(R.id.bn);
		introduce.setText(tk.tea_knowledge_introduce);

		brew = (TextView) this.findViewById(R.id.brewinfo);
		brew.setText(tk.tea_knowledge_brew);

		quality = (TextView) this.findViewById(R.id.qualityinfo);
		quality.setText(tk.tea_knowledge_quality);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View source) {
				finish();
			}
		});
	}
}
