package net.sensnet.node.sensor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class DummySender {
	public static final String TTY = "/Users/janis/jufo";

	public static void main(String[] args) throws UnknownHostException,
			IOException {
		try {
			FileOutputStream out = new FileOutputStream(TTY);
			byte[] buf = new byte[512];
			int offset = 0;
			// version
			buf[offset++] = SensorReceiver.MAJOR_VERSION;
			buf[offset++] = SensorReceiver.MINOR_VERSION;
			// id
			buf[offset++] = (byte) 0x81;
			buf[offset++] = (byte) 0x81;
			// sensortype
			buf[offset++] = (byte) 0x01;
			// lat
			int lat = 488109700;
			byte[] latar = ByteBuffer.allocate(4).putInt(lat).array();
			buf[offset++] = latar[0];
			buf[offset++] = latar[1];
			buf[offset++] = latar[2];
			buf[offset++] = latar[3];
			// long
			int longi = 91442900;
			byte[] longar = ByteBuffer.allocate(4).putInt(longi).array();
			buf[offset++] = longar[0];
			buf[offset++] = longar[1];
			buf[offset++] = longar[2];
			buf[offset++] = longar[3];
			// battery (50%)
			buf[offset++] = (byte) 0x7E;
			// time
			long time = System.currentTimeMillis() / 1000;
			byte[] timear = ByteBuffer.allocate(8).putLong(time).array();
			buf[offset++] = timear[4];
			buf[offset++] = timear[5];
			buf[offset++] = timear[6];
			buf[offset++] = timear[7];
			// ttl
			buf[offset++] = 0x7c;
			// rssi
			buf[offset++] = (byte) 255;
			// length
			buf[offset++] = (byte) 8;
			// some data
			byte dataAr[] = ByteBuffer.allocate(4).putFloat(14.2508674f).array();
			buf[offset++] = dataAr[0];
			buf[offset++] = dataAr[1];
			buf[offset++] = dataAr[2];
			buf[offset++] = dataAr[3];
			dataAr = ByteBuffer.allocate(4).putFloat(13.64891f).array();
			buf[offset++] = dataAr[0];
			buf[offset++] = dataAr[1];
			buf[offset++] = dataAr[2];
			buf[offset++] = dataAr[3];
			out.write(buf);
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
