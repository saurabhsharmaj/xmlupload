package com.example.xml.vo;

public class EpaperRequest {
	DeviceInfo DeviceInfoObject;
	GetPages GetPagesObject;

	// Getter Methods

	public DeviceInfo getDeviceInfo() {
		return DeviceInfoObject;
	}

	public GetPages getGetPages() {
		return GetPagesObject;
	}

	// Setter Methods

	public void setDeviceInfo(DeviceInfo deviceInfoObject) {
		this.DeviceInfoObject = deviceInfoObject;
	}

	public void setGetPages(GetPages getPagesObject) {
		this.GetPagesObject = getPagesObject;
	}
}