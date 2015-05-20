package com.doubisanyou.appcenter.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Blook
 * @Description 聊天消息广播传递类
 */
public class ChatMsgTransferEntity implements Parcelable {
	private String from;
	private String to;
	private String content;
	private int type;

	public ChatMsgTransferEntity() {
	}

	public ChatMsgTransferEntity(String from, String to, String content,
			int type) {
		this.from = from;
		this.to = to;
		this.content = content;
		this.type = type;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(from);
		parcel.writeString(to);
		parcel.writeString(content);
		parcel.writeInt(type);
	}

	public static final Parcelable.Creator<ChatMsgTransferEntity> CREATOR = new Parcelable.Creator<ChatMsgTransferEntity>() {

		@Override
		public ChatMsgTransferEntity createFromParcel(Parcel parcel) {
			String from = parcel.readString();
			String to = parcel.readString();
			String content = parcel.readString();
			int type = parcel.readInt();
			return new ChatMsgTransferEntity(from, to, content, type);
		}

		@Override
		public ChatMsgTransferEntity[] newArray(int size) {
			return new ChatMsgTransferEntity[size];
		}

	};
}
