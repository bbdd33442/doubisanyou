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
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jivesoftware.smackx.xdata.Form;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
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
import com.doubisanyou.appcenter.bean.EBEvents.RequestLoginEvent;
import com.doubisanyou.appcenter.bean.EBEvents.RequestLogoutEvent;
import com.doubisanyou.appcenter.bean.EBEvents.RequestRegisterEvent;
import com.doubisanyou.appcenter.bean.EBEvents.RequestSaveVCardEvent;
import com.doubisanyou.appcenter.bean.EBEvents.RequestVCardEvent;
import com.doubisanyou.appcenter.bean.EBEvents.ResponseLoginEvent;
import com.doubisanyou.appcenter.bean.EBEvents.ResponseRegisterEvent;
import com.doubisanyou.appcenter.bean.EBEvents.ResponseSaveVCardEvent;
import com.doubisanyou.appcenter.bean.EBEvents.ResponseVCardEvent;
import com.doubisanyou.appcenter.date.Config;
import com.doubisanyou.appcenter.db.TeaDatabaseHelper;

import de.greenrobot.event.EventBus;

public class XmppService extends Service {
	private static final String TAG = XmppService.class.getSimpleName();
	public static boolean IS_CONNECT = false; // 是否连接
	public static boolean IS_LOGIN = false; // 是否登录
	private XMPPTCPConnection conn = null;
	private XmppBinder binder = new XmppBinder();
	private ChatManager cm;
	private TeaDatabaseHelper teaDatabaseHelper;
	private ArrayList<AddressGroupEntity> addressGroupList;
	private ArrayList<ArrayList<ContactEntity>> contactList;
	// private List<ChatInfoEntity> chatInfoList = new
	// ArrayList<ChatInfoEntity>();
	private Map<String, String> chatInfoMap = new HashMap<String, String>();
	private static SharedPreferences userInfoSp;

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "is binded");
		return binder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "create");
		userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		EventBus.getDefault().register(this);
		teaDatabaseHelper = new TeaDatabaseHelper(this,
				Config.DEFAULT_DATABASE, 1);
		SmackConfiguration.DEBUG = true;
		new Thread() {
			public void run() {
				doConnect(Config.XMPP_SERVICE_NAME, Config.XMPP_HOST,
						Config.XMPP_PORT);
			};
		}.start();
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
			makeToast("消息发送错误", Toast.LENGTH_LONG);
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
		String chatId = getChatHistoryEvent.getChatId();
		String jid = getChatHistoryEvent.getJid() + "@" + conn.getServiceName();
		// Log.i(TAG, "chatId"+chatId+"\tjid"+jid);
		if (StringUtils.isNullOrEmpty(chatId)) {
			chatId = isExistChatRoom(jid, getChatHistoryEvent.getRoomType());
		}
		List<ChatMsgViewEntity> chatMsgViews = getChatHistory(chatId);
		EBEvents.RefreshChatHistoryEvent refreshChatHistoryEvent = EBEvents
				.instanceRefreshChatHistoryEvent();
		refreshChatHistoryEvent.setChatMsgViews(chatMsgViews);
		EventBus.getDefault().post(refreshChatHistoryEvent);
		int count = changeUnreadMsg(chatId);
		Log.i(TAG, "将未读消息设为已读：" + count);
	}

	/**
	 * @Description 添加好友
	 * @param addFriendEvent
	 */
	public void onEventBackgroundThread(EBEvents.AddFriendEvent addFriendEvent) {
		String jid = addFriendEvent.getJid() + "@" + conn.getServiceName();
		Roster roster = Roster.getInstanceFor(conn);
		// 加载roster，10次
		for (int i = 0; i < 10; i++) {
			if (!roster.isLoaded())
				try {
					roster.reload();
				} catch (NotLoggedInException e) {
					e.printStackTrace();
					return;
				} catch (NotConnectedException e) {
					e.printStackTrace();
					return;
				}
			else
				break;
		}
		if (!roster.isLoaded()) {
			Log.i(TAG, "添加好友失败");
			makeToast("添加好友失败，网络问题", Toast.LENGTH_LONG);
			return;
		}
		UserSearchManager usm = new UserSearchManager(conn);

		ReportedData rd = null;
		try {
			Form sf = usm.getSearchForm("search." + conn.getServiceName());
			Form af = sf.createAnswerForm();
			af.setAnswer("Username", true);
			af.setAnswer("search", addFriendEvent.getJid());
			rd = usm.getSearchResults(af, "search." + conn.getServiceName());
		} catch (NoResponseException e1) {
			e1.printStackTrace();
		} catch (XMPPErrorException e1) {
			e1.printStackTrace();
		} catch (NotConnectedException e1) {
			e1.printStackTrace();
		}
		if (rd != null && rd.getRows().size() > 0) {
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
			makeToast("该用户不存在", Toast.LENGTH_LONG);
		}

	}

	/**
	 * @Description 删除好友
	 * @param deleteFriendEvent
	 */
	public void onEventBackgroundThread(
			EBEvents.DeleteFriendEvent deleteFriendEvent) {
		String jid = deleteFriendEvent.getJid() + "@" + conn.getServiceName();
		Roster roster = Roster.getInstanceFor(conn);
		Log.i(TAG, "删除好友:" + jid);
		// 加载roster，10次
		for (int i = 0; i < 10; i++) {
			if (!roster.isLoaded())
				try {
					roster.reload();
				} catch (NotLoggedInException e) {
					e.printStackTrace();
					return;
				} catch (NotConnectedException e) {
					e.printStackTrace();
					return;
				}
			else
				break;
		}
		if (!roster.isLoaded()) {
			Log.i(TAG, "删除好友失败");
			return;
		}
		if (roster.contains(jid)) {
			try {
				roster.removeEntry(roster.getEntry(jid));
				// 删除数据库中的用户数据
				deleteContact(jid);
				// 删除聊天室线程
				String username = deleteFriendEvent.getJid().split("@")[0];
				String threadId = chatInfoMap.get(username);
				if (threadId != null) {
					cm.getThreadChat(threadId).close();
					chatInfoMap.remove(username);
				}
			} catch (NotLoggedInException e) {
				e.printStackTrace();
			} catch (NoResponseException e) {
				e.printStackTrace();
			} catch (XMPPErrorException e) {
				e.printStackTrace();
			} catch (NotConnectedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Description 删除聊天室
	 * @param deleteChatRoomEvent
	 */
	public void onEventBackgroundThread(
			EBEvents.DeleteChatRoomEvent deleteChatRoomEvent) {
		String chatId = deleteChatRoomEvent.getChatId();
		deleteChatRoom(chatId);
		// 关闭聊天线程
		String username = deleteChatRoomEvent.getJid().split("@")[0];
		String threadId = chatInfoMap.get(username);
		if (threadId != null) {
			cm.getThreadChat(threadId).close();
			chatInfoMap.remove(username);
		}
		// 刷新聊天列表
		EBEvents.RefreshChatListEvent refreshChatListEvent = EBEvents
				.instanceRefreshChatListEvent();
		refreshChatListEvent.setChatListForms(getChatListForms());
		EventBus.getDefault().post(refreshChatListEvent);
	}

	/**
	 * @Description 用户登陆
	 * @param reqloginEvent
	 */
	public void onEventBackgroundThread(RequestLoginEvent reqloginEvent) {
		Log.i(TAG, "登陆方法");
		final String username = reqloginEvent.getUsername();
		final String password = reqloginEvent.getPassword();
		new Thread() {
			public void run() {
				doLogin(username, password);
			};
		}.start();
	}

	/**
	 * @Description 用户注册
	 * @param reqRegisterEvent
	 */
	public void onEventBackgroundThread(RequestRegisterEvent reqRegisterEvent) {
		final String username = reqRegisterEvent.getUsername();
		final String password = reqRegisterEvent.getPassword();
		new Thread() {
			public void run() {
				doRegister(username, password);
			};
		}.start();
	}

	/**
	 * @Description 请求VCard
	 * @param requestVCardEvent
	 */
	public void onEventBackgroundThread(RequestVCardEvent requestVCardEvent) {
		String username = requestVCardEvent.getUsername();
		VCard vCard = getUserVCard(username);
		ResponseVCardEvent responseVCardEvent = EBEvents
				.instanceResponseVCardEvent();
		responseVCardEvent.setvCard(vCard);
		EventBus.getDefault().post(responseVCardEvent);
	}

	/**
	 * @Description 保存VCard
	 * @param requestSaveVCardEvent
	 */
	public void onEventBackgroundThread(
			RequestSaveVCardEvent requestSaveVCardEvent) {
		VCard vCard = requestSaveVCardEvent.getvCard();
		int respCode = -1;
		if (saveUserVCard(vCard)) {
			respCode = 1;
		} else
			respCode = 0;
		ResponseSaveVCardEvent responseSaveVCardEvent = EBEvents
				.instanceResponseSaveVCardEvent();
		responseSaveVCardEvent.setRespCode(respCode);
		EventBus.getDefault().post(responseSaveVCardEvent);
	}

	/**
	 * @Description 注销事件
	 * @param requestLogoutEvent
	 */
	public void onEventBackgroundThread(RequestLogoutEvent requestLogoutEvent) {
		doLogout();
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
		cursor.close();
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
		String result = null;
		Cursor cursor = db.rawQuery(
				"SELECT _id FROM tea_contact WHERE teco_jid = ?",
				new String[] { jid });
		if (cursor.moveToNext()) {
			result = cursor.getString(0);
		}
		cursor.close();
		return result;
	}

	/**
	 * @Description 是否存在聊天室
	 * @param jid
	 *            用户jid
	 * @param roomType
	 *            聊天室类型
	 * @return chatId or null
	 */
	private String isExistChatRoom(String jid, String roomType) {
		SQLiteDatabase db = teaDatabaseHelper.getReadableDatabase();
		String result = null;
		Cursor cursor = db
				.rawQuery(
						"SELECT tc._id FROM tea_contact AS tn, tea_teco_tech AS tt, tea_chatroom AS tc WHERE tn._id = tt.tett_teco_id AND tt.tett_tech_id = tc._id AND tc.tech_type = ? AND tn.teco_jid = ?",
						new String[] { roomType, jid });
		if (cursor.moveToNext())
			result = cursor.getString(0);
		cursor.close();
		return result;

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
								+ "ORDER BY b.tech_update_time DESC", null);
		ArrayList<ChatListFormEntity> list = new ArrayList<ChatListFormEntity>();
		while (cursor.moveToNext()) {
			ChatListFormEntity clfe = new ChatListFormEntity();
			clfe.setAvator(R.drawable.abaose);
			clfe.setJid(cursor.getString(0));
			clfe.setShortMsg(cursor.getString(1));
			clfe.setUpdateTime(cursor.getString(2));
			String chatId = cursor.getString(3);
			clfe.setChatId(chatId);
			int unReadCount = getUnReadMsgCount(chatId);
			Log.i(TAG, "未读消息数：" + unReadCount);
			clfe.setAlertCount(unReadCount);
			list.add(clfe);
		}
		cursor.close();
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
		cursor.close();
		return list;
	}

	private int changeUnreadMsg(String chatId) {
		SQLiteDatabase db = teaDatabaseHelper.getWritableDatabase();
		/*
		 * Cursor cursor = db .rawQuery(
		 * "UPDATE tea_message SET teme_is_read = 0 WHERE teme_is_read = 1 AND teme_tech_id = ?"
		 * , new String[] { chatId });
		 */
		ContentValues cv = new ContentValues();
		cv.put("teme_is_read", 1);
		return db.update("tea_message", cv,
				"teme_is_read = 0 AND teme_tech_id = ?",
				new String[] { chatId });
	}

	private void deleteContact(String jid) {
		if (StringUtils.isNullOrEmpty(jid)) {
			Log.i(TAG, "jid为空");
			return;
		}
		SQLiteDatabase db = teaDatabaseHelper.getWritableDatabase();
		// 1.查看是否存在聊天室
		// 2.删除关联表，聊天室表，消息表，和通讯录表中的数据
		String chatId = isExistChatRoom(jid, TeaDatabaseHelper.SINGLE_CHATROOM);
		db.beginTransaction();
		if (!StringUtils.isNullOrEmpty(chatId)) {
			String[] queryArgs = new String[] { chatId };
			db.delete("tea_message", "teme_tech_id=?", queryArgs);
			db.delete("tea_teco_tech", "tett_tech_id=?", queryArgs);
			db.delete("tea_chatroom", "_id=?", queryArgs);
		}
		db.delete("tea_contact", "teco_jid=?", new String[] { jid });
		db.setTransactionSuccessful();
		db.endTransaction();

	}

	private void deleteChatRoom(String chatId) {
		if (StringUtils.isNullOrEmpty(chatId)) {
			Log.i(TAG, "chatId为空");
			return;
		}
		String[] queryArgs = new String[] { chatId };
		SQLiteDatabase db = teaDatabaseHelper.getWritableDatabase();
		db.beginTransaction();
		db.delete("tea_message", "teme_tech_id=?", queryArgs);
		db.delete("tea_teco_tech", "tett_tech_id=?", queryArgs);
		db.delete("tea_chatroom", "_id=?", queryArgs);
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	private int getUnReadMsgCount(String chatId) {
		if (StringUtils.isNullOrEmpty(chatId)) {
			Log.i(TAG, "chatId为空");
			return -1;
		}
		Log.i(TAG, "chatId:" + chatId);
		int result = -1;
		SQLiteDatabase db = teaDatabaseHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT count(*) FROM tea_message a"
				+ " WHERE a.[teme_is_read] = 0" + " AND a.[teme_tech_id] = ?",
				new String[] { chatId });
		if (cursor.moveToFirst())
			result = cursor.getInt(0);
		cursor.close();
		return result;
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
				
				Presence presence = roster.getPresence(e.getUser());
				if (presence.isAvailable()) {
					onlineCount++;
				}
				le.setOnline(presence.isAvailable());
				VCard vCard = getUserVCard(e.getName());
				le.setAvatar(vCard.getAvatar());
				item.add(le);
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

	private void makeToast(final String text, final int duration) {
		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), text, duration).show();
			}

		});
	}

	private void doConnect(String serviceName, String host, int port) {
		if (conn != null) {
			conn.disconnect();
		}
		XMPPTCPConnectionConfiguration conf = XMPPTCPConnectionConfiguration
				.builder().setSendPresence(false)
				.setSecurityMode(SecurityMode.disabled)
				.setServiceName(serviceName).setHost(host).setPort(port)
				.build();
		conn = new XMPPTCPConnection(conf);
		try {
			conn.connect();
			IS_CONNECT = true;
		} catch (SmackException e) {
			e.printStackTrace();
			Log.e(TAG, "登录错误");
			makeToast("登录错误", Toast.LENGTH_LONG);
			IS_LOGIN = false;
			IS_CONNECT = false;
			// respCode = 1;
		} catch (IOException e) {
			e.printStackTrace();
			IS_LOGIN = false;
			IS_CONNECT = false;
			// respCode = 2;
		} catch (XMPPException e) {
			e.printStackTrace();
			Log.e(TAG, "连接错误");
			makeToast("连接错误", Toast.LENGTH_LONG);
			IS_LOGIN = false;
			IS_CONNECT = false;
			// respCode = 3;
		}
	}

	private void doLogin(final String username, final String password) {
		Log.i(TAG, "start login");
		if (conn == null || !conn.isConnected()) {
			Log.e(TAG, "还没有连接服务器");
			doConnect(Config.XMPP_SERVICE_NAME, Config.XMPP_HOST,
					Config.XMPP_PORT);
			try {
				conn.login(username, password);
				IS_LOGIN = true;
			} catch (XMPPException e) {
				e.printStackTrace();
				IS_LOGIN = false;
			} catch (SmackException e) {
				e.printStackTrace();
				IS_LOGIN = false;
			} catch (IOException e) {
				e.printStackTrace();
				IS_LOGIN = false;
			}
			return;
		}
		// 已登陆
		if (conn.isAuthenticated()) {
			//
			if (conn.getUser().equals(username)) {
				Log.i(TAG, "已登陆");
				IS_LOGIN = true;
				return;
			} else {
				doConnect(conn.getServiceName(), conn.getHost(), conn.getPort());
				try {
					conn.login(username, password);
					IS_LOGIN = true;
				} catch (XMPPException e) {
					e.printStackTrace();
					IS_LOGIN = false;
				} catch (SmackException e) {
					e.printStackTrace();
					IS_LOGIN = false;
				} catch (IOException e) {
					e.printStackTrace();
					IS_LOGIN = false;
				}
			}
		} else {
			try {
				conn.login(username, password);
				IS_LOGIN = true;
			} catch (XMPPException e) {
				e.printStackTrace();
				IS_LOGIN = false;
			} catch (SmackException e) {
				e.printStackTrace();
				IS_LOGIN = false;
			} catch (IOException e) {
				e.printStackTrace();
				IS_LOGIN = false;
			}
		}
		int respCode = -1;
		if (IS_LOGIN) {
			Editor editor = userInfoSp.edit();
			editor.putString("username", username);
			editor.putString("password", password);
			editor.commit();
			initUserData(username);
			respCode = 1;
		} else
			respCode = 0;
		ResponseLoginEvent respLoginEvent = EBEvents
				.instanceReponseLoginEvent();
		respLoginEvent.setRespCode(respCode);
		EventBus.getDefault().post(respLoginEvent);
	}

	/**
	 * @Description 连接并登录服务器
	 * @param username
	 * @param password
	 */
	private void doConnectAndLogin(final String username, final String password) {
		doConnect(Config.XMPP_SERVICE_NAME, Config.XMPP_HOST, Config.XMPP_PORT);
		try {
			conn.login(username, password);
			IS_LOGIN = true;
		} catch (XMPPException e) {
			e.printStackTrace();
			IS_LOGIN = false;
		} catch (SmackException e) {
			e.printStackTrace();
			IS_LOGIN = false;
		} catch (IOException e) {
			e.printStackTrace();
			IS_LOGIN = false;
		}
		if (IS_LOGIN) {
			Editor editor = userInfoSp.edit();
			editor.putString("username", username);
			editor.putString("password", password);
			editor.commit();
			initUserData(username);
		}
	}

	private void doLogout() {
		if (conn == null || !conn.isConnected()) {
			return;
		} else
			conn.disconnect();
	}

	private void loadOfflineMsgs() {
		// 接收离线消息
		OfflineMessageManager offlineManager = new OfflineMessageManager(conn);
		List<Message> offlineMsgs = null;
		try {
			Log.i(TAG,
					"offline message count: "
							+ offlineManager.getMessageCount());
			offlineMsgs = offlineManager.getMessages();
			for (Message msg : offlineMsgs) {
				// Log.i(TAG, msg.getBody());
				String fromJid = msg.getFrom().split("/")[0];
				String fromId = getContactId(fromJid);
				if (fromId == null) {
					Log.i(TAG, "来自陌生人的消息");
					continue;
				}
				// offlineManager.
				MessageModel mm = new MessageModel();
				String chatId = isExistChatRoom(fromJid, String.valueOf(1));
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
		} catch (NoResponseException e1) {
			e1.printStackTrace();
		} catch (XMPPErrorException e1) {
			e1.printStackTrace();
		} catch (NotConnectedException e1) {
			e1.printStackTrace();
		}
	}

	private void addChatRoomListenrs() {
		cm = ChatManager.getInstanceFor(conn);
		cm.addChatListener(new ChatManagerListener() {

			@Override
			public void chatCreated(Chat chat, boolean isLocally) {
				Log.i(TAG, "createChat：" + isLocally);
				if (!isLocally) {
					Log.i(TAG,
							chat.getParticipant() + "\t" + chat.getThreadID());
					String username = chat.getParticipant().split("@")[0];
					if (getContactId(chat.getParticipant().split("/")[0]) == null) {
						Log.i(TAG, "陌生人来信:" + chat.getParticipant());
						chat.close();
						return;
					}
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
					public void processMessage(Chat chat, Message msg) {
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
						receiveChatMsgEvent.setChatMsgTransferEntity(cmte);
						EventBus.getDefault().post(receiveChatMsgEvent);

						// ....
						// 更新chatroom时间
						// ....
						// 保存未读消息
						String fromJid = msg.getFrom().split("/")[0];
						String fromId = getContactId(fromJid);
						MessageModel mm = new MessageModel();
						mm.chatId = isExistChatRoom(fromJid, String.valueOf(1));
						mm.content = msg.getBody();
						mm.time = getDatetime();
						mm.from = fromId;
						mm.type = String.valueOf(1);
						mm.isRead = false; // 默认消息未读
						if (insertMessage(mm)) {
							Log.i(TAG, "message insert success");
							// 向teaChatListFragment发送消息
							EBEvents.RefreshChatListEvent refreshChatListEvent = EBEvents
									.instanceRefreshChatListEvent();
							refreshChatListEvent
									.setChatListForms(getChatListForms());
							EventBus.getDefault().post(refreshChatListEvent);
						}
					}
				});
			}
		});
	}

	private void addRosterListener() {
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
				getAddressListEvent.setAddressGroupList(addressGroupList);
				getAddressListEvent.setContactList(contactList);
				EventBus.getDefault().post(getAddressListEvent);
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
				loadContact(roster);
				EBEvents.GetAddressListEvent getAddressListEvent = EBEvents
						.instanceGetAddressListEvent();
				getAddressListEvent.setAddressGroupList(addressGroupList);
				getAddressListEvent.setContactList(contactList);
				EventBus.getDefault().post(getAddressListEvent);
				List<ChatListFormEntity> chatlst = getChatListForms();
				EBEvents.RefreshChatListEvent refreshChatListEvent = EBEvents
						.instanceRefreshChatListEvent();
				refreshChatListEvent.setChatListForms(chatlst);
				EventBus.getDefault().post(refreshChatListEvent);
			}

			@Override
			public void entriesAdded(Collection<String> entries) {
				System.out.println("added====================");
				for (String e : entries) {
					System.out.println(e);
				}
				loadContact(roster);
				EBEvents.GetAddressListEvent getAddressListEvent = EBEvents
						.instanceGetAddressListEvent();
				getAddressListEvent.setAddressGroupList(addressGroupList);
				getAddressListEvent.setContactList(contactList);
				EventBus.getDefault().post(getAddressListEvent);
			}
		});
		loadContact(roster);
	}

	@SuppressLint("NewApi")
	private void initUserData(String username) {
		TeaChatActivity.ACCOUNT_NAME = username;
		String databaseName = "teaDatabase_" + username + ".db3";
		if (!teaDatabaseHelper.getDatabaseName().equals(databaseName)) {
			teaDatabaseHelper.close();
			teaDatabaseHelper = new TeaDatabaseHelper(XmppService.this,
					databaseName, 1);
		}
		Log.i(TAG, "数据库名称：" + teaDatabaseHelper.getDatabaseName());
		loadOfflineMsgs();
		// 保存用户
		ContactModel owner = new ContactModel();
		owner.nickname = conn.getUser().split("@")[0];
		owner.jid = conn.getUser().split("/")[0];
		if (saveContact(owner)) {
			Log.i(TAG, " user save success!");
		}
		addChatRoomListenrs();
		addRosterListener();
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
	}

	private void doRegister(String username, String password) {
		int respCode = -1;
		if (conn == null || !conn.isConnected()) {
			doConnect(Config.XMPP_SERVICE_NAME, Config.XMPP_HOST,
					Config.XMPP_PORT);
		}
		AccountManager am = AccountManager.getInstance(conn);
		try {
			am.createAccount(username, password);
			respCode = 1;
		} catch (NoResponseException e) {
			e.printStackTrace();
			respCode = 2;
		} catch (XMPPErrorException e) {
			e.printStackTrace();
			respCode = 0;
		} catch (NotConnectedException e) {
			e.printStackTrace();
			respCode = 3;
		}
		ResponseRegisterEvent respRegisterEvent = EBEvents
				.instanceResponseRegisterEvent();
		respRegisterEvent.setRespCode(respCode);
		EventBus.getDefault().post(respRegisterEvent);
	}

	/**
	 * @Description 获取VCard
	 * @param username
	 * @return
	 */
	private VCard getUserVCard(String username) {
		VCard vCard = null;
		String bareJid;
		if (conn == null || !conn.isConnected() || !conn.isAuthenticated()) {
			String name = userInfoSp.getString("username", "");
			String password = userInfoSp.getString("password", "");
			if (!name.isEmpty() && !password.isEmpty()) {
				doConnectAndLogin(name, password);
			}
		}
		if (IS_LOGIN) {
			VCardManager vCardManager = VCardManager.getInstanceFor(conn);
			try {
				bareJid = username + "@" + conn.getServiceName();
				vCard = vCardManager.loadVCard(bareJid);
			} catch (NoResponseException e) {
				e.printStackTrace();
			} catch (XMPPErrorException e) {
				e.printStackTrace();
			} catch (NotConnectedException e) {
				e.printStackTrace();
			}
		}

		return vCard;
	}

	/**
	 * @Description 保存VCard
	 * @param vCard
	 * @return
	 */
	private boolean saveUserVCard(VCard vCard) {
		boolean result = false;
		if (conn == null || !conn.isConnected() || !conn.isAuthenticated()) {
			String name = userInfoSp.getString("username", "");
			String password = userInfoSp.getString("password", "");
			if (!name.isEmpty() && !password.isEmpty()) {
				doConnectAndLogin(name, password);
			}
		}
		if (IS_LOGIN) {
			VCardManager vCardManager = VCardManager.getInstanceFor(conn);
			try {
				vCardManager.saveVCard(vCard);
				result = true;
			} catch (NoResponseException e) {
				e.printStackTrace();
				result = false;
			} catch (XMPPErrorException e) {
				e.printStackTrace();
				result = false;
			} catch (NotConnectedException e) {
				e.printStackTrace();
				result = false;
			}
		}
		return result;
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
