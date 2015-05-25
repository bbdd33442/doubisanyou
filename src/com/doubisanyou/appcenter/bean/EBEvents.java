package com.doubisanyou.appcenter.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Blook
 * @Description eventBus事件
 */
public class EBEvents {
	public static GetAddressListEvent instanceGetAddressListEvent() {
		EBEvents events = new EBEvents();
		return events.new GetAddressListEvent();
	}

	public static SendChatMsgEvent instanceSendChatMsgEvent() {
		EBEvents events = new EBEvents();
		return events.new SendChatMsgEvent();
	}

	public static ReceiveChatMsgEvent instanceReceiveChatMsgEvent() {
		EBEvents events = new EBEvents();
		return events.new ReceiveChatMsgEvent();
	}

	public static RefreshChatListEvent instanceRefreshChatListEvent() {
		EBEvents events = new EBEvents();
		return events.new RefreshChatListEvent();
	}

	public static GetChatListEvent instanceGetChatListEvent() {
		EBEvents events = new EBEvents();
		return events.new GetChatListEvent();
	}

	public static GetChatHistoryEvent instanceGetChatHistoryEvent() {
		EBEvents events = new EBEvents();
		return events.new GetChatHistoryEvent();
	}

	public static RefreshChatHistoryEvent instanceRefreshChatHistoryEvent() {
		EBEvents events = new EBEvents();
		return events.new RefreshChatHistoryEvent();
	}

	public static AddFriendEvent instanceAddFriendEvent() {
		EBEvents events = new EBEvents();
		return events.new AddFriendEvent();
	}

	public static DeleteFriendEvent instanceDeleteFriendEvent() {
		EBEvents events = new EBEvents();
		return events.new DeleteFriendEvent();
	}

	/**
	 * @author Blook
	 * @Description 获取通信录
	 */
	public class GetAddressListEvent {
		private ArrayList<AddressGroupEntity> addressGroupList;
		private ArrayList<ArrayList<ContactEntity>> contactList;

		public ArrayList<AddressGroupEntity> getAddressGroupList() {
			return addressGroupList;
		}

		public void setAddressGroupList(
				ArrayList<AddressGroupEntity> addressGroupList) {
			this.addressGroupList = addressGroupList;
		}

		public ArrayList<ArrayList<ContactEntity>> getContactList() {
			return contactList;
		}

		public void setContactList(
				ArrayList<ArrayList<ContactEntity>> contactList) {
			this.contactList = contactList;
		}

	}

	/**
	 * @author Blook
	 * @Description 发送消息事件
	 */
	public class SendChatMsgEvent {
		private ChatMsgTransferEntity chatMsgTransferEntity;

		public ChatMsgTransferEntity getChatMsgTransferEntity() {
			return chatMsgTransferEntity;
		}

		public void setChatMsgTransferEntity(
				ChatMsgTransferEntity chatMsgTransferEntity) {
			this.chatMsgTransferEntity = chatMsgTransferEntity;
		}

	}

	/**
	 * @author Blook
	 * @Description 接收消息事件
	 */
	public class ReceiveChatMsgEvent {
		private ChatMsgTransferEntity chatMsgTransferEntity;

		public ChatMsgTransferEntity getChatMsgTransferEntity() {
			return chatMsgTransferEntity;
		}

		public void setChatMsgTransferEntity(
				ChatMsgTransferEntity chatMsgTransferEntity) {
			this.chatMsgTransferEntity = chatMsgTransferEntity;
		}

	}

	/**
	 * @author Blook
	 * @Description 刷新聊天列表事件
	 */
	public class RefreshChatListEvent {
		private List<ChatListFormEntity> chatListForms;

		public List<ChatListFormEntity> getChatListForms() {
			return chatListForms;
		}

		public void setChatListForms(List<ChatListFormEntity> chatListForms) {
			this.chatListForms = chatListForms;
		}

	}

	public class GetChatListEvent {
	}

	public class GetChatHistoryEvent {
		private String chatId;

		public String getChatId() {
			return chatId;
		}

		public void setChatId(String chatId) {
			this.chatId = chatId;
		}

	}

	public class RefreshChatHistoryEvent {
		private List<ChatMsgViewEntity> chatMsgViews;

		public List<ChatMsgViewEntity> getChatMsgViews() {
			return chatMsgViews;
		}

		public void setChatMsgViews(List<ChatMsgViewEntity> chatMsgViews) {
			this.chatMsgViews = chatMsgViews;
		}
	}

	/**
	 * @author Blook
	 * @Description 添加好友事件
	 */
	public class AddFriendEvent {
		private String jid;

		public String getJid() {
			return jid;
		}

		public void setJid(String jid) {
			this.jid = jid;
		}

	}

	/**
	 * @author Blook
	 * @Description 删除好友事件
	 */
	public class DeleteFriendEvent {
		private String jid;

		public String getJid() {
			return jid;
		}

		public void setJid(String jid) {
			this.jid = jid;
		}
	}
}
