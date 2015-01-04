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
			byte[] buf = new byte[27];
			// version
			buf[0] = SensorReceiver.MAJOR_VERSION;
			buf[1] = SensorReceiver.MINOR_VERSION;
			// sensortype
			buf[2] = (byte) 0x01;
			// id
			buf[3] = (byte) 0x81;
			buf[4] = (byte) 0x81;
			// lat
			int lat = 488109700;
			byte[] latar = ByteBuffer.allocate(4).putInt(lat).array();
			buf[5] = latar[0];
			buf[6] = latar[1];
			buf[7] = latar[2];
			buf[8] = latar[3];
			// long
			int longi = 91442900;
			byte[] longar = ByteBuffer.allocate(4).putInt(longi).array();
			buf[9] = longar[0];
			buf[10] = longar[1];
			buf[11] = longar[2];
			buf[12] = longar[3];
			// battery (50%)
			buf[13] = (byte) 0x7E;
			// time
			long time = System.currentTimeMillis() / 1000;
			byte[] timear = ByteBuffer.allocate(8).putLong(time).array();
			buf[14] = timear[4];
			buf[15] = timear[5];
			buf[16] = timear[6];
			buf[17] = timear[7];
			// ttl
			buf[18] = 0x7c;
			// some data
			byte dataAr[] = ByteBuffer.allocate(4).putFloat(14.2508674f).array();
			buf[19] = dataAr[0];
			buf[20] = dataAr[1];
			buf[21] = dataAr[2];
			buf[22] = dataAr[3];
			dataAr = ByteBuffer.allocate(4).putFloat(13.64891f).array();
			buf[23] = dataAr[0];
			buf[24] = dataAr[1];
			buf[25] = dataAr[2];
			buf[26] = dataAr[3];
			out.write(buf);
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
