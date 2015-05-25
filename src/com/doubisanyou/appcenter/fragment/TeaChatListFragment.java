package com.doubisanyou.appcenter.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.activity.TeaChatRoomActivity;
import com.doubisanyou.appcenter.bean.ChatListFormEntity;
import com.doubisanyou.appcenter.bean.EBEvents;

import de.greenrobot.event.EventBus;

public class TeaChatListFragment extends Fragment {
	private static final String TAG = TeaChatListFragment.class.getSimpleName();
	// private String[] nicknames = new String[] { "blook", "admin" };
	// private String[] shotMsgs = new String[] { "你好！", "haha" };
	private SimpleAdapter chatListAdapter;
	private List<Map<String, Object>> chatListItems;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Log.i(TAG, "createView");
		View chatListView = inflater.inflate(R.layout.fragment_chat_list,
				container, false);
		Log.i(TAG, "显示视图");
		chatListItems = new ArrayList<Map<String, Object>>();

		chatListAdapter = new SimpleAdapter(chatListView.getContext(),
				chatListItems, R.layout.chat_list_item, new String[] {
						"avatar", "nickname", "shotmsg" }, new int[] {
						R.id.header_iv, R.id.nickname_tv, R.id.shortmsg_tv });

		ListView chatLv = (ListView) chatListView.findViewById(R.id.chat_lv);
		chatLv.setAdapter(chatListAdapter);
		chatLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Log.i("IM", (String) );
				Intent i = new Intent(TeaChatListFragment.this.getActivity(),
						TeaChatRoomActivity.class);
				String nickName = ((String) chatListItems.get(position).get(
						"nickname")).split("@")[0];
				i.putExtra("nickName", nickName);
				i.putExtra("chatId", (String) chatListItems.get(position).get(
						"chatId"));
				startActivity(i);
			}
		});
		return chatListView;
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.i(TAG, "start");
		EventBus.getDefault().register(this);
		EventBus.getDefault().post(EBEvents.instanceGetChatListEvent());
	}

	@Override
	public void onStop() {
		Log.i(TAG, "stop");
		EventBus.getDefault().unregister(this);
		super.onStop();
	}

	/**
	 * @Description 刷新列表
	 * @param refreshChatListEvent
	 */
	public void onEventMainThread(
			EBEvents.RefreshChatListEvent refreshChatListEvent) {
		Log.i(TAG, "刷新聊天列表");
		chatListItems.clear();
		List<ChatListFormEntity> chatlst = refreshChatListEvent
				.getChatListForms();
		for (ChatListFormEntity clfe : chatlst) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("nickname", clfe.getJid());
			item.put("shotmsg", clfe.getShortMsg());
			item.put("avatar", clfe.getAvator());
			item.put("chatId", clfe.getChatId());
			chatListItems.add(item);
		}
		chatListAdapter.notifyDataSetChanged();
	}
}
