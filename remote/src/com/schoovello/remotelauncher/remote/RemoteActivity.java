package com.schoovello.remotelauncher.remote;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.schoovello.remotelauncher.lib.json.AppLaunchInfo;

public class RemoteActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		findViewById(R.id.btn_search).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startSearch();
			}
		});
	}

	private void startSearch() {

	}

	private class SearchTask extends AsyncTask<Void, Void, List<LaunchableActivity>> {
		@Override
		protected List<LaunchableActivity> doInBackground(Void... params) {
			List<LaunchableActivity> result = new ArrayList<LaunchableActivity>();


			return result;
		}
	}

	private static class LaunchableActivity {
		public final AppLaunchInfo appLaunchInfo;
		public final Bitmap iconBitmap;
		
		public LaunchableActivity(AppLaunchInfo appLaunchInfo, Bitmap iconBitmap) {
			this.appLaunchInfo = appLaunchInfo;
			this.iconBitmap = iconBitmap;
		}
	}
}
