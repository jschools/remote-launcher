package com.schoovello.remotelauncher.lib.json;

public class AppLaunchInfo {

	private String name;
	private String packageName;
	private String activityName;
	private String iconBase64;

	public AppLaunchInfo() {
		// constructor for Gson
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getIconBase64() {
		return iconBase64;
	}

	public void setIconBase64(String iconBase64) {
		this.iconBase64 = iconBase64;
	}

}
