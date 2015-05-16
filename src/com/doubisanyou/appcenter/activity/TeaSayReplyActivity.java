package com.doubisanyou.appcenter.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.adapter.SimpleAdapter;
import com.doubisanyou.appcenter.bean.TeaSay;
import com.doubisanyou.baseproject.base.BaseActivity;

public class TeaSayReplyActivity extends BaseActivity implements OnClickListener{
	
	public static final String TEASAYINFO="teasayinfo";
	
	TeaSay ts;
	
	TextView replyTitle;
	Button back;
	ListView replyListView;
	Button teaSayCommentButton;
	SimpleAdapter sla;
	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> replyItem;
	EditText commentText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_tea_say_reply);
		Intent i = getIntent();
		ts = (TeaSay) i.getSerializableExtra(TEASAYINFO);
		iniView();
	}
	
	void iniView(){
		replyItem = new ArrayList<HashMap<String, Object>>();
		map = new HashMap<String, Object>();
		map.put("useravatars",R.drawable.user_default_avatars);
		map.put("replycontent", "12345上山打老虎,老虎不在家");
		replyItem.add(map);
		replyItem.add(map);
		replyItem.add(map);
		replyItem.add(map);
		replyItem.add(map);
		replyItem.add(map);
		replyItem.add(map);
		replyItem.add(map);
		replyItem.add(map);
		replyItem.add(map);
		replyItem.add(map);
		replyItem.add(map);
		sla = new SimpleAdapter(getApplicationContext(), replyItem,
				                R.layout.listitem_tea_say_repley,
				                new String[] { "useravatars", "replycontent"},
				                new int[]{R.id.tea_say_reply_user_avatars,R.id.tea_say_reply_content});
		replyTitle = (TextView) findViewById(R.id.default_title);
		replyTitle.setText("回复");
		back = (Button) findViewById(R.id.btn_left);
		back.setOnClickListener(this);
		back.setVisibility(View.VISIBLE);
		replyListView = (ListView) findViewById(R.id.tea_say_reply_listview);
		replyListView.setAdapter(sla);
		teaSayCommentButton = (Button) findViewById(R.id.tea_say_comment_btn);
		teaSayCommentButton.setOnClickListener(this);
		commentText = (EditText) findViewById(R.id.tea_say_comment_content);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_left:
			finish();
			break;
		case R.id.tea_say_comment_btn:
			map = new HashMap<String, Object>();
			map.put("useravatars",R.drawable.user_default_avatars);
			map.put("replycontent", commentText.getText());
			replyItem.add(map);
			sla.notifyDataSetChanged();
			replyListView.setSelection(replyListView.getBottom());
			commentText.setText("");
			break;
		default:
			break;
		}
		
	}
}
