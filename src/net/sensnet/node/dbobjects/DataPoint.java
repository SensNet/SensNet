package net.sensnet.node.dbobjects;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.ExceptionRunnable;
import net.sensnet.node.InvalidNodeAuthException;
import net.sensnet.node.SensNetNodeConfiguration;
import net.sensnet.node.SuperCommunicationsManager;
import net.sensnet.node.pages.DataPointSubmitPage;
import net.sensnet.node.util.ConnUtils;

public class DataPoint implements Syncable {
	private SensorType type;
	private Sensor from;
	private float value, value2;
	private LocationLatLong location;
	private long time;
	private int battery;
	private Node receiverNode;

	public DataPoint(SensorType type, Sensor from, float value, float value2,
			long time, int battery, LocationLatLong location, Node receiverNode) {
		this.type = type;
		this.from = from;
		this.value = value;
		this.value2 = value2;
		this.time = time;
		this.battery = battery;
		this.location = location;
		this.receiverNode = receiverNode;
	}

	public DataPoint(HttpServletRequest req) throws NumberFormatException,
			SQLException, IOException, InvalidNodeAuthException {
		this.type = SensorType.getById(Integer.parseInt(req
				.getParameter("type")));
		this.from = Sensor.getBySensorUid(Integer.parseInt(req
				.getParameter("sensor")));
		this.value = Float.parseFloat(req.getParameter("value"));
		this.value2 = Float.parseFloat(req.getParameter("value2"));
		this.location = new LocationLatLong(Integer.parseInt(req
				.getParameter("lat")), Integer.parseInt(req
				.getParameter("long")));
		this.time = Long.parseLong(req.getParameter("time"));
		this.battery = Integer.parseInt(req.getParameter("battery"));
		this.receiverNode = Node.getByUid(Integer.parseInt(req
				.getParameter("receiver")));
	}

	public Sensor getFrom() {
		return from;
	}

	public SensorType getType() {
		return type;
	}

	public double getValue() {
		return value;
	}

	public float getValue2() {
		return value2;
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
									"&type=" + type.getId() + "&sensor="
											+ from.getId() + "&value=" + value
											+ "&value2=" + value2 + "&lat="
											+ location.getLat() + "&long="
											+ location.getLng() + "&battery="
											+ battery + "&time=" + time
											+ "&receiver="
											+ receiverNode.getId());
						}
					});

		}
		PreparedStatement prep = DatabaseConnection
				.getInstance()
				.prepare(
						"INSERT INTO datapoints SET `from`=?, `type`=?, locationlat=?, locationlong=?, battery=?, received=?, `value`=?, value2=?, receivernode=?");
		prep.setInt(1, from.getId());
		prep.setInt(2, type.getId());
		prep.setInt(3, location.getLat());
		prep.setInt(4, location.getLng());
		prep.setInt(5, battery);
		prep.setTimestamp(6, new Timestamp(time));
		prep.setFloat(7, value);
		prep.setFloat(8, value2);
		prep.setInt(9, receiverNode.getId());
		prep.executeUpdate();
		Logger.getAnonymousLogger().log(Level.INFO, "Datapoint commited.");
	}

	public int getBattery() {
		return battery;
	}

	public Node getReceiverNode() {
		return receiverNode;
	}

}
