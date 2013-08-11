package com.schoovello.remotelauncher.host;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import android.os.IBinder;
import android.util.Base64;
import android.util.Base64OutputStream;

import com.google.gson.Gson;
import com.schoovello.remotelauncher.lib.Id;
import com.schoovello.remotelauncher.lib.json.AppLaunchInfo;
import com.schoovello.remotelauncher.lib.json.ListResponse;
import com.schoovello.remotelauncher.lib.json.Request;
import com.schoovello.remotelauncher.lib.json.Request.IconDensity;
import com.schoovello.remotelauncher.lib.json.Response;
import com.schoovello.remotelauncher.lib.util.IoUtils;

public class MainService extends Service {

	private LocalBinder mBinder = new LocalBinder();
	private BluetoothAdapter mBluetoothAdapter;

	public static void startMonitoring(Context context) {
		Intent intent = new Intent(context, MainService.class);
		context.startService(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		new AcceptThread().start();

		return START_STICKY;
	}

	private void serveSocket(BluetoothSocket socket) throws IOException {
		GZIPInputStream in = new GZIPInputStream(socket.getInputStream(), 1024);
		InputStreamReader reader = new InputStreamReader(in, "UTF-8");
		Request request = new Gson().fromJson(reader, Request.class);
		reader.close();

		Response response = getResponse(request);
		OutputStream out = new GZIPOutputStream(socket.getOutputStream(), 1024);
		out.write(new Gson().toJson(response).getBytes());
		out.flush();
		out.close();
	}

	private Response getResponse(Request request) throws IOException {
		switch (request.getRequestType()) {
		case LIST:
			return getListResponse(request);
		case LAUNCH:
			return null;
		default:
			return null;
		}
	}

	private Response getListResponse(Request request) throws IOException {
		IconDensity density = IconDensity.parse(request.getParams().get(Request.Json.PARAM_ICON_DENSITY));

		Intent launchIntent = new Intent(Intent.ACTION_MAIN);
		launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		PackageManager pm = getPackageManager();
		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(launchIntent, 0);
		for (ResolveInfo resolveInfo : resolveInfos) {
			AppLaunchInfo appInfo = new AppLaunchInfo();

			ActivityInfo info = resolveInfo.activityInfo;
			appInfo.setName(info.loadLabel(pm).toString());
			appInfo.setPackageName(info.packageName);
			appInfo.setActivityName(info.name);

			InputStream iconInputStream = new BufferedInputStream(getResources().openRawResource(info.getIconResource()));
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream(1024);
			Base64OutputStream os = new Base64OutputStream(byteStream, Base64.NO_WRAP);
			IoUtils.copyStream(iconInputStream, os);
			iconInputStream.close();

			appInfo.setActivityName(byteStream.toString("UTF-8"));
			os.close();
		}

		List<AppLaunchInfo> apps = new ArrayList<AppLaunchInfo>();

		ListResponse response = new ListResponse();

		return response;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private class LocalBinder extends Binder {
		MainService getService() {
			return MainService.this;
		}
	}

	private class AcceptThread extends Thread {
		private final BluetoothServerSocket mmServerSocket;

		public AcceptThread() {
			BluetoothServerSocket tmp = null;
			try {
				tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("RemoteLauncher", Id.BT_UUID);
			}
			catch (IOException e) {
			}
			mmServerSocket = tmp;
		}

		public void run() {
			BluetoothSocket socket = null;
			while (true) {
				try {
					socket = mmServerSocket.accept();
				}
				catch (IOException e) {
					break;
				}
				if (socket != null) {
					try {
						mmServerSocket.close();

						serveSocket(socket);
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
