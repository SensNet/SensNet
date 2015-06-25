package net.sensnet.plugins.hardwareinputs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import net.sensnet.node.SensNetNodeConfiguration;
import net.sensnet.node.dbobjects.DataPoint;
import net.sensnet.node.dbobjects.LocationLatLong;
import net.sensnet.node.dbobjects.Sensor;
import net.sensnet.node.natives.SerialPort;
import net.sensnet.node.plugins.HardwareInputPlugin;

public class SensNetSensorReceiver extends HardwareInputPlugin {
	public static final byte MAJOR_VERSION = 4;
	public static final byte MINOR_VERSION = 0x00;
	public static final char[] PARSE_WORD = "p!hup".toCharArray();
	public int packageCount = 0;
	public int damagedPackages = 0;
	public float avgPackageLoss = 0;

	private static final SimpleDateFormat PRIMITIVE = new SimpleDateFormat(
			"HHmmssddMMyy");
	static {
		PRIMITIVE.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public SensNetSensorReceiver(SensNetNodeConfiguration configuration) {
		super(configuration);
		SerialPort.setBaudRate(getProperty("serial"), 9600);
	}

	private File initializeHardware() {
		File inter = new File(getProperty("serial"));
		if (!inter.exists()) {
			getLoger().warn("Serial interface not found!");
			return null;
		}
		System.out.println("Initializing shield....");
		try (PrintWriter wr = new PrintWriter(new FileWriter(inter), true)) {
			try (BufferedReader read = new BufferedReader(new FileReader(inter))) {
				wr.println("AT+RST");
				if (read.readLine().equals("OK")) {
					System.out.println("Basic initialization.");
					wr.println("AT+PARS=" + new String(PARSE_WORD));
					if (read.readLine().equals("OK")) {
						wr.println("AT+INIT");
						wr.close();
						if (read.readLine().equals("OK")) {
							System.out.println("Shield initialzied.");
							read.close();
							return inter;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void run() {
		File inter = null;
		if ((inter = initializeHardware()) == null) {
			return;
		}
		while (true) {
			try {
				FileInputStream inStream = new FileInputStream(inter);
				int readed;
				ByteBuffer byteBuf = ByteBuffer.allocate(512);
				int succIndex = 0;
				while ((readed = inStream.read()) != -1) {
					if (readed == PARSE_WORD[succIndex]) {
						succIndex++;
						if (succIndex == 5) {
							if (packageCount == Integer.MAX_VALUE) {
								packageCount = 0;
								damagedPackages = 0;
							}
							packageCount++;
							System.out.println("DATA!");
							succIndex = 0;
							short[] header = new short[28];
							for (int i = 0; i < 28; i++) {
								header[i] = (short) inStream.read();
							}
							short major = header[1];
							short minor = header[2];
							if (header[0] == 0 && major == MAJOR_VERSION) {
								short sensorClass = header[5];
								short sensorType = header[6];
								short datalength = header[27];
								short[] data = new short[datalength];
								int sensorId = header[3];
								sensorId = sensorId << 8;
								sensorId += header[4];
								for (int i = 0; i < data.length; i++) {
									data[i] = (short) inStream.read();
								}
								long parseTime = readUnsignedInt(17, header);
								long parseDate = readUnsignedInt(21, header);
								short battery = header[16];
								battery = (short) ((battery / 255f) * 100);
								System.out.println(battery);
								byte temperature = (byte) header[15];
								int latitude = readSignedInt(7, header);
								int longitude = readSignedInt(11, header);
								LocationLatLong location = new LocationLatLong(
										latitude, longitude);
								String time = ammenddateZero(parseTime + "", 6);
								Date measurementTime;
								System.out.println(time);
								if (time.equals("250000")) {
									measurementTime = new Date();
									System.out.println("St date myself.");
								} else {
									measurementTime = PRIMITIVE
											.parse(ammenddateZero(parseTime
													+ "", 6)
													+ ammenddateZero(parseDate
															+ "", 6));
								}
								if (measurementTime.getTime() < 0) {
									System.out.println("Invalid time. Droped.");
								}
								System.out.println(measurementTime);
								DataPoint dp = new DataPoint(sensorType,
										sensorClass,
										Sensor.getBySensorUid(sensorId), data,
										measurementTime, battery, location,
										temperature, SensNetNodeConfiguration
												.getInstance().getThisNode());
								dp.commit();
							} else {
								if (header[0] == 1) {
									System.out.println("Damaged package!");
									damagedPackages++;
									avgPackageLoss = ((float) damagedPackages / packageCount) * 100f;
									System.out.println("Avg. Package loss: "
											+ avgPackageLoss + "%");
								} else {
									System.out
											.println("Received unsupported/unknown package version/format.");
								}
								byteBuf.clear();
							}
						}
					} else {
						succIndex = 0;
					}
					if (readed == '\n') {
						char[] arr = new char[byteBuf.position() / 2];
						byteBuf.position(0);
						for (int i = 0; i < arr.length; i++) {
							arr[i] = byteBuf.getChar();
						}
						String line = new String(arr);
						// TODO GPS
						byteBuf.clear();
					} else {
						byteBuf.putShort((short) readed);
					}
				}
				inStream.close();
			} catch (IOException e) {
				getLoger().warn("Interface I/O error! Retying in 15 secounds.",
						e);
				try {
					Thread.sleep(15000);
				} catch (InterruptedException e1) {
				}
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

	private long readUnsignedInt(int offset, short[] values) {
		long res = values[offset];
		for (int i = 1; i < 4; i++) {
			res <<= 8;
			res += values[offset + i];
		}
		return res;
	}

	private int readSignedInt(int offset, short[] values) {
		int res = values[offset];
		for (int i = 1; i < 4; i++) {
			res <<= 8;
			res += values[offset + i];
		}
		return res;
	}

	public float getAvgPackageLoss() {
		return avgPackageLoss;
	}

}
