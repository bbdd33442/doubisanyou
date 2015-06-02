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
		tea_history_origin.setText("中国茶文化源远流长，巴蜀常被称为中国茶业和茶文化的摇篮。六朝以前的茶史资料表明，中国的茶业最初兴起于巴蜀。茶叶文化的形成，与巴蜀地区早期的政治、风俗及茶叶饮用有着密切的关系。茶树原产于中国，自古以来，一向为世界所公认。只是在1824年之后，印度发现有野生茶树，国外学者中有人对中国是茶树原产地提出异议，在国际学术界引发了争论。这些持异议者，均以印度野生茶树为依据，同时认为中国没有野生茶树。其实中国在公元200年左右，《尔雅》中就提到有野生大茶树，且现今的资料表明，全国有10个省区198处发现野生大茶树，其中云南的一株，树龄已达1700年左右，仅是云南省内树干直径在一米以上的就有10多株。有的地区，甚至野生茶树群落大至数千亩。所以自古至今，我国已发现的野生大茶树，时间之早，树体之大，数量之多，分布之广，性状之异，堪称世界之最。此外，又经考证，印度发现的野生茶树与从中国引入印度的茶树同属中国茶树之变种。由此，中国是茶树的原产地遂成定论。近几十年来，茶学和植物学研究相结合，从树种及地质变迁气候变化等不同角度出发，对茶树原产地作了更加细致深入的分析和论证，进一步证明我国西南地区是茶树原产地。主要论据， ");

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View source) {
				finish();
			}
		});
	}
}
