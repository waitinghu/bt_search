package com.seuic.btsearch;

public class BTInfo {
	private String mName;
	private String mMacAddress;
	
	public BTInfo(String name,String macAddress) {
		this.mName = name;
		this.mMacAddress = macAddress;
	}
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		this.mName = name;
	}
	public String getMacAdreess() {
		return mMacAddress;
	}
	public void setMacAdreess(String macAddress) {
		this.mMacAddress = macAddress;
	}
}
