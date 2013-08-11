package com.schoovello.remotelauncher.lib.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class IoUtils {
	private IoUtils() {
		// do not instantiate
	}

	public static void copyStream(InputStream in, OutputStream out) throws IOException {
		byte[] buff = new byte[1024];

		int readLength = 0;
		while ((readLength = in.read(buff)) != -1) {
			if (readLength > 0) {
				out.write(buff, 0, readLength);
			}
		}

		out.flush();
	}
}
