package net.sensnet.node.sensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;

import net.sensnet.node.SensNetNodeConfiguration;
import net.sensnet.node.dbobjects.DataPoint;
import net.sensnet.node.dbobjects.LocationLatLong;
import net.sensnet.node.dbobjects.Sensor;

public class BluetoothSensorReceiver implements Runnable {
	public static void main(String[] args) throws IOException {
		new Thread(new BluetoothSensorReceiver()).start();
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	@Override
	public void run() {
		Process process;
		try {
			process = Runtime.getRuntime().exec(
					"gatttool -b 20:CD:39:B0:76:AA -I");
			Reader read = new InputStreamReader(process.getInputStream());
			PrintWriter wr = new PrintWriter(process.getOutputStream());
			boolean connected = false;
			char[] buf = new char[1];
			while ((read.read(buf, 0, 1)) != -1) {
				if (buf[0] == '>' && !connected) {
					wr.println("connect");
					wr.flush();
					connected = true;
					break;
				}
			}
			BufferedReader reader = new BufferedReader(read);
			String lineRead;
			String nextPart = null;
			while ((lineRead = reader.readLine()) != null) {
				try {
					if (lineRead.contains("value:")) {
						String data = lineRead.split(": ")[1];
						data = data.replace(" ", "");
						data = data.trim();
						byte[] rawdata = hexStringToByteArray(data);
						data = new String(hexStringToByteArray(data)).trim();
						if (data.startsWith("$")) {
							continue;
						}
						String[] dataParts = data.split(";");
						DataPoint point = new DataPoint(1, new Sensor(0x20CD,
								SensNetNodeConfiguration.getInstance()
										.getThisNode()), rawdata,
								System.currentTimeMillis() / 1000,
								Integer.parseInt(dataParts[2]) / 10,
								new LocationLatLong(490208826, 83705708),
								SensNetNodeConfiguration.getInstance()
										.getThisNode());
						point.commit();
						System.out.println(data);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
}
