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
		introduce.setText("  祁门红茶简称祁红，茶叶原料选用当地的中叶、中生种茶树“槠叶种”（又名祁门种）制作，是一种汉族传统名茶，中国历史名茶，著名红茶精品。由安徽省汉族茶农创制于光绪年间，但史籍记载最早可追溯至唐朝陆羽的茶经。产于安徽省祁门、东至、贵池（今池州市）、石台、黟县，以及江西的浮梁一带。“祁红特绝群芳最，清誉高香不二门。”祁门红茶是红茶中的极品，享有盛誉，是英国女王和王室的至爱饮品，高香美誉，香名远播，美称“群芳最”、“红茶皇后”。相关经典名句：“一器成名只为茗，悦来客满是茶香。”");
		
		brew = (TextView) this.findViewById(R.id.brewinfo);
		brew.setText("温杯:用开水温茶具，使茶具均匀受热；投茶:取5g左右茶叶放入茶壶中；泡茶:倒掉洗茶的水，倒入8分满开水，盖上壶盖泡2-3min；出汤：将泡好的茶汤倒入公道杯中，然后将茶汤分到小直口杯中。闻茶香，品茶味，");
		
		quality = (TextView) this.findViewById(R.id.qualityinfo);
		quality.setText("茶叶评审主要依靠评茶人员的视觉、嗅觉、味觉、触觉来判断茶叶品质好坏的检验方法。相对于茶叶理化检验，茶叶的感官评审主要内容是茶叶品质、等级、制作等质量问题，包括外形、汤色、香气、滋味、叶底五项，简称“五项因子”，由于相应定义了500多条专用评茶术语；商业系统对成品茶的检验有的将外形拆分成条索、整碎、净度、色泽四项，构成八个检验项目，简称“八项因子”，其主要程序为取样、干评、湿评、记分下评语，在名优茶的评比中，当茶样数较多，且要评出每个品质顺序时，还须采用评分方法。茶叶评审可以对茶叶贸易与茶叶定价提供标准，及对制茶工艺上存在哪些不当并为生产者提出改进意见。");
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View source) {
				finish();
			}
		});
	}
}
