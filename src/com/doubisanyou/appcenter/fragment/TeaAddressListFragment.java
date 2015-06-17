package com.doubisanyou.appcenter.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.activity.TeaChatRoomActivity;
import com.doubisanyou.appcenter.adapter.TeaAddressListViewAdapter;
import com.doubisanyou.appcenter.adapter.TeaAddressListViewAdapter.PosBean;
import com.doubisanyou.appcenter.bean.AddressGroupEntity;
import com.doubisanyou.appcenter.bean.ContactEntity;
import com.doubisanyou.appcenter.bean.EBEvents;

import de.greenrobot.event.EventBus;

public class TeaAddressListFragment extends Fragment {
	private static final String TAG = TeaAddressListFragment.class
			.getSimpleName();
	private ExpandableListView mExpandableListView;
	private TeaAddressListViewAdapter mTalvAdapter;
	private ArrayList<AddressGroupEntity> addressGroupList;
	private ArrayList<ArrayList<ContactEntity>> contactList;

	@Override
	public void onDestroyView() {
		EventBus.getDefault().unregister(this);
		super.onDestroyView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		EventBus.getDefault().register(this);
		View addressListView = inflater.inflate(R.layout.fragment_address_list,
				container, false);
		mExpandableListView = (ExpandableListView) addressListView
				.findViewById(R.id.tea_contact_elv);
		mExpandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View view,
					int groupPosition, int childPosition, long id) {
				String nickName = ((ContactEntity) mTalvAdapter.getChild(
						groupPosition, childPosition)).getName();

				Intent intent = new Intent(TeaAddressListFragment.this
						.getActivity(), TeaChatRoomActivity.class);
				intent.putExtra("nickName", nickName);
				startActivity(intent);
				Log.i(TAG, "click");
				return false;
			}
		});
		mExpandableListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View view, int pos, long id) {

						final TeaAddressListViewAdapter.PosBean pb = (PosBean) view
								.getTag();
						if (pb != null) {
							Log.i(TAG, "groupPos:" + pb.groupPos
									+ "\tchildPos:" + pb.childPos);
							PopupMenu addressListMenu = new PopupMenu(
									TeaAddressListFragment.this.getActivity(),
									view);
							addressListMenu.getMenuInflater().inflate(
									R.menu.ppm_address_list_item,
									addressListMenu.getMenu());
							addressListMenu
									.setOnMenuItemClickListener(new OnMenuItemClickListener() {

										@Override
										public boolean onMenuItemClick(
												MenuItem item) {
											switch (item.getItemId()) {
											case R.id.ppma_item_delete_friend:
												EBEvents.DeleteFriendEvent deleteFriendEvent = EBEvents
														.instanceDeleteFriendEvent();
												String jid = ((ContactEntity) mTalvAdapter
														.getChild(pb.groupPos,
																pb.childPos))
														.getName();
												deleteFriendEvent.setJid(jid);
												EventBus.getDefault().post(
														deleteFriendEvent);
											}
											return false;
										}
									});
							addressListMenu.show();
						}

						return true;
					}

				});
		addressGroupList = new ArrayList<AddressGroupEntity>();
		contactList = new ArrayList<ArrayList<ContactEntity>>();
		mTalvAdapter = new TeaAddressListViewAdapter(this.getActivity(),
				addressGroupList, contactList);
		mExpandableListView.setAdapter(mTalvAdapter);
		return addressListView;
	}

	public void onEventMainThread(
			EBEvents.GetAddressListEvent getAddressListEvent) {
		// Toast.makeText(this, "text", Toast.LENGTH_SHORT).show();
		addressGroupList.clear();
		contactList.clear();
		addressGroupList.addAll(getAddressListEvent.getAddressGroupList());
		contactList.addAll(getAddressListEvent.getContactList());
		mTalvAdapter.notifyDataSetChanged();
		/*
		 * int groupCount = addressGroupList.size(); int contactCount =
		 * contactList.size(); String[] groupArr = new String[groupCount];
		 * String[][] contactArr = new String[contactCount][]; for (int i = 0; i
		 * < groupCount; i++) { groupArr[i] =
		 * addressGroupList.get(i).getGroupName(); } for (int i = 0; i <
		 * contactCount; i++) { contactArr[i] = new
		 * String[contactList.get(i).size()]; for (int j = 0; j <
		 * contactList.get(i).size(); j++) { contactArr[i][j] =
		 * contactList.get(i).get(j).getName(); } }
		 */
		Log.i(TAG,
				"selectedItemId:" + mExpandableListView.getSelectedItemId()
						+ "\t\tselectedItemPosition:"
						+ mExpandableListView.getSelectedItemPosition()
						+ "\t\tselectedPosition:"
						+ mExpandableListView.getSelectedPosition());
	}
}
