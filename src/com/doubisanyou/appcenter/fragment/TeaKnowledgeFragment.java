package com.doubisanyou.appcenter.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.activity.SearchSecondActivity;

public class TeaKnowledgeFragment extends Fragment {
	Context c;

	@Override
	public View onCreateView(LayoutInflater inflater,
			final ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View chatView = inflater.inflate(R.layout.tea_search_fragment,
				container, false);
		c = container.getContext();
		GridView gridview = (GridView) chatView
				.findViewById(R.id.tea_knowledge_gridview);

		// 生成动态数组，并且转入数据
		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		// for (int i = 0; i < 6; i++) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("tea_knowledge_item_image", R.drawable.red_tea);// 添加图像资源的ID
		map.put("tea_knowledge_item_text", "红茶");// 按序号做ItemText
		lstImageItem.add(map);
		map = new HashMap<String, Object>();
		map.put("tea_knowledge_item_image", R.drawable.green_tea);// 添加图像资源的ID
		map.put("tea_knowledge_item_text", "绿茶");// 按序号做ItemText
		lstImageItem.add(map);
		map = new HashMap<String, Object>();
		map.put("tea_knowledge_item_image", R.drawable.balck_tea);// 添加图像资源的ID
		map.put("tea_knowledge_item_text", "黑茶");// 按序号做ItemText
		lstImageItem.add(map);
		map = new HashMap<String, Object>();
		map.put("tea_knowledge_item_image", R.drawable.white_tea);// 添加图像资源的ID
		map.put("tea_knowledge_item_text", "白茶");// 按序号做ItemText
		lstImageItem.add(map);
		map = new HashMap<String, Object>();
		map.put("tea_knowledge_item_image", R.drawable.yello_tea);// 添加图像资源的ID
		map.put("tea_knowledge_item_text", "黄茶");// 按序号做ItemText
		lstImageItem.add(map);
		map = new HashMap<String, Object>();
		map.put("tea_knowledge_item_image", R.drawable.cyan_tea);// 添加图像资源的ID
		map.put("tea_knowledge_item_text", "青茶");// 按序号做ItemText
		lstImageItem.add(map);
		map = new HashMap<String, Object>();
		map.put("tea_knowledge_item_image", R.drawable.health_care_tea);// 添加图像资源的ID
		map.put("tea_knowledge_item_text", "保健茶");// 按序号做ItemText
		lstImageItem.add(map);
		map = new HashMap<String, Object>();
		map.put("tea_knowledge_item_image", R.drawable.flower_tea);// 添加图像资源的ID
		map.put("tea_knowledge_item_text", "花茶");// 按序号做ItemText
		lstImageItem.add(map);
		// }
		SimpleAdapter saImageItems = new SimpleAdapter(container.getContext(),
				lstImageItem, R.layout.griditem_tea_knowledge,
				new String[] { "tea_knowledge_item_image",
						"tea_knowledge_item_text" }, new int[] {
						R.id.tea_knowledge_item_image,
						R.id.tea_knowledge_item_text });
		// 添加并且显示
		gridview.setAdapter(saImageItems);
		// 添加消息处理
		gridview.setOnItemClickListener(new ItemClickListener());

		return chatView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the
													// click happened
				View arg1,// The view within the AdapterView that was clicked
				int arg2,// The position of the view in the adapter
				long arg3// The row id of the item that was clicked
		) {
			HashMap<String, Object> item = (HashMap<String, Object>) arg0
					.getItemAtPosition(arg2);
			Intent intent = new Intent(c, SearchSecondActivity.class);
			switch (arg2) {
			case 0:
				intent.putExtra(SearchSecondActivity.TEATYPE, SearchSecondActivity.REDTEA);
				intent.putExtra(SearchSecondActivity.TEAINFO, "红茶，英文为Black tea。红茶在加工过程中发生了以茶多酚酶促氧化为中心的化学反应，鲜叶中的化学成分变化较大，茶多酚减少90%以上，产生了茶黄素、茶红素等新成分。香气物质比鲜叶明显增加。所以红茶具有红茶、红汤、红叶和香甜味醇的特征。");
				break;
			case 1:
				intent.putExtra(SearchSecondActivity.TEATYPE, SearchSecondActivity.GREENTEA);
				intent.putExtra(SearchSecondActivity.TEAINFO, "绿茶是中国的主要茶类之一，是指采取茶树新叶或芽，未经发酵，经杀青、或者整形、烘干等典型工艺制作而成的产品。其制成品的色泽，冲泡后茶汤较多的保存了鲜茶叶的绿色主调。常饮绿茶能防癌，降血脂和减肥。吸烟者可减轻尼古丁伤害。");
				break;
			case 2:
				intent.putExtra(SearchSecondActivity.TEATYPE, SearchSecondActivity.BLACKTEA);
				intent.putExtra(SearchSecondActivity.TEAINFO, "黑茶因成品茶的外观呈黑色，故得名。六大茶类之一，属后发酵茶，主产区为四川、云南、湖北、湖南、陕西、安徽等地。黑茶采用的原料较粗老，是压制紧压茶的主要原料。制茶工艺一般包括杀青、揉捻、渥堆和干燥四道工序。黑茶按地域分布，主要分类为湖南黑茶（茯茶）、四川藏茶（边茶）、云南黑茶（普洱茶）、广西六堡茶、湖北老黑茶及陕西黑茶（茯茶）、安徽古黟黑茶。");
				break;
			case 3:
				intent.putExtra(SearchSecondActivity.TEATYPE, SearchSecondActivity.WHITETEA);
				intent.putExtra(SearchSecondActivity.TEAINFO, "白茶，属微发酵茶，是汉族茶农创制的传统名茶。中国六大茶类之一。指一种采摘后，不经杀青或揉捻，只经过晒或文火干燥后加工的茶。具有外形芽毫完整，满身披毫，毫香清鲜，汤色黄绿清澈，滋味清淡回甘的的品质特点。 属轻微发酵茶，是中国茶类中的特殊珍品。因其成品茶多为芽头，满披白毫，如银似雪而得名。主要产区在福建福鼎、政和、松溪、建阳、云南景谷等地。");
				break;
			case 4:
				intent.putExtra(SearchSecondActivity.TEATYPE, SearchSecondActivity.YELLOWTEA);
				intent.putExtra(SearchSecondActivity.TEAINFO, "黄茶是中国特产。其按鲜叶老嫩芽叶大小又分为黄芽茶、黄小茶和黄大茶。黄芽茶主要有君山银针、蒙顶黄芽和霍山黄芽；如沩山毛尖、泉城红、泉城绿、平阳黄汤等均属黄小茶；而安徽皖西金寨、霍山、湖北英山和广东大叶青则为黄大茶。黄茶的品质特点是“黄叶黄汤”。湖南岳阳为中国黄茶之乡。");
				break;
			case 5:
				intent.putExtra(SearchSecondActivity.TEATYPE, SearchSecondActivity.CYANTEA);
				intent.putExtra(SearchSecondActivity.TEAINFO, "乌龙茶亦称青茶、半发酵茶及全发酵茶，品种较多，是中国几大茶类中，独具鲜明汉族特色的茶叶品类。乌龙茶是经过采摘、萎凋、摇青、炒青、揉捻、烘焙等工序后制出的品质优异的茶类。乌龙茶由宋代贡茶龙团、凤饼演变而来，创制于1725年（清雍正年间）前后。品尝后齿颊留香，回味甘鲜。乌龙茶的药理作用，突出表现在分解脂肪、减肥健美等方面。");
				break;
			case 6:
				intent.putExtra(SearchSecondActivity.TEATYPE, SearchSecondActivity.HEALTHTEA);
				intent.putExtra(SearchSecondActivity.TEAINFO, "保健茶首先在西方流行。中国保健茶是以绿茶、红茶或乌龙茶、花草茶为主要原料，配以确有疗效的单味或复方中药制成；也有用中药煎汁喷在茶叶上干燥而成；或者药液茶液浓缩干燥而成。外形颗粒状，易于沸水速溶。中国保健茶与外国药茶不同，后者是以草药为原料，不含茶叶，只借用“茶”这个名称。");
				break;
			case 7:
				intent.putExtra(SearchSecondActivity.TEATYPE, SearchSecondActivity.FLOWERTEA);
				intent.putExtra(SearchSecondActivity.TEAINFO, "花茶（Scented tea），又名香片， 即将植物的花或叶或其果实泡制而成的茶，是中国特有的一类再加工茶。其是利用茶善于吸收异味的特点，将有香味的鲜花和新茶一起闷，茶将香味吸收后再把干花筛除，制成的花茶香味浓郁，茶汤色深。花茶又可细分为花草茶和花果茶。饮用叶或花的称之为花草茶，如荷叶、甜菊叶。");
				break;
			default:
				break;
			}

			startActivity(intent);

		}
	}
}
