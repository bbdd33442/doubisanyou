package com.doubisanyou.appcenter.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.ListView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.activity.TeaChatRoomActivity;
import com.doubisanyou.appcenter.adapter.ChatListAdapter;
import com.doubisanyou.appcenter.bean.ChatListFormEntity;
import com.doubisanyou.appcenter.bean.ContactEntity;
import com.doubisanyou.appcenter.bean.EBEvents;

import de.greenrobot.event.EventBus;

public class TeaChatListFragment extends Fragment {
	private static final String TAG = TeaChatListFragment.class.getSimpleName();
	// private String[] nicknames = new String[] { "blook", "admin" };
	// private String[] shotMsgs = new String[] { "你好！", "haha" };
	private ChatListAdapter chatListAdapter;
	private List<ChatListFormEntity> chatlst;
	private ListView chatLv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Log.i(TAG, "createView");
		EventBus.getDefault().register(this);

		View chatListView = inflater.inflate(R.layout.fragment_chat_list,
				container, false);
		Log.i(TAG, "显示视图");
		chatlst = new ArrayList<ChatListFormEntity>();
		chatListAdapter = new ChatListAdapter(this.getActivity(), chatlst);

		chatLv = (ListView) chatListView.findViewById(R.id.chat_lv);
		chatLv.setAdapter(chatListAdapter);
		chatLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Log.i("IM", (String) );
				Intent i = new Intent(TeaChatListFragment.this.getActivity(),
						TeaChatRoomActivity.class);
				String nickName = chatlst.get(position).getJid().split("@")[0];
				i.putExtra("nickName", nickName);
				i.putExtra("chatId", chatlst.get(position).getChatId());
				startActivity(i);
			}
		});
		chatLv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final int pos = position;
				PopupMenu chatListMenu = new PopupMenu(TeaChatListFragment.this
						.getActivity(), view);
				chatListMenu.getMenuInflater().inflate(
						R.menu.ppm_chat_list_item, chatListMenu.getMenu());
				chatListMenu
						.setOnMenuItemClickListener(new OnMenuItemClickListener() {

							@Override
							public boolean onMenuItemClick(MenuItem item) {
								switch (item.getItemId()) {
								case R.id.ppmc_item_delete_chat_form:
									EBEvents.DeleteChatRoomEvent deleteChatRoomEvent = EBEvents
											.instanceDeleteChatRoomEvent();
									ChatListFormEntity clfe = (ChatListFormEntity) chatListAdapter
											.getItem(pos);
									deleteChatRoomEvent.setChatId(clfe
											.getChatId());
									deleteChatRoomEvent.setJid(clfe.getJid());
									EventBus.getDefault().post(
											deleteChatRoomEvent);
								}
								return false;
							}
						});
				chatListMenu.show();
				return true;
			}
		});
		return chatListView;
	}

	@Override
	public void onStart() {
		super.onStart();
		EventBus.getDefault().post(EBEvents.instanceGetChatListEvent());
	}

	@Override
	public void onDestroyView() {
		EventBus.getDefault().unregister(this);
		super.onDestroyView();
	}

	/**
	 * @Description 刷新列表
	 * @param refreshChatListEvent
	 */
	public void onEventMainThread(
			EBEvents.RefreshChatListEvent refreshChatListEvent) {

		chatlst.clear();
		for (ChatListFormEntity e : refreshChatListEvent.getChatListForms()) {
			chatlst.add(e);
		}
		chatListAdapter.notifyDataSetChanged();
		// chatLv.setSelection(chatLv.getCount() - 1);
		Log.i(TAG, "刷新聊天列表: " + chatlst.size());
	}
}
