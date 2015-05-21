package com.doubisanyou.appcenter.bean;

public class ChatListFormEntity {
	private String jid;
	private String chatId;
	private int avator;
	private String shortMsg;
	private String updateTime;

	public String getJid() {
		return jid;
	}

	public void setJid(String jid) {
		this.jid = jid;
	}

	public int getAvator() {
		return avator;
	}

	public void setAvator(int avator) {
		this.avator = avator;
	}

	public String getShortMsg() {
		return shortMsg;
	}

	public void setShortMsg(String shortMsg) {
		this.shortMsg = shortMsg;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getChatId() {
		return chatId;
	}

	public void setChatId(String chatId) {
		this.chatId = chatId;
	}

}
