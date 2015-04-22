package net.sensnet.node.natives;

import java.io.File;

public class SerialPort {
	static {
		if (System.getProperty("os.name").toLowerCase().contains("linux")) {
			System.load(new File("native/linux/libserialport.so")
					.getAbsolutePath());
		}
	}

	public static void setBoudRate(String port, int boud) {
		if (System.getProperty("os.name").toLowerCase().contains("linux")) {
			setBoudRate0(port, boud);
		}
	}

	private native static void setBoudRate0(String port, int boud);
}
