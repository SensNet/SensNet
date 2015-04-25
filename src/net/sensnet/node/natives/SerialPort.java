package net.sensnet.node.natives;

import java.io.File;

public class SerialPort {
	private static boolean loaded = false;

	static {
		if (System.getProperty("os.name").toLowerCase().contains("linux")) {
			System.load(new File("native/linux/libserialport.so")
					.getAbsolutePath());
			loaded = true;
		} else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			System.load(new File("native/darwin/libserialport.so")
					.getAbsolutePath());
			loaded = true;
		}
	}

	public static void setBoudRate(String port, int boud) {
		if (loaded) {
			System.out.println("Set boudrate of '" + port + "' to " + boud);
			setBoudRate0(port, boud);
		}
	}

	private native static void setBoudRate0(String port, int boud);
}
