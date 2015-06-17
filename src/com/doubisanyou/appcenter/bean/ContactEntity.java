package com.doubisanyou.appcenter.bean;

public class ContactEntity {
	private String name;
	private String gxqm;
	private boolean isOnline;
	private byte[] avatar;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGxqm() {
		return gxqm;
	}

	public void setGxqm(String gxqm) {
		this.gxqm = gxqm;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

}
