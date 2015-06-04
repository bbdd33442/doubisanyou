package com.doubisanyou.appcenter.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.adapter.SimpleAdapter;
import com.doubisanyou.appcenter.bean.TeaKnowledge;
import com.doubisanyou.appcenter.widget.LoadingDialog;
import com.doubisanyou.appcenter.widget.PullToRefreshBase.OnRefreshListener;
import com.doubisanyou.appcenter.widget.PullToRefreshListView;

/* carLoadingDialog.show(); */
// TODO Auto-generated method stub
// map.put("", xxx.cccc);
// 重写选项被选中事件的处理方法
// 重写选项被单击事件的处理方法
public class SearchSecondActivity extends Activity {

	public static final String TEATYPE = "teatype";
	public static final String TEAINFO = "teainfo";
	public static final int REDTEA = 1;
	public static final int GREENTEA = 2;
	public static final int BLACKTEA = 3;
	public static final int WHITETEA = 4;
	public static final int YELLOWTEA = 5;
	public static final int CYANTEA = 6;
	public static final int HEALTHTEA = 7;
	public static final int FLOWERTEA = 8;

	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> replyItem;
	private PullToRefreshListView mPullRefreshListView;
	SimpleAdapter sla;
	private ListView mListView;
	LoadingDialog carLoadingDialog;
	List<TeaKnowledge> teaKnowledges = new ArrayList<TeaKnowledge>();;
	private int mLoadingTpye = LOAGDING_LOADING_MORE;
	private final static int LOAGDING_NORMAL = 0;
	private final static int LOAGDING_REFRESH = 1;
	private final static int LOAGDING_LOADING_MORE = 2;

	private int pagesize = 10;
	private int pageNumber = 1;

	private TextView title;
	private TextView info;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		carLoadingDialog = new LoadingDialog(this);
		setContentView(R.layout.tea_search_second);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.task_list);
		mPullRefreshListView.setOnRefreshListener(mOnrefreshListener);
		mListView = mPullRefreshListView.getRefreshableView();
		/* carLoadingDialog.show(); */
		mPullRefreshListView.setUpRefreshEnabled(true);

		title = (TextView) this.findViewById(R.id.default_title);

		title.setText("茶知识");

		Button back = (Button) findViewById(R.id.btn_left);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		ListView lv = mPullRefreshListView.getRefreshableView();
		int d = getIntent().getIntExtra(TEATYPE, 0);
		// 进行网络通讯从服务器获取的
		switch (getIntent().getIntExtra(TEATYPE, 0)) {
		case REDTEA:
			TeaKnowledge tk = new TeaKnowledge();
			tk.tea_knowledge_name = "祁门红茶";
			tk.tea_knowledge_introduce = "  祁门红茶简称祁红，茶叶原料选用当地的中叶、中生种茶树“槠叶种”（又名祁门种）制作，是一种汉族传统名茶，中国历史名茶，著名红茶精品。由安徽省汉族茶农创制于光绪年间，但史籍记载最早可追溯至唐朝陆羽的茶经。产于安徽省祁门、东至、贵池（今池州市）、石台、黟县，以及江西的浮梁一带。“祁红特绝群芳最，清誉高香不二门。”祁门红茶是红茶中的极品，享有盛誉，是英国女王和王室的至爱饮品，高香美誉，香名远播，美称“群芳最”、“红茶皇后”。相关经典名句：“一器成名只为茗，悦来客满是茶香。”";
			tk.tea_knowledge_simple_name = "祁红";
			tk.tea_knowledge_area = "祁门、池州";
			tk.tea_knowledge_type = "红茶";
			tk.tea_knowledge_brew = "温杯:用开水温茶具，使茶具均匀受热；投茶:取5g左右茶叶放入茶壶中；泡茶:倒掉洗茶的水，倒入8分满开水，盖上壶盖泡2-3min；出汤：将泡好的茶汤倒入公道杯中，然后将茶汤分到小直口杯中。闻茶香，品茶味，";
			tk.tea_knowledge_quality = "1、最简单的方法是看祁门红茶茶叶的外形。品质好的茶叶其外形看起来条索纤细紧结，茶叶之间看起来均匀整齐;质量茶的茶叶则条索较粗，松散，茶叶大小不一，不整齐。2、从茶叶的颜色上看。好的祁门红茶茶叶色泽乌润鲜亮有光泽，而品质差的茶叶颜色不一，相对看起来光泽比较的暗淡。3、从茶叶的香气上看。好的茶叶的香气浓郁持久，品质茶的茶叶香气不纯正，不持久。4、从冲泡后的茶汤汤色上看。冲泡后茶汤红艳，而且有金圈，说明这种祁门红茶茶叶的品质是好的;如果冲泡出来的茶汤颜色较暗，而且汤色有浑浊的样子则说明其是不好的茶叶。";
			tk.tea_knowledge_pic = String.valueOf(R.drawable.red_tea);
			teaKnowledges.add(tk);
			info = (TextView) this.findViewById(R.id.bn);
			info.setText(getIntent().getStringExtra(
					SearchSecondActivity.TEAINFO));
			break;
		case GREENTEA:
			TeaKnowledge tk1 = new TeaKnowledge();
			tk1.tea_knowledge_name = "信阳毛尖";
			tk1.tea_knowledge_pic = String.valueOf(R.drawable.green_tea);
			tk1.tea_knowledge_introduce = "  信阳毛尖又称豫毛峰，汉族传统名茶，属绿茶类。中国十大名茶之一，河南省著名特产。由汉族茶农创制于民国初年。主要产地在信阳市和新县，商城县及境内大别山一带。信阳毛尖具有“细、圆、光、直、多白毫、香高、味浓、汤色绿”的独特风格，具有生津解渴、清心明目、提神醒脑、去腻消食等多种营养价值。";
			tk1.tea_knowledge_simple_name = "信阳毛尖";
			tk1.tea_knowledge_area = "信阳";
			tk1.tea_knowledge_type = "绿茶";
			tk1.tea_knowledge_brew = "使用纯净水和玻璃器皿，依据个人口味酌量投放毛尖。品饮时先用水冲洗茶具，然后再投放毛尖；洗茶用85℃的水冲泡摇晃茶杯并随即倒掉清洗水，再加水冲泡。过半分钟后再品饮，茶汤饮至1/3添水续饮。继续添水续饮至茶汤变淡。一般可品饮3至5次。";
			tk1.tea_knowledge_quality = "特级：一芽一叶初展外形紧细圆匀称，细嫩多毫，色泽嫩绿油润，叶底嫩匀，芽叶成朵，叶底柔软，叶底嫩绿，香气高爽，鲜嫩持久，滋味鲜爽，汤色鲜明。 "
					+ " 一级：一芽一叶或一芽二叶初展占85%以上，外形条索紧秀、圆、直、匀称多白毫，色泽翠绿，叶底匀称，芽叶成朵，叶色嫩绿而明亮，香气鲜浓，板栗香，纯浓，甘甜，汤色明亮。"
					+ "二级：一芽一二叶不少于65%条索紧结，圆直欠匀，白毫显露，色泽翠绿，稍有嫩茎，叶底嫩，芽叶成朵，叶底柔软，叶色绿亮，香气鲜嫩，有板栗香，滋味浓强，甘甜，汤色绿亮。"
					+ "三级：一芽二三叶，不少于65%，条索紧实光圆直芽头显露，色泽翠绿，有少量粗条，叶底嫩欠匀，稍有嫩单张和对夹叶，叶底较柔软，色嫩绿较明亮，香气清香，滋味醇厚，汤色明净。"
					+ "四级：外形条索较粗实，圆，有少量朴青，色泽青黄，叶底嫩欠匀，香气纯正，醇和，汤色泛黄清亮。"
					+ "五级：条索粗松，有少量朴片，色泽黄绿，叶底粗老，有弹性，香气纯正，滋味平和，汤色黄尚亮。没有产地要求。";

			teaKnowledges.add(tk1);
			info = (TextView) this.findViewById(R.id.bn);
			info.setText(getIntent().getStringExtra(
					SearchSecondActivity.TEAINFO));
			break;
		case BLACKTEA:
			TeaKnowledge tk2 = new TeaKnowledge();
			tk2.tea_knowledge_name = "云南普洱";
			tk2.tea_knowledge_pic = String.valueOf(R.drawable.balck_tea);
			tk2.tea_knowledge_introduce = "普洱茶，又名滇青茶，属于黑茶类，因原运销集散地在普洱县，故名普洱茶。普洱茶以云南大叶种晒青茶为原料，使用亚发酵青茶制法，从发酵不同分为生茶和熟茶两种，成品分为散茶和紧压茶两类。"
					+ "普洱茶是中国名茶中最讲究冲泡技巧和品饮艺术的茶类，其饮用方法异常丰富，既可清饮，也可混饮。普洱茶茶汤橙黄浓厚，香气高锐持久，香型独特，滋味浓醇，经久耐泡，冲泡五六次后仍有香味。";
			tk2.tea_knowledge_simple_name = "普洱茶";
			tk2.tea_knowledge_area = "雲南";
			tk2.tea_knowledge_type = "黑茶";
			tk2.tea_knowledge_brew = "先冲过一次热水对于普洱茶来说这是不可缺少的程序。因为好的陈年普洱茶至少要储存十年左右，所以可能会带有部分的灰尘在里面。第一次冲泡茶叶的热水除了可以唤醒茶叶的味道之外，还具有将茶叶中的杂质一并洗净。第一次的冲泡速度要快，只要能将茶叶洗净即可，不须将它的味道浸泡出来；而第二次以后浓淡的选择就可依照个人喜好来决定。普洱茶即使变冷以后还是风味十足，所以夏天的时候可以弄得冷一些或者是冰过以后再喝。";
			tk2.tea_knowledge_quality = "1、观形。好的普洱应该是条索肥嫩紧结，叶形完整。如果是碎、细的茶沫或者杂质很多，一般是次品。"
					+ "2、闻味道。一块茶，不管是新旧，是不应该有其他杂味的。熟茶经过渥堆，会有一种熟茶味。新的生茶，有清香和甜味。而陈茶应有种特殊的陈味，好的陈年普洱会散发出一种自然的樟香，荷香等香味，而不是“臭卜味”。"
					+ "3、试泡。开汤后看冲泡后的叶底（茶渣），主要看柔软度、色泽、匀度。叶质柔软、肥嫩、有弹性的好，叶底硬、无弹性的品质不好；色泽褐红、均匀一致的好，色泽花杂不匀，或发黑、碳化、或腐烂如泥，叶张不开展属品质不好。"
					+ "4、试喝。好的普洱茶应该是入口柔顺滑润、滋味醇正、清爽平和，刺激性不强，可以毫不阻滞地从口腔流向喉咙和胃部。若出现麻口、苦涩异常的，要结合产地存疑，勐海区的会偏苦一些，思茅的会偏涩。";

			teaKnowledges.add(tk2);
			info = (TextView) this.findViewById(R.id.bn);
			info.setText(getIntent().getStringExtra(
					SearchSecondActivity.TEAINFO));
			break;
		case WHITETEA:
			TeaKnowledge tk3 = new TeaKnowledge();
			tk3.tea_knowledge_name = "白毫银针";
			tk3.tea_knowledge_pic = String.valueOf(R.drawable.white_tea);
			tk3.tea_knowledge_introduce = "  白毫银针，简称银针，又叫白毫，是一种汉族传统名茶，属白茶类。素有茶中“美女”、“茶王”之美称。白毫银针由福建省的汉族茶农创制于1889年，产地位于中国福建省的福鼎市和南平市政和县。"
					+ "由于鲜叶原料全部是茶芽，白毫银针制成成品茶后，形状似针，白毫密被，色白如银，因此命名为白毫银针。其针状成品茶，长三厘米许，整个茶芽为白毫覆被，银装素裹，熠熠闪光，令人赏心悦目。冲泡后，香气清鲜，滋味醇和，杯中的景观也使人情趣横生。茶在杯中冲泡，即出现白云疑光闪，满盏浮花乳，芽芽挺立，蔚为奇观。";
			tk3.tea_knowledge_simple_name = "白毫银针";
			tk3.tea_knowledge_area = "福建";
			tk3.tea_knowledge_type = "白茶";
			tk3.tea_knowledge_brew = "1、用200ml的大杯，取5g白茶用90度开水先温润茶叶，先闻香再用开水直接冲泡，一分钟以后可饮用(白茶加工时未经揉捻，茶汁不容易浸出，冲泡时间一般来说相对较长。"
					+ "2、随水温茶量不同而不同，想要茶汤浓厚则时间就要相对延长，尤其是冲泡陈年白茶)此法泡白茶，白茶毫香溶于水中，茶汤非常爽滑甘甜同时也便于观赏白茶升降沉浮，边观赏边品饮，体会冲饮白茶意趣盎然的意境。";
			tk3.tea_knowledge_quality = "白毫银针是白茶 中的珍品。因其成茶芽头肥壮、肩披白毫、挺直如针、色白如银而得名。主产地有福鼎和政和，尤以福鼎生产的白毫银针品质为高。白毫银针外形芽壮肥硕显毫，色泽银灰，熠熠有光。汤色杏黄，滋味醇厚回甘，冲泡后，茶芽徐徐下落，慢慢沉至杯底，条条挺立。白毫银针性寒，有退热、降火解毒之功效。"
					+ "白毫银针的成品茶，芽头肥壮，满披白毫，挺直如针。福鼎白毫，茶芽茸毛厚，白色富光泽，汤色浅杏黄，味清鲜爽口。政和白毫，汤味醇厚，香气清芬。"
					+ "白毫银针的形、色、质、趣是名茶中绝无仅有的，实为茶中珍品，品尝泡饮，别有风味。品选银针，寸许芽心，银光闪烁；冲泡杯中，条条挺立，如陈枪列戟；微吹饮辍，升降浮游，观赏品饮，别有情趣。"
					+ "冲泡过后的白毫银针就像细长的针一样直直的立于水中，由于它的芽头满是白色茸毛，配上绿色的芽叶 确实让人赏心悦目。";

			teaKnowledges.add(tk3);
			info = (TextView) this.findViewById(R.id.bn);
			info.setText(getIntent().getStringExtra(
					SearchSecondActivity.TEAINFO));
			break;
		case YELLOWTEA:
			TeaKnowledge tk4 = new TeaKnowledge();
			tk4.tea_knowledge_name = "君山银针";
			tk4.tea_knowledge_pic = String.valueOf(R.drawable.yello_tea);
			tk4.tea_knowledge_introduce = "  君山银针是汉族传统名茶，中国名茶之一。产于湖南岳阳洞庭湖中的君山，形细如针，故名君山银针。属于黄茶。其成品茶芽头茁壮，长短大小均匀，茶芽内面呈金黄色，外层白毫显露完整，而且包裹坚实，茶芽外形很象一根根银针，雅称“金镶玉”。“金镶玉色尘心去，川迥洞庭好月来。”君山茶历史悠久，唐代就已生产、出名。据说文成公主出嫁时就选带了君山银针茶带入西藏。";
			tk4.tea_knowledge_simple_name = "君山银针";
			tk4.tea_knowledge_area = "岳阳";
			tk4.tea_knowledge_type = "黄茶";
			tk4.tea_knowledge_brew = "冲泡君山银针，用水以清澈的山泉为佳，茶具宜用透明的玻璃杯，杯子高度10～15厘米，杯口直径4～6厘米。每杯用茶量为3克，太多太少都不利于欣赏茶的姿形景观。冲泡程序如下：（1）赏茶用茶匙摄取少量君山银针，置于洁净赏茶盘中，供宾客观赏。" +
					"（2）洁具用开水预热茶杯，清洁茶具，并擦干杯中水珠，以避免茶芽吸水而降低茶芽的竖立率。" +
					"（3）置茶用茶匙轻轻地从茶叶罐中取出君山银针约3克，放入茶杯待泡。" +
					"（4）高冲用水壶将70℃左右的开水，利用水的冲力，先快后慢冲入茶杯，至1/2处，使茶芽湿透。稍后，再冲至七八分杯满为止。为使茶芽均匀吸水，加速下沉，这时可用玻璃片盖在茶杯上，经5分钟后，去掉玻璃盖片。在水和热的作用下，茶姿的形态，茶芽的沉浮，气泡的发生等，都是其他茶泡时罕见的，这是君山银针茶的特有氛围。" +
					"（5）奉茶大约冲泡10分钟后，就可开始品饮。这时双手端杯，有礼貌地奉给宾客。";
			tk4.tea_knowledge_quality = "君山银针，产于湖南岳阳君山。由未展开的肥嫩芽头制成，芽头肥壮挺直、匀齐，满披茸毛，色泽金黄光亮，香气清鲜，茶色浅黄，味甜爽，冲泡看起来芽尖冲向水面，悬空竖立，然后徐徐下沉杯底，形如群笋出土，又像银刀直立。假银针为清草味，泡后银针不能竖立。" +
					"成品茶按芽头肥瘦、曲直，色泽亮暗进行分级。以壮实挺直亮黄为上。优质茶芽头肥壮，紧实挺直，芽身金黄，满披银毫；汤色橙黄明净，香气清纯，叶底嫩黄匀亮。实为黄茶之珍品。";

			teaKnowledges.add(tk4);
			info = (TextView) this.findViewById(R.id.bn);
			info.setText(getIntent().getStringExtra(
					SearchSecondActivity.TEAINFO));
			break;
		case CYANTEA:
			TeaKnowledge tk5 = new TeaKnowledge();
			tk5.tea_knowledge_name = "铁观音";
			tk5.tea_knowledge_pic = String.valueOf(R.drawable.cyan_tea);
			tk5.tea_knowledge_introduce = "  铁观音茶，汉族传统名茶，属于青茶类，是中国十大名茶之一。原产于福建泉州市安溪县西坪镇，发现于1723—1735年。“铁观音”既是茶名，也是茶树品种名，铁观音茶介于绿茶和红茶之间，属于半发酵茶类，铁观音独具“观音韵”，清香雅韵，冲泡后有天然的兰花香，滋味纯浓,香气馥郁持久，有“七泡有余香之誉 ”。除具有一般茶叶的保健功能外，还具有抗衰老、抗动脉硬化、防治糖尿病、减肥健美、防治龋齿、清热降火，敌烟醒酒等功效。";
			tk5.tea_knowledge_simple_name = "铁观音";
			tk5.tea_knowledge_area = "闽南、闽北、广东、台湾";
			tk5.tea_knowledge_type = "青茶";
			tk5.tea_knowledge_brew = "要从水、茶具、冲泡时间入手。水要用山泉水为佳，好的水质可以更好的发挥出茶的内质。用100℃开水冲泡最佳。" +
					"1、清香系列产品：原料均来自铁观音发源地安溪高海拔、岩石基质土壤种植的茶树，具有“鲜、香、韵、锐”之综合特征。冲泡方法：每次5-10克放进茶杯，用沸水冲泡，首汤10-20秒即可倒出茶水，以后依次延长，但不可久浸，可连续冲泡6-7次。矿泉水或纯净水冲泡，山泉水泡饮效果最佳。" +
					"2、浓香系列产品：以传统工艺“茶为君，火为臣”制作的铁观音茶叶，使用百年独特的烘焙方法，温火慢烘，湿风快速冷却。浓香型铁观音则要用紫砂壶泡，并且要用大嘴的。如果用小嘴的紫砂壶不利于茶叶的散热，茶叶在壶里很快就会“熟化”，茶叶的滋味会体现出“涩”的一面。" +
					"3、韵香系列产品特色：“观音韵”是安溪铁观音特有的韵味，是正宗铁观音的品质和特性的象征，色、香、味俱全。由于产地的原因：内安溪铁观音韵味纯正、外安溪铁观音韵味次之 、华安铁观音韵味则微。" +
					"4、炭焙浓香型系列产品：特色制作方法是在传统正味做法的基础上再经过120℃左右烘焙10小时左右，提高滋味醇度，发展香气。原料均来自铁观音发源地安溪高海拔、岩石基质土壤种植的茶树，经过精挑细选、传统工艺精制拼配而成。茶叶发酵充足，传统正味，具有“浓、韵、润、特”之口味，香味高，回甘好，韵味足，长期以来倍受广大消费者的青睐。韵香型铁观音要用盖碗泡，因为盖碗是白瓷制作，不吸味，导热也快。";
			tk5.tea_knowledge_quality = "1、观看外形：主要是观察铁观音的外形、色泽、匀净度和闻茶米的香气。凡外形肥状、重实、色泽砂绿，干茶（茶米）香气清纯的，此类茶即观音特征明显均为上品茶;反之为次品茶。2、湿评品质：茶叶经沸水冲泡后鉴别其香气、汤色、滋味和叶底。";

			teaKnowledges.add(tk5);
			info = (TextView) this.findViewById(R.id.bn);
			info.setText(getIntent().getStringExtra(
					SearchSecondActivity.TEAINFO));
			break;
		case HEALTHTEA:
			TeaKnowledge tk6 = new TeaKnowledge();
			tk6.tea_knowledge_name = "决明子茶";
			tk6.tea_knowledge_pic = String.valueOf(R.drawable.health_care_tea);
			tk6.tea_knowledge_introduce = "  决明子茶是豆科草本植物的成熟种子，味苦、甘而性凉，清肝明目、润肠通便。现代药理研究认为，决明子富含大黄酚、大黄素、决明素等成分，具有降压、抗菌和降低胆固醇的作用。将单味炒决明子15克，直接泡茶饮用，直至茶水无色。";
			tk6.tea_knowledge_simple_name = "决明子茶";
			tk6.tea_knowledge_area = "中国";
			tk6.tea_knowledge_type = "保健茶";
			tk6.tea_knowledge_brew = "决明子茶的泡法十分简单，只要用15～20克决明子用热开水冲泡即可，也可依个人喜好放入适量的糖，当茶饮用，每日数次，若能配上枸杞子及菊花，效果更佳。不过，决明子性微寒，容易拉肚子、腹泻、胃痛的人，不宜饮用此茶。";
			tk6.tea_knowledge_quality = "决明子有二种，一种马蹄决明，茎高三、四尺，叶大于苜蓿而本小末茤，昼开夜合，两两相帖。秋开淡黄花，五出。结角如初生细豇豆，长五、六寸，角中子数十粒，参差相连，状如马蹄，青绿色，入眼目药最良。一种茳芒决明。二种皆可作酒曲，俗呼为独占缸。但茳芒嫩苗及花与角子皆可瀹茹及点茶食，而马蹄决明苗角皆韧苦，不可食用。（所以选取决明子要取决你的用途）决明子一般呈四方形或短圆柱形，两端近平行，稍倾斜，长3～7毫米，宽2～4毫米，绿棕色或暗棕色平滑有光泽，背腹面各有1条突起的棱线，棱线两侧各有1条淡黄色的线形凹纹；质坚硬,不易破碎。横切面可见薄的种皮和2片3形折曲的黄色子叶。气微,味微苦。以颗粒饱满、色绿棕者为佳。。";

			teaKnowledges.add(tk6);
			info = (TextView) this.findViewById(R.id.bn);
			info.setText(getIntent().getStringExtra(
					SearchSecondActivity.TEAINFO));
			break;
		case FLOWERTEA:
			TeaKnowledge tk7 = new TeaKnowledge();
			tk7.tea_knowledge_name = "茉莉花茶";
			tk7.tea_knowledge_pic = String.valueOf(R.drawable.flower_tea);
			tk7.tea_knowledge_introduce = "  信阳毛尖又称豫毛峰，汉族传统名茶，属绿茶类。中国十大名茶之一，河南省著名特产。由汉族茶农创制于民国初年。主要产地在信阳市和新县，商城县及境内大别山一带。信阳毛尖具有“细、圆、光、直、多白毫、香高、味浓、汤色绿”的独特风格，具有生津解渴、清心明目、提神醒脑、去腻消食等多种营养价值。";
			tk7.tea_knowledge_simple_name = "茉莉花茶";
			tk7.tea_knowledge_area = "广西 、福建等地";
			tk7.tea_knowledge_type = "花茶";
			tk7.tea_knowledge_brew = "一、冲泡品饮高档花茶，通常采用透明的玻璃杯冲泡，用 90~C左右的沸水冲泡，冲泡时间约3-5min，冲泡次数以2-3次为宜。冲泡时可通过玻璃杯欣赏茶叶精美别致的造型。盖碗冲泡法是四川人品饮花茶常用的方法，一套茶具有茶碗、茶托、茶盖，每人一套盖碗泡茶，边饮边品，摆摆“龙门阵”，悠悠自得，其乐无穷。" +
					"二、闻香茉莉花茶经冲泡静置片刻后，即可提起茶盏，揭开杯盖一侧，用鼻闻香，顿觉芬芳扑鼻而来。有兴趣者，还可凑着香气作深呼吸状，以充分领略香气对人的愉悦之感，人称“鼻品”。" +
					"三、品饮经闻香后，待茶汤稍凉适口时，小口喝入，并将茶汤在口中稍时停留，以口吸气、鼻呼气相配合的动作，使茶汤在舌面上往返流动12次，充分与味蕾接触，品尝茶叶和香气后再咽下，这叫“口品”。所以民间对饮茉莉花茶有“一口为喝，三口为品”之说。" +
					"四、欣赏特种工艺造型茉莉花茶和高级茉莉花茶泡在玻璃杯中，在品其香气和滋味的同时可欣赏其在杯中优美的舞姿。";
			tk7.tea_knowledge_quality = "茉莉花茶是供人们喝的天然饮料，应以内质为主，兼看外观（外形）来鉴别的。" +
					"1、外观：以条索、色泽、匀整度来评定。" +
					"① 条索（名优茶叫造型）：要求紧结壮实。名茶有它自己独特要求，如龙井茶要求扁平匀直；碧螺春要求卷曲细嫩等等。但一般而言，春茶一般条索紧结重实，夏秋茶就较轻瘦或粗松。高档茶要求芽毫多且肥壮。" +
					"② 色泽：花茶是以绿茶为原料，一般色泽要求嫩绿、黄绿、有光泽为好，夏秋茶比较枯绿欠油润，秋茶还好些，陈茶灰暗。" +
					"③ 匀整度：干净匀整为好，特别要检查非茶类夹杂物，如昆虫、铁钉等不能有掺杂。" +
					"2、内质:内质是茉莉花茶品质主要因素，以香气、滋味、汤色、叶底来鉴别，主要决定于香气和滋味。" +
					"①香气：从鲜度、浓度、纯度三因素来评判，优质的花茶同时具有鲜、浓、纯的香气，三者既有区别又有相关性。" +
					"a、鲜度：指香气的鲜灵程度，审评一杯花茶时，闻香气，给人第一印象鲜度如何？通俗说就是香气新鲜否？高档花茶要求“鲜灵”，鲜灵是香气表现十分敏锐，即“一嗅即感”是鲜的更高表现，不鲜，陈味都是低档花茶或陈茶的表现特征。" +
					"b、浓度：指茉莉花茶耐泡度。香气持久、耐泡者为浓度好的茉莉花茶，相反香气薄、不持久、一泡有香，二泡就闻不到香气，浓度就差了。一般低级别花茶浓度总比不上高级别的花茶。" +
					"c、纯度：指花香、茶香的纯正度。如茉莉花茶中不能“透兰”或其它花的香，茶香中不能有烟焦味及其它异味。" +
					"②滋味：主要评浓度和鲜纯度。品尝花茶滋味纯正浓醇为好。滋味与香气在正常情况下，一般是相关性。香气鲜滋味爽；香气浓滋味醇；香气纯滋味细；若发现香气有异，在滋味上要认真地加以鉴别。" +
					"③汤色：以黄绿、清澈明亮为好，黄暗或泛红为劣。" +
					"④叶底：嫩绿、黄绿、匀亮为好，粗展、欠匀、色暗或红张为劣。" +
					"顾客在选购茉莉花茶时，如上所说，首先看外形要符合我们的要求（条索、色泽、匀整度）；此外也可以干闻香气，鲜纯度一般能闻出来，有经验者也能闻出浓度来；然后最好冲泡一杯，品尝一下香气滋味，能喝到鲜爽浓醇花茶味时，再看茶汤是黄绿明亮的，那定是品质好的花茶，可放心购买。";

			teaKnowledges.add(tk7);
			info = (TextView) this.findViewById(R.id.bn);
			info.setText(getIntent().getStringExtra(
					SearchSecondActivity.TEAINFO));
			break;
		default:
			break;
		}

		// TeaKnowledge gt = new TeaKnowledge();
		// gt.tea_knowledge_name = "祁红";
		// teaKnowledges.add(gt);
		// teaKnowledges.add(tk);

		replyItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < teaKnowledges.size(); i++) {
			map = new HashMap<String, Object>();
			map.put("searchImage",  teaKnowledges.get(i).tea_knowledge_pic);
			map.put("searchContent", teaKnowledges.get(i).tea_knowledge_name);
			replyItem.add(map);
		}

		// map.put("", xxx.cccc);

		sla = new SimpleAdapter(getApplicationContext(), replyItem,
				R.layout.listitem_tea_search_second, new String[] {
						"searchImage", "searchContent" }, new int[] {
						R.id.tea_search_second_image,
						R.id.tea_search_second_content });

		lv.setAdapter(sla);

		lv.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {// 重写选项被选中事件的处理方法
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {// 重写选项被单击事件的处理方法
				TeaKnowledge c = teaKnowledges.get(arg2 - 1);
				Intent intent = new Intent(SearchSecondActivity.this,
						SearchThirdActivity.class);
				intent.putExtra(SearchThirdActivity.TEAKNOWLEDGE, c);
				startActivity(intent);
			}
		});
	}

	OnRefreshListener mOnrefreshListener = new OnRefreshListener() {
		public void onRefresh() {
			switch (mPullRefreshListView.getRefreshType()) {
			case LOAGDING_REFRESH:
				mLoadingTpye = LOAGDING_REFRESH;
				pageNumber = 1;
				mPullRefreshListView.setUpRefreshEnabled(true);
				mPullRefreshListView.onRefreshComplete();
				break;
			case LOAGDING_LOADING_MORE:
				pageNumber++;
				mLoadingTpye = LOAGDING_LOADING_MORE;
				mPullRefreshListView.setUpRefreshEnabled(true);
				mPullRefreshListView.onRefreshComplete();
				break;
			default:
				break;
			}

		}
	};

}