package net.sensnet.node.dbobjects;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.ExceptionRunnable;
import net.sensnet.node.InvalidNodeAuthException;
import net.sensnet.node.SensNetNodeConfiguration;
import net.sensnet.node.SuperCommunicationsManager;
import net.sensnet.node.pages.RegisterNodePage;
import net.sensnet.node.util.ConnUtils;

import org.json.JSONArray;

public class Node implements Syncable {
	private int uid;
	private String name, description;
	private static HashMap<Integer, Node> cache = new HashMap<Integer, Node>();

	public Node(int id, String name, String description) {
		this.uid = id;
		this.name = name;
		this.description = description;
	}

	public Node(ResultSet res) throws SQLException {
		this.uid = res.getInt(1);
		this.name = res.getString(2);
		this.description = res.getString(3);
	}

	public Node(HttpServletRequest req) {
		this.uid = Integer.parseInt(req.getParameter("uid"));
		this.description = req.getParameter("description");
		this.name = req.getParameter("name");
	}

	public String getDescription() {
		return description;
	}

	public int getUid() {
		return uid;
	}

	public String getName() {
		return name;
	}

	public static Node getByUid(int uid) throws SQLException {
		if (uid == SensNetNodeConfiguration.getInstance().getNodeID()) {
			return SensNetNodeConfiguration.getInstance().getThisNode();
		}
		if (cache.containsKey(uid)) {
			return cache.get(uid);
		}
		PreparedStatement prep = DatabaseConnection.getInstance().prepare(
				"SELECT * FROM nodes WHERE uid = ?");
		prep.setInt(1, uid);
		ResultSet query = prep.executeQuery();
		if (query.next()) {
			Node node = new Node(query);
			cache.put(uid, node);
			return node;
		} else {
			return null;
		}
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
									RegisterNodePage.PATH, "&desciption="
											+ description + "&name=" + name
											+ "&uid=" + uid);
						}
					});
		}
		PreparedStatement prep = DatabaseConnection.getInstance().prepare(
				"INSERT INTO nodes SET uid=?, name=?, description=?");
		prep.setInt(1, uid);
		prep.setString(2, name);
		prep.setString(3, description);
		prep.executeUpdate();
	}

	public JSONArray getLatestSensors() throws SQLException {
		PreparedStatement prep = DatabaseConnection
				.getInstance()
				.prepare(
						"select * from datapoints a where not exists ( select * from datapoints b where b.`from` = a.`from` and a.`received` < b.`received` and b.`receivernode` = ?) and a.`receivernode` = ?");
		prep.setInt(1, uid);
		prep.setInt(2, uid);
		ResultSet resSet = prep.executeQuery();
		resSet.last();
		String[][] res = new String[resSet.getRow()][];
		resSet.beforeFirst();
		int count = 0;
		while (resSet.next()) {
			String[] thing = new String[3];
			thing[0] = resSet.getInt("from") + "";
			thing[1] = resSet.getLong("received") + "";
			thing[2] = resSet.getInt("battery") + " %";
			res[count++] = thing;
		}
		return new JSONArray(res);
	}

	public JSONArray getLatestSensors(int sensortype) throws SQLException {
		PreparedStatement prep = DatabaseConnection
				.getInstance()
				.prepare(
						"select * from datapoints a where not exists ( select * from datapoints b where b.`from` = a.`from` and a.`received` < b.`received` and b.`receivernode` = ?) and a.`receivernode` = ? and type = ?");
		prep.setInt(1, uid);
		prep.setInt(2, uid);
		prep.setInt(3, sensortype);
		ResultSet resSet = prep.executeQuery();
		resSet.last();
		String[][] res = new String[resSet.getRow()][];
		resSet.beforeFirst();
		int count = 0;
		while (resSet.next()) {
			String[] thing = new String[3];
			thing[0] = resSet.getInt("from") + "";
			thing[1] = resSet.getLong("received") + "";
			thing[2] = resSet.getInt("battery") + " %";
			res[count++] = thing;
		}
		return new JSONArray(res);
	}
}
