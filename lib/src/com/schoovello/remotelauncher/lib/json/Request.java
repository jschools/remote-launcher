package com.schoovello.remotelauncher.lib.json;

import java.util.Map;

public class Request {

	private RequestType requestType;
	private Map<String, String> params;

	public Request() {
		// constructor for Gson
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public static enum RequestType {
		LIST, LAUNCH
	}

	public static enum IconDensity {
		MDPI, HDPI, XHDPI;

		public static IconDensity parse(String string) {
			try {
				return valueOf(string);
			}
			catch (Exception e) {
				return null;
			}
		}
	}

	public static interface Json {
		public static final String PARAM_ICON_DENSITY = "iconDensity";
		public static final String PARAM_URI = "uri";
	}
}
