package net.sensnet.node.natives;

import java.io.File;
import java.util.Date;

public class SystemTime {
	private static boolean loaded = false;

	static {
		if (System.getProperty("os.name").toLowerCase().contains("linux")) {
			System.load(new File("native/linux/libsystemtime.so")
					.getAbsolutePath());
			loaded = true;
		}
	}

	public static void setSystemTime(Date d) {
		setSystemTime(d.getTime());
	}

	public static void setSystemTime(long millis) {
		if (loaded) {
			setSystemTime0((int) (millis / 1000));
		} else {
			System.err.println("Settime ignored; not compatible.");
		}
	}

	private native static void setSystemTime0(int time);
}
