package com.schoovello.remotelauncher.lib.json;

import java.util.List;

public class ListResponse extends Response {

	private List<AppLaunchInfo> apps;

	public ListResponse() {
		// constructor for Gson
	}

	public List<AppLaunchInfo> getApps() {
		return apps;
	}

	public void setApps(List<AppLaunchInfo> apps) {
		this.apps = apps;
	}

}
