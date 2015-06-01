package com.doubisanyou.appcenter.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.bean.ChatListFormEntity;
import com.doubisanyou.appcenter.widget.NBadgeView;

public class ChatListAdapter extends BaseAdapter {
	// private List<Map<String, Object>> chatListItems;
	private List<ChatListFormEntity> chatlst;
	private LayoutInflater inflater;
	private Map<String, View> itemViews;

	public ChatListAdapter(Context context, List<ChatListFormEntity> chatlst) {
		this.inflater = LayoutInflater.from(context);
		this.chatlst = chatlst;
		this.itemViews = new HashMap<String, View>();
		// Log.i("ChatListAdapter", "init");
	}

	@Override
	public int getCount() {
		return chatlst.size();
	}

	@Override
	public Object getItem(int pos) {
		return chatlst.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		NBadgeView bv;
		ViewHolder holder;
		if (view == null) {
			view = inflater.inflate(R.layout.chat_list_item, null);
			bv = new NBadgeView(inflater.getContext());
			holder = new ViewHolder();
			holder.bv = bv;
			view.setTag(holder);
		}
		ImageView avatar = (ImageView) view.findViewById(R.id.itcl_avatar_iv);
		TextView nickName = (TextView) view.findViewById(R.id.itcl_nickname_tv);
		TextView shortMsg = (TextView) view.findViewById(R.id.itcl_shortmsg_tv);
		// Map<String, Object> childMap = (Map<String, Object>)getItem(pos);
		ChatListFormEntity clfe = (ChatListFormEntity) getItem(pos);
		avatar.setBackgroundResource(clfe.getAvator());
		nickName.setText(clfe.getJid());
		shortMsg.setText(clfe.getShortMsg());
		holder = (ViewHolder) view.getTag();
		bv = holder.bv;
		bv.setTargetView(avatar);
		int alertCount = clfe.getAlertCount();
		/*Log.i("ChatListAdapter", "alertCount:" + alertCount + "\tnickName"
				+ clfe.getJid() + "\tpos" + pos);*/
		if (alertCount > 0) {
			bv.setBadgeCount(alertCount);
			bv.setVisibility(View.VISIBLE);
		} else
			bv.setVisibility(View.GONE);
		itemViews.put(clfe.getJid(), view);
		// Log.i("getView", clfe.getJid());
		return view;
		/*
		 * LinearLayout ll = new LinearLayout(inflater.getContext()); view =
		 * inflater.inflate(R.layout.chat_list_item, null); ImageView avatar =
		 * (ImageView) view.findViewById(R.id.itcl_avatar_iv); TextView nickName
		 * = (TextView) view.findViewById(R.id.itcl_nickname_tv); TextView
		 * shortMsg = (TextView) view.findViewById(R.id.itcl_shortmsg_tv); //
		 * Map<String, Object> childMap = (Map<String, Object>)getItem(pos);
		 * ChatListFormEntity clfe = (ChatListFormEntity) getItem(pos);
		 * avatar.setBackgroundResource(clfe.getAvator());
		 * nickName.setText(clfe.getJid());
		 * shortMsg.setText(clfe.getShortMsg()); BadgeView bv = new
		 * BadgeView(inflater.getContext(),avatar); // bv.setTargetView(avatar);
		 * bv.setText("2"); bv.show(); ll.addView(view); return ll;
		 */
	}

	static class ViewHolder {
		NBadgeView bv;
	}
}
