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

	public static void setBaudRate(String port, int baud) {
		if (loaded) {
			System.out.println("Baud set for " + port + " retuned: "
					+ setBaudRate0(port.trim(), baud));
		} else {
			System.err.println("Call to baudrate set not funtional!");
		}
	}

	private native static int setBaudRate0(String port, int baud);
}
