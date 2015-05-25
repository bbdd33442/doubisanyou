package com.doubisanyou.appcenter.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.offline.OfflineMessageManager;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.activity.TeaChatActivity;
import com.doubisanyou.appcenter.bean.AddressGroupEntity;
import com.doubisanyou.appcenter.bean.ChatListFormEntity;
import com.doubisanyou.appcenter.bean.ChatMsgTransferEntity;
import com.doubisanyou.appcenter.bean.ChatMsgViewEntity;
import com.doubisanyou.appcenter.bean.ContactEntity;
import com.doubisanyou.appcenter.bean.EBEvents;
import com.doubisanyou.appcenter.db.TeaDatabaseHelper;

import de.greenrobot.event.EventBus;

public class XmppService extends Service {
	private static final String TAG = XmppService.class.getSimpleName();
	private Thread loginThread;
	private XMPPTCPConnection conn = null;
	private XmppBinder binder = new XmppBinder();
	private ChatManager cm;
	private TeaDatabaseHelper teaDatabaseHelper;
	private ArrayList<AddressGroupEntity> addressGroupList;
	private ArrayList<ArrayList<ContactEntity>> contactList;
	// private List<ChatInfoEntity> chatInfoList = new
	// ArrayList<ChatInfoEntity>();
	private Map<String, String> chatInfoMap = new HashMap<String, String>();

	// private SendMsgReceiver smReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "is binded");
		return binder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "create");
		EventBus.getDefault().register(this);
		teaDatabaseHelper = new TeaDatabaseHelper(this, "teaDatabase.db3", 1);
		SmackConfiguration.DEBUG = true;
		TeaChatActivity.ACCOUNT_NAME = "blook";
		loginThread = new Thread() {
			String result = "failed connected";

			@Override
			public void run() {
				if (conn == null) {
					XMPPTCPConnectionConfiguration conf = XMPPTCPConnectionConfiguration
							.builder().setUsernameAndPassword("blook", "1234")
							.setSendPresence(false)
							.setSecurityMode(SecurityMode.disabled)
							.setServiceName("localhost")
							.setHost("192.168.1.118").setPort(5222).build();
					conn = new XMPPTCPConnection(conf);
				}
				if (conn.isConnected()) {
					conn.disconnect();
				}
				try {
					conn.connect();
					conn.login();
					// 接收离线消息
					OfflineMessageManager offlineManager = new OfflineMessageManager(
							conn);
					Log.i(TAG,
							"offline message count: "
									+ offlineManager.getMessageCount());
					List<Message> offlineMsgs = offlineManager.getMessages();
					for (Message msg : offlineMsgs) {
						Log.i(TAG, msg.getBody());
						String fromJid = msg.getFrom().split("/")[0];
						String fromId = getContactId(fromJid);
						if (fromId == null) {
							Log.i(TAG, "来自陌生人的消息");
							continue;
						}
						// offlineManager.
						MessageModel mm = new MessageModel();
						String chatId = isExistChatRoom(fromJid,
								String.valueOf(1));
						if (chatId == null) {
							ChatRoomModel crm = new ChatRoomModel();
							crm.jid = fromJid;
							crm.type = String.valueOf(1);
							crm.updateTime = getDatetime();
							crm = saveChatRoom(crm);
							if (crm.id != null)
								chatId = crm.id;
						}
						mm.chatId = chatId;
						mm.content = msg.getBody();
						mm.time = getDatetime();
						mm.from = fromId;
						mm.type = String.valueOf(1);
						mm.isRead = false; // 默认消息未读
						if (insertMessage(mm)) {
							Log.i(TAG, "message insert success");
						}
					}
					offlineManager.deleteMessages();
					conn.sendStanza(new Presence(Presence.Type.available));
					// 保存用户
					ContactModel owner = new ContactModel();
					owner.nickname = conn.getUser().split("@")[0];
					owner.jid = conn.getUser().split("/")[0];
					if (saveContact(owner)) {
						Log.i(TAG, " user save success!");
					}
					cm = ChatManager.getInstanceFor(conn);
					cm.addChatListener(new ChatManagerListener() {

						@Override
						public void chatCreated(Chat chat, boolean isLocally) {
							if (!isLocally) {
								Log.i(TAG,
										chat.getParticipant() + "\t"
												+ chat.getThreadID());
								String username = chat.getParticipant().split(
										"@")[0];
								String threadId = chatInfoMap.get(username);
								// 如果同一用户已存在聊天室，则关闭原聊天室
								if (threadId != null
										&& !threadId.equals(chat.getThreadID())) {
									cm.getThreadChat(threadId).close();
								}
								chatInfoMap.put(username, chat.getThreadID());
							}
							// 保存聊天室
							Log.i(TAG, "jid =" + chat.getParticipant());
							ChatRoomModel cm = new ChatRoomModel();
							cm.jid = chat.getParticipant().split("/")[0];
							cm.type = String.valueOf(1);
							cm.updateTime = getDatetime();
							saveChatRoom(cm);
							chat.addMessageListener(new ChatMessageListener() {

								@Override
								public void processMessage(Chat chat,
										Message msg) {
									Log.i(TAG, msg.getBody());
									// if(activityName.equals(""))
									// 广播收到的消息
									ChatMsgTransferEntity cmte = new ChatMsgTransferEntity();
									cmte.setFrom(msg.getFrom().split("@")[0]);
									cmte.setTo(TeaChatActivity.ACCOUNT_NAME);
									cmte.setContent(msg.getBody());
									cmte.setType(1);
									// 向teaChatRoomActivity发送消息
									EBEvents.ReceiveChatMsgEvent receiveChatMsgEvent = EBEvents
											.instanceReceiveChatMsgEvent();
									receiveChatMsgEvent
											.setChatMsgTransferEntity(cmte);
									EventBus.getDefault().post(
											receiveChatMsgEvent);
									// 向teaChatListFragment发送消息
									// ....
									// 更新chatroom时间
									// ....
									// 保存未读消息
									String fromJid = msg.getFrom().split("/")[0];
									String fromId = getContactId(fromJid);
									MessageModel mm = new MessageModel();
									mm.chatId = isExistChatRoom(fromJid,
											String.valueOf(1));
									mm.content = msg.getBody();
									mm.time = getDatetime();
									mm.from = fromId;
									mm.type = String.valueOf(1);
									mm.isRead = false; // 默认消息未读
									if (insertMessage(mm)) {
										Log.i(TAG, "message insert success");
									}
								}
							});
						}

					});
					// 获取通讯录
					final Roster roster = Roster.getInstanceFor(conn);
					// roster.setSubscriptionMode(SubscriptionMode.manual);
					// Gson gson = new Gson();
					roster.addRosterListener(new RosterListener() {

						@Override
						public void presenceChanged(Presence presence) {
							System.out.println("changed====================");
							System.out.println(presence.toString());
							// presence.getFrom();
							// 用户状态改变
							loadContact(roster);
							EBEvents.GetAddressListEvent getAddressListEvent = EBEvents
									.instanceGetAddressListEvent();
							getAddressListEvent
									.setAddressGroupList(addressGroupList);
							getAddressListEvent.setContactList(contactList);
							EventBus.getDefault().post(getAddressListEvent );
						}

						@Override
						public void entriesUpdated(Collection<String> entries) {
							System.out.println("updated====================");
							for (String e : entries) {
								System.out.println(e);
							}
						}

						@Override
						public void entriesDeleted(Collection<String> entries) {
							System.out.println("deleted====================");
							for (String e : entries) {
								System.out.println(e);
							}
						}

						@Override
						public void entriesAdded(Collection<String> entries) {
							System.out.println("added====================");
							for (String e : entries) {
								System.out.println(e);
							}
						}
					});
					result = "XmppSever has connected";
					loadContact(roster);
					EBEvents.GetAddressListEvent getAddressListEvent = EBEvents
							.instanceGetAddressListEvent();
					getAddressListEvent.setAddressGroupList(addressGroupList);
					getAddressListEvent.setContactList(contactList);
					EventBus.getDefault().post(getAddressListEvent);
					// 获取聊天列表
					List<ChatListFormEntity> chatlst = getChatListForms();
					EBEvents.RefreshChatListEvent refreshChatListEvent = EBEvents
							.instanceRefreshChatListEvent();
					refreshChatListEvent.setChatListForms(chatlst);
					EventBus.getDefault().post(refreshChatListEvent);
				} catch (SmackException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XMPPException e) {
					e.printStackTrace();
				}
				Log.i(TAG, result);
			}
		};
		loginThread.start();
	}

	@Override
	public void onDestroy() {
		if (teaDatabaseHelper != null)
			teaDatabaseHelper.close();
		EventBus.getDefault().unregister(this);
		super.onDestroy();
		// unregisterReceiver(smReceiver);
	}

	public class XmppBinder extends Binder {
		public XMPPConnection getConn() {
			return conn;
		}
	}

	/**
	 * @Description 发送消息
	 * @param sendChatMsgEvent
	 */
	public void onEventBackgroundThread(
			EBEvents.SendChatMsgEvent sendChatMsgEvent) {
		Chat chat = null;
		ChatMsgTransferEntity cme = sendChatMsgEvent.getChatMsgTransferEntity();
		String username = cme.getTo();
		String jid = username + "@" + conn.getServiceName();
		String threadId = chatInfoMap.get(username);
		if (threadId != null) {
			chat = cm.getThreadChat(threadId);
		}
		if (chat == null) {
			chat = cm.createChat(jid);
			chatInfoMap.put(username, chat.getThreadID());
		}
		try {
			chat.sendMessage(cme.getContent());
			// 保存发送的消息
			MessageModel mm = new MessageModel();
			mm.chatId = isExistChatRoom(jid, String.valueOf(1));
			mm.content = cme.getContent();
			mm.from = getContactId(conn.getUser().split("/")[0]);
			mm.time = getDatetime();
			mm.type = String.valueOf(1); // 文本类型
			mm.isRead = true;
			insertMessage(mm);
		} catch (NotConnectedException e) {
			e.printStackTrace();
			Toast.makeText(this, "消息发送失败！", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * @Description 获取聊天列表
	 * @param getChatListEvent
	 */

	public void onEventBackgroundThread(
			EBEvents.GetChatListEvent getChatListEvent) {
		List<ChatListFormEntity> chatlst = getChatListForms();
		EBEvents.RefreshChatListEvent refreshChatListEvent = EBEvents
				.instanceRefreshChatListEvent();
		refreshChatListEvent.setChatListForms(chatlst);
		EventBus.getDefault().post(refreshChatListEvent);
	}

	public void onEventBackgroundThread(
			EBEvents.GetChatHistoryEvent getChatHistoryEvent) {
		List<ChatMsgViewEntity> chatMsgViews = getChatHistory(getChatHistoryEvent
				.getChatId());
		EBEvents.RefreshChatHistoryEvent refreshChatHistoryEvent = EBEvents
				.instanceRefreshChatHistoryEvent();
		refreshChatHistoryEvent.setChatMsgViews(chatMsgViews);
		EventBus.getDefault().post(refreshChatHistoryEvent);
	}

	/**
	 * @Description 添加好友
	 * @param addFriendEvent
	 */
	public void onEventBackgroundThread(EBEvents.AddFriendEvent addFriendEvent) {
		String jid = addFriendEvent.getJid() + "@" + conn.getServiceName();
		Roster roster = Roster.getInstanceFor(conn);
		while (!roster.isLoaded())
			;
		if (roster.contains(jid)) {
			Log.i(TAG, "存在用户" + jid);
			try {
				roster.createEntry(jid, jid.split("@")[0],
						new String[] { "Friends" });
			} catch (NotLoggedInException e) {
				e.printStackTrace();
			} catch (NoResponseException e) {
				e.printStackTrace();
			} catch (XMPPErrorException e) {
				e.printStackTrace();
			} catch (NotConnectedException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "用户" + jid + "不存在");
		}

	}

	private String getDatetime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	private boolean saveContact(ContactModel cm) {
		SQLiteDatabase db = teaDatabaseHelper.getWritableDatabase();
		String tableName = "tea_contact";
		ContentValues values = new ContentValues();
		boolean result = false;
		if (StringUtils.isNotEmpty(cm.nickname)) {
			values.put("teco_nickname", cm.nickname);
		}
		if (StringUtils.isNotEmpty(cm.backname)) {
			values.put("teco_backname", cm.backname);
		}
		if (StringUtils.isNotEmpty(cm.jid)) {
			values.put("teco_jid", cm.jid);
		}
		if (StringUtils.isNotEmpty(cm.gxqm)) {
			values.put("teco_gxqm", cm.gxqm);
		}
		Cursor cursor = db.rawQuery(
				"SELECT * FROM tea_contact WHERE teco_jid = ?",
				new String[] { cm.jid });
		// 如果存在则更新
		if (cursor.getCount() > 0) {
			result = db.update(tableName, values, "teco_jid = ?",
					new String[] { String.valueOf(cm.jid) }) > 0 ? true : false;
		} else {
			result = db.insert(tableName, null, values) > 0 ? true : false;
		}
		return result;
	}

	private boolean insertMessage(MessageModel mm) {
		SQLiteDatabase db = teaDatabaseHelper.getWritableDatabase();
		String tableName = "tea_message";
		ContentValues values = new ContentValues();
		values.put("teme_content", mm.content);
		values.put("teme_type", mm.type);
		values.put("teme_from", mm.from);
		values.put("teme_to", mm.to);
		values.put("teme_time", mm.time);
		values.put("teme_tech_id", mm.chatId);
		values.put("teme_is_read", mm.isRead);
		return db.insert(tableName, null, values) > 0 ? true : false;
	}

	private ChatRoomModel saveChatRoom(ChatRoomModel crm) {
		// boolean result = false;
		SQLiteDatabase db = teaDatabaseHelper.getWritableDatabase();
		String tableName = "tea_chatroom";
		ContentValues values = new ContentValues();
		values.put("tech_type", crm.type);
		values.put("tech_update_time", crm.updateTime);
		// 如果存在则更新
		String rid = isExistChatRoom(crm.jid, crm.type);
		if (rid != null) {
			db.update(tableName, values, "_id = ?", new String[] { rid });
			crm.id = rid;
		} else {
			db.beginTransaction();
			Long roomId = db.insert(tableName, null, values);
			String contactId = getContactId(crm.jid);
			if (contactId != null) {
				ContentValues cv = new ContentValues();
				cv.put("tett_teco_id", contactId);
				cv.put("tett_tech_id", roomId);
				db.insert("tea_teco_tech", null, cv);
				db.setTransactionSuccessful();
				crm.id = String.valueOf(roomId);
			} else {
				Log.e(TAG, "来自陌生人的消息");
				return null;
			}
			db.endTransaction();
		}
		return crm;
	}

	private String getContactId(String jid) {
		SQLiteDatabase db = teaDatabaseHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT _id FROM tea_contact WHERE teco_jid = ?",
				new String[] { jid });
		if (cursor.moveToNext())
			return cursor.getString(0);
		return null;
	}

	private String isExistChatRoom(String jid, String roomType) {
		SQLiteDatabase db = teaDatabaseHelper.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT tc._id FROM tea_contact AS tn, tea_teco_tech AS tt, tea_chatroom AS tc WHERE tn._id = tt.tett_teco_id AND tt.tett_tech_id = tc._id AND tc.tech_type = ? AND tn.teco_jid = ?",
						new String[] { roomType, jid });
		if (cursor.moveToNext())
			return cursor.getString(0);
		return null;

	}

	private List<ChatListFormEntity> getChatListForms() {
		SQLiteDatabase db = teaDatabaseHelper.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT a.teco_jid AS jid, d.teme_content AS content, Max(d.teme_time) AS time, b._id AS chatid FROM tea_contact AS a, tea_chatroom AS b, tea_teco_tech AS c, tea_message AS d "
								+ "WHERE a._id = c.tett_teco_id "
								+ "AND c.tett_tech_id = b._id "
								+ "AND d.teme_tech_id = b._id "
								+ "GROUP BY b._id "
								+ "ORDER BY b.tech_update_time", null);
		ArrayList<ChatListFormEntity> list = new ArrayList<ChatListFormEntity>();
		while (cursor.moveToNext()) {
			ChatListFormEntity clfe = new ChatListFormEntity();
			clfe.setAvator(R.drawable.abaose);
			clfe.setJid(cursor.getString(0));
			clfe.setShortMsg(cursor.getString(1));
			clfe.setUpdateTime(cursor.getString(2));
			clfe.setChatId(cursor.getString(3));
			list.add(clfe);
		}
		return list;
	}

	private List<ChatMsgViewEntity> getChatHistory(String chatId) {
		ArrayList<ChatMsgViewEntity> list = new ArrayList<ChatMsgViewEntity>();
		if (StringUtils.isNullOrEmpty(chatId)) {
			Log.i(TAG, "chatId为空");
			return list;
		}
		SQLiteDatabase db = teaDatabaseHelper.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"select a.[teme_content], c.[teco_nickname], a.[teme_from], a.[teme_time]"
								+ " from tea_message as a, tea_chatroom as b, tea_contact as c"
								+ " where c._id = a.teme_from"
								+ " and a.[teme_tech_id] = b.[_id]"
								+ " and b.[_id] = ?", new String[] { chatId });
		while (cursor.moveToNext()) {
			ChatMsgViewEntity cmve = new ChatMsgViewEntity();
			cmve.setContent(cursor.getString(0));
			cmve.setFrom(cursor.getString(1));
			if (cursor.getInt(2) == 1) {
				cmve.setSend(true);
			} else
				cmve.setSend(false);
			cmve.setTime(cursor.getString(3));
			list.add(cmve);
		}
		return list;
	}

	private void loadContact(Roster roster) {
		while (!roster.isLoaded())
			;
		Log.i(TAG, "count = " + roster.getEntryCount());

		for (RosterEntry e : roster.getEntries()) {
			Log.i(TAG, e.toString());
		}
		addressGroupList = new ArrayList<AddressGroupEntity>();
		contactList = new ArrayList<ArrayList<ContactEntity>>();
		for (RosterGroup g : roster.getGroups()) {
			AddressGroupEntity cge = new AddressGroupEntity();
			cge.setGroupName(g.getName());
			// cge.setOnline(g.getEntryCount());
			cge.setGroupCount(g.getEntryCount());
			int onlineCount = 0;
			ArrayList<ContactEntity> item = new ArrayList<ContactEntity>();
			for (RosterEntry e : g.getEntries()) {
				ContactEntity le = new ContactEntity();
				le.setName(e.getName());
				le.setGxqm(e.getUser());
				item.add(le);
				Presence presence = roster.getPresence(e.getUser());
				if (presence.isAvailable()) {
					onlineCount++;
				}
				// 保存通讯录
				ContactModel cm = new ContactModel();
				Log.i(TAG, "name:" + e.getName());
				cm.nickname = e.getName();
				cm.jid = e.getUser();
				if (saveContact(cm)) {
					Log.i(TAG, "contact save success!");
				}
			}
			cge.setOnline(onlineCount);
			addressGroupList.add(cge);
			contactList.add(item);
		}
	}

	class MessageModel {
		String content;
		String type;
		String chatId;
		String time;
		String from;
		String to;
		boolean isRead;

		public MessageModel() {
		}
	}

	class ChatRoomModel {
		String id;
		String type;
		String jid;
		String updateTime;

		public ChatRoomModel() {
		}
	}

	class ContactModel {
		int id = -1;
		String nickname;
		String jid;
		String backname;
		String gxqm;

		public ContactModel() {
		}
	}

}
