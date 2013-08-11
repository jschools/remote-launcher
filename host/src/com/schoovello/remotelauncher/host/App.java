package com.schoovello.remotelauncher.host;

import android.app.Application;
import android.content.Context;

public class App extends Application {

	private static App sInstance;

	public App() {
		sInstance = this;
	}

	public static Context getContext() {
		return sInstance;
	}
}
