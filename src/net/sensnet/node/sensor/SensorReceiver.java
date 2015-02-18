package net.sensnet.node.sensor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sensnet.node.InvalidNodeAuthException;
import net.sensnet.node.SensNetNodeConfiguration;
import net.sensnet.node.dbobjects.DataPoint;
import net.sensnet.node.dbobjects.LocationLatLong;
import net.sensnet.node.dbobjects.Sensor;

public class SensorReceiver implements Runnable {
	private File inter;
	public static final byte MAJOR_VERSION = 0x02;
	public static final byte MINOR_VERSION = 0x00;

	public static void main(String[] args) throws UnknownHostException,
			IOException, InterruptedException {
		new Thread(new SensorReceiver(DummySender.TTY)).start();
		// DummySender.main(new String[0]);
	}

	public SensorReceiver(String interfa) {
		this.inter = new File(interfa);
	}

	@Override
	public void run() {
		try {
			while (true) {
				InputStream r = new FileInputStream(inter);
				byte[] buf = new byte[256];
				while (r.read(buf, 0, 256) != -1) {
					ByteBuffer buffer = ByteBuffer.wrap(buf);
					if (buf[0] == 0) {
						Logger.getAnonymousLogger().log(Level.WARNING,
								"Received damaged package.");
						continue;
					}
					byte major = buf[1];
					byte minor = buf[2];
					if (major == MAJOR_VERSION) {
						int lat = buffer.getInt(6);
						int longitude = buffer.getInt(10);
						byte sensortype = buf[5];
						int sensorid = buf[3];
						sensorid = sensorid & 0xFF;
						sensorid = sensorid << 8;
						sensorid = sensorid | (buf[5] & 0xff);
						byte rawbat = buf[14];
						int battery = rawbat;
						if (battery < 0) {
							battery = 127 + (127 - (battery * -1));
						}
						battery = (int) ((battery / 255f) * 100);
						long timestamp = buffer.getInt(15) & 0xFFFFFFFFl;
						byte ttl = buf[19];
						byte rssi = buf[20];
						byte[] datas = new byte[buf[21]];
						for (int i = 0; i < buf[21]; i++) {
							datas[i] = buf[22 + i];
						}
						try {
							DataPoint datapoint = new DataPoint(sensortype,
									Sensor.getBySensorUid(sensorid), datas,
									timestamp, battery, new LocationLatLong(
											lat, longitude),
									SensNetNodeConfiguration.getInstance()
											.getThisNode());
							datapoint.commit();
						} catch (SQLException | InvalidNodeAuthException e) {
							e.printStackTrace();
						}
					} else {
						Logger.getAnonymousLogger().log(
								Level.WARNING,
								"Received breaking packet version: " + major
										+ "." + minor);
					}
				}
				r.close();
				Logger.getAnonymousLogger()
						.log(Level.WARNING,
								"Receiver shutdown: Received EOF on interface. Retrying in 5 secounds.");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
