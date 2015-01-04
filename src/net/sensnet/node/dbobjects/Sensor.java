package net.sensnet.node.dbobjects;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.ExceptionRunnable;
import net.sensnet.node.InvalidNodeAuthException;
import net.sensnet.node.SensNetNodeConfiguration;
import net.sensnet.node.SuperCommunicationsManager;
import net.sensnet.node.pages.RegisterSensorPage;
import net.sensnet.node.util.ConnUtils;

public class Sensor implements Syncable {
	private static HashMap<Integer, Sensor> cache = new HashMap<Integer, Sensor>();
	private int id;
	private Node node;

	public Sensor(int id, Node node) {
		this.id = id;
		this.node = node;
	}

	public Sensor(ResultSet set) throws SQLException {
		this.id = set.getInt(2);
		this.node = Node.getByUid(set.getInt(3));
	}

	public int getId() {
		return id;
	}

	public Node getNode() {
		return node;
	}

	public static Sensor getBySensorUid(int uid) throws SQLException,
			IOException, InvalidNodeAuthException {
		if (cache.containsKey(uid)) {
			return cache.get(uid);
		}
		PreparedStatement ps = DatabaseConnection.getInstance().prepare(
				"SELECT * FROM sensors WHERE uid=?");
		ps.setInt(1, uid);
		ResultSet query = ps.executeQuery();
		Sensor sensor;
		if (!query.next()) {
			sensor = new Sensor(uid, SensNetNodeConfiguration.getInstance()
					.getThisNode());
			Logger.getAnonymousLogger()
					.log(Level.INFO, "New Sensor registerd.");
			sensor.commit();
		} else {
			sensor = new Sensor(query);
		}
		cache.put(uid, sensor);
		return sensor;
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
									RegisterSensorPage.PATH, "&uid=" + id
											+ "&node=" + node.getId());
						}
					});
		}
		PreparedStatement prep = DatabaseConnection.getInstance().prepare(
				"INSERT INTO sensors SET uid=?, node=?");
		prep.setInt(1, id);
		prep.setInt(2, node.getId());
		prep.executeUpdate();
	}
}
