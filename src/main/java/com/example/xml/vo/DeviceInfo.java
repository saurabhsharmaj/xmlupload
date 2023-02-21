package com.example.xml.vo;


public class DeviceInfo {
	ScreenInfo ScreenInfoObject;
	OsInfo OsInfoObject;
	AppInfo AppInfoObject;
	private String _name;
	private String _id;

	// Getter Methods

	public ScreenInfo getScreenInfo() {
		return ScreenInfoObject;
	}

	public OsInfo getOsInfo() {
		return OsInfoObject;
	}

	public AppInfo getAppInfo() {
		return AppInfoObject;
	}

	public String get_name() {
		return _name;
	}

	public String get_id() {
		return _id;
	}

	// Setter Methods

	public void setScreenInfo(ScreenInfo screenInfoObject) {
		this.ScreenInfoObject = screenInfoObject;
	}

	public void setOsInfo(OsInfo osInfoObject) {
		this.OsInfoObject = osInfoObject;
	}

	public void setAppInfo(AppInfo appInfoObject) {
		this.AppInfoObject = appInfoObject;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	public void set_id(String _id) {
		this._id = _id;
	}
}