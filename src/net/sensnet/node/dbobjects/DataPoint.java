package net.sensnet.node.dbobjects;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.IndexerHolder;
import net.sensnet.node.InvalidNodeAuthException;
import net.sensnet.node.SensNetNodeConfiguration;
import net.sensnet.node.pages.DataPointSubmitPage;
import net.sensnet.node.plugins.SensorIndexer;
import net.sensnet.node.supercommunications.HttpSyncAction;
import net.sensnet.node.supercommunications.SuperCommunicationsManager;

public class DataPoint implements Syncable {
	private int type, sensorClass;
	private Sensor from;
	private LocationLatLong location;
	private long time;
	private int battery;
	private Node receiverNode;
	private short temperature;
	private byte[] values;

	public DataPoint(int type, int sensorsClass, Sensor from, short[] values,
			long time, int battery, LocationLatLong location,
			short temperature, Node receiverNode) {
		this.type = type;
		this.from = from;
		ByteBuffer buf = ByteBuffer.allocate(values.length * 2);
		for (int i = 0; i < values.length; i++) {
			buf.putShort(values[i]);
		}
		this.values = buf.array();
		buf.clear();
		this.time = time;
		this.temperature = temperature;
		this.battery = battery;
		this.location = location;
		this.receiverNode = receiverNode;
	}

	public DataPoint(int type, int sensorsClass, Sensor from, byte[] values,
			long time, int battery, LocationLatLong location,
			short temperature, Node receiverNode) {
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
		this.sensorClass = Integer.parseInt(req.getParameter("class"));
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
					new HttpSyncAction() {

						@Override
						public String getPostData()
								throws UnsupportedEncodingException {
							return "&type="
									+ type
									+ "&sensor="
									+ from.getId()
									+ "&data="
									+ URLEncoder.encode(Base64.getEncoder()
											.encodeToString(values), "UTF-8")
									+ "&lat=" + location.getLat() + "&long="
									+ location.getLng() + "&battery=" + battery
									+ "&time=" + time + "&receiver="
									+ receiverNode.getUid() + "&temperature="
									+ temperature + "&class=" + sensorClass;
						}

						@Override
						public String getPath() {
							return DataPointSubmitPage.PATH;
						}
					});
		}
		PreparedStatement prep = DatabaseConnection
				.getInstance()
				.prepare(
						"INSERT INTO datapoints SET `from`=?, `type`=?, locationlat=?, locationlong=?, battery=?, received=?, `value`=?, receivernode=?, temperature=?, class=?");
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
		prep.setInt(10, sensorClass);
		prep.execute();
		ResultSet id = prep.getGeneratedKeys();
		id.next();
		SensorIndexer indexer = IndexerHolder.getInstance().getIndexizer(type);
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
