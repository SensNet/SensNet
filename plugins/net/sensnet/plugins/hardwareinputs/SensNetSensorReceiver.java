package net.sensnet.plugins.hardwareinputs;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sensnet.node.InvalidNodeAuthException;
import net.sensnet.node.SensNetNodeConfiguration;
import net.sensnet.node.dbobjects.DataPoint;
import net.sensnet.node.dbobjects.LocationLatLong;
import net.sensnet.node.dbobjects.Sensor;
import net.sensnet.node.plugins.HardwareInputPlugin;

public class SensNetSensorReceiver extends HardwareInputPlugin {
	public static final byte MAJOR_VERSION = 0x02;
	public static final byte MINOR_VERSION = 0x00;

	public SensNetSensorReceiver(SensNetNodeConfiguration configuration) {
		super(configuration);
	}

	@Override
	public void run() {
		File inter = new File(getProperty("serial"));
		if (!inter.exists()) {
			getLoger().warn("Serial interface not found!");
			return;
		}
		while (true) {
			try {
				System.out.println("p!");
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
						long timestamp2 = buffer.getInt(19) & 0xFFFFFFFFl;
						System.out.println(timestamp + "" + timestamp2);
						// 10:59:34 24.02.15
						SimpleDateFormat format = new SimpleDateFormat(
								"HHmmssddMMyy");
						format.setTimeZone(TimeZone.getTimeZone("GMT"));
						Date date = format.parse(ammenddateZero(timestamp + "",
								6) + ammenddateZero(timestamp2 + "", 6));
						System.out.println(date);
						byte ttl = buf[23];
						byte rssi = buf[24];
						int length = buf[25];
						if (length < 0) {
							length = 127 + (127 - (length * -1));
						}
						// FIXME: OMFG FUCKING HACK FIX THAT!
						byte[] datas = new byte[(length * 2) + 4];
						ByteBuffer lengbuf = ByteBuffer.allocate(4);
						lengbuf.putInt(length);
						datas[0] = lengbuf.get(0);
						datas[1] = lengbuf.get(1);
						datas[2] = lengbuf.get(2);
						datas[3] = lengbuf.get(3);

						for (int i = 0; i < (length * 2); i++) {
							datas[i + 4] = buf[26 + i];
						}
						byte[] tmp = new byte[256];
						try {
							if (date.getTime() > System.currentTimeMillis() + 1000 * 60 * 6) {
								Logger.getAnonymousLogger()
										.log(Level.WARNING,
												"Received timestamp from the future! Droped...");
							} else {
								DataPoint datapoint = new DataPoint(sensortype,
										Sensor.getBySensorUid(sensorid), datas,
										date.getTime() / 1000, battery,
										new LocationLatLong(lat, longitude),
										SensNetNodeConfiguration.getInstance()
												.getThisNode());
								datapoint.commit();
							}
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
				Thread.sleep(5000);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	private static String ammenddateZero(String in, int l) {
		String res = in;
		while (res.length() != l) {
			res = "0" + res;
		}
		return res;
	}

}
