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
import net.sensnet.node.dbobjects.SensorType;

public class SensorReceiver implements Runnable {
	private File inter;
	public static final byte MAJOR_VERSION = 0x01;
	public static final byte MINOR_VERSION = 0x01;

	public static void main(String[] args) throws UnknownHostException,
			IOException {
		new Thread(new SensorReceiver(DummySender.TTY)).start();
		DummySender.main(new String[0]);
	}

	public SensorReceiver(String interfa) {
		this.inter = new File(interfa);
	}

	@Override
	public void run() {
		try {
			while (true) {
				InputStream r = new FileInputStream(inter);
				byte[] buf = new byte[255];
				while (r.read(buf, 0, 255) != -1) {
					ByteBuffer buffer = ByteBuffer.wrap(buf);
					byte major = buf[0];
					byte minor = buf[1];
					if (major == MAJOR_VERSION) {
						int lat = buffer.getInt(5);
						int longitude = buffer.getInt(9);
						byte sensortype = buf[2];
						int sensorid = buf[3];
						sensorid = sensorid & 0xFF;
						sensorid = sensorid << 8;
						sensorid = sensorid | (buf[4] & 0xff);
						byte rawbat = buf[13];
						int battery = (int) ((rawbat / 255f) * 100f);
						long timestamp = buffer.getInt(14) & 0xFFFFFFFFl;
						timestamp *= 1000;
						byte ttl = buf[18];
						float data1 = buffer.getFloat(19);
						float data2 = buffer.getFloat(23);
						try {
							DataPoint datapoint = new DataPoint(
									SensorType.getById(sensortype),
									Sensor.getBySensorUid(sensorid), data1,
									data2, timestamp, battery,
									new LocationLatLong(lat, longitude),
									SensNetNodeConfiguration.getInstance()
											.getThisNode());
							datapoint.commit();
						} catch (SQLException | InvalidNodeAuthException e) {
							e.printStackTrace();
						}
						break;
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
