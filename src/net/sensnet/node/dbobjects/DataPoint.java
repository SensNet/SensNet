package net.sensnet.node.dbobjects;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.ExceptionRunnable;
import net.sensnet.node.IndexizerHolder;
import net.sensnet.node.InvalidNodeAuthException;
import net.sensnet.node.SensNetNodeConfiguration;
import net.sensnet.node.SuperCommunicationsManager;
import net.sensnet.node.pages.DataPointSubmitPage;
import net.sensnet.node.plugins.SensorIndexizer;
import net.sensnet.node.util.ConnUtils;

public class DataPoint implements Syncable {
	private int type;
	private Sensor from;
	private LocationLatLong location;
	private long time;
	private int battery;
	private Node receiverNode;
	private short temperature;
	private byte[] values;

	public DataPoint(int type, Sensor from, byte[] values, long time,
			int battery, LocationLatLong location, short temperature,
			Node receiverNode) {
		this.type = type;
		this.from = from;
		this.values = values;
		this.time = time;
		this.temperature = temperature;
		this.battery = battery;
		this.location = location;
		this.receiverNode = receiverNode;
	}

	public DataPoint(HttpServletRequest req) throws NumberFormatException,
			SQLException, IOException, InvalidNodeAuthException {
		this.type = Integer.parseInt(req.getParameter("type"));
		this.from = Sensor.getBySensorUid(Integer.parseInt(req
				.getParameter("sensor")));
		this.values = Base64.getDecoder().decode(
				req.getParameter("data").getBytes());
		this.location = new LocationLatLong(Integer.parseInt(req
				.getParameter("lat")), Integer.parseInt(req
				.getParameter("long")));
		this.time = Long.parseLong(req.getParameter("time"));
		this.battery = Integer.parseInt(req.getParameter("battery"));
		this.receiverNode = Node.getByUid(Integer.parseInt(req
				.getParameter("receiver")));
		this.temperature = Short.parseShort(req.getParameter("temperature"));
	}

	public Sensor getFrom() {
		return from;
	}

	public int getType() {
		return type;
	}

	public byte[] getValues() {
		return values;
	}

	public LocationLatLong getLocation() {
		return location;
	}

	public long getTime() {
		return time;
	}

	@Override
	public void commit() throws IOException, SQLException,
			InvalidNodeAuthException {
		if (!SensNetNodeConfiguration.getInstance().isRootNode()) {
			SuperCommunicationsManager.getInstance().putJob(
					new ExceptionRunnable() {

						@Override
						public void run() throws Exception {
							ConnUtils.postNodeAuthenticatedData(
									DataPointSubmitPage.PATH,
									"&type="
											+ type
											+ "&sensor="
											+ from.getId()
											+ "&data="
											+ URLEncoder.encode(Base64
													.getEncoder()
													.encodeToString(values),
													"UTF-8") + "&lat="
											+ location.getLat() + "&long="
											+ location.getLng() + "&battery="
											+ battery + "&time=" + time
											+ "&receiver="
											+ receiverNode.getUid()
											+ "&temperature=" + temperature);
						}
					});

		}
		PreparedStatement prep = DatabaseConnection
				.getInstance()
				.prepare(
						"INSERT INTO datapoints SET `from`=?, `type`=?, locationlat=?, locationlong=?, battery=?, received=?, `value`=?, receivernode=?, temperature=?");
		prep.setInt(1, from.getId());
		prep.setInt(2, type);
		prep.setInt(3, location.getLat());
		prep.setInt(4, location.getLng());
		prep.setInt(5, battery);
		System.out.println(time);
		prep.setLong(6, time);
		prep.setBytes(7, values);
		prep.setInt(8, receiverNode.getUid());
		prep.setShort(9, temperature);
		prep.execute();
		ResultSet id = prep.getGeneratedKeys();
		id.next();
		SensorIndexizer indexer = IndexizerHolder.getInstance().getIndexizer(
				type);
		if (indexer != null) {
			indexer.indexize(this, id.getInt(1));
		}
		id.close();
		Logger.getAnonymousLogger().log(Level.INFO, "Datapoint commited.");
	}

	public int getBattery() {
		return battery;
	}

	public Node getReceiverNode() {
		return receiverNode;
	}

	public short getTemperature() {
		return temperature;
	}

}
