package net.sensnet.plugins.visualizer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.dbobjects.DataPoint;
import net.sensnet.node.pages.NodesOverviewPage;
import net.sensnet.node.plugins.SensorIndexer;

import org.json.JSONObject;

public class RadioDoseSensor extends SensorIndexer {

	public RadioDoseSensor() throws SQLException {
		super();
	}

	@Override
	public String getSensorName() {
		return "radiodose";
	}

	@Override
	public String createIndexTableArguments() {
		return "`id` int(11) unsigned NOT NULL AUTO_INCREMENT, `datapoint` "
				+ "int(11) unsigned NOT NULL, `dose` int(5) "
				+ "unsigned NOT NULL, PRIMARY KEY (`id`)";
	}

	@Override
	public String getInsertionPreparedQuery() {
		return "INSERT INTO " + getTableName()
				+ " (datapoint,dose) VALUES(?,?)";
	}

	@Override
	public boolean indexize(PreparedStatement insertQuery, DataPoint target,
			int id) throws SQLException {
		insertQuery.setInt(1, id);
		// ByteBuffer bf = ByteBuffer.wrap(point.getValues());
		if (target.getValues().length == 0) {
			insertQuery.setInt(2, 0);
		} else {
			insertQuery.setInt(2, ((target.getValues().length - 4) / 2) * 12);
		}
		return insertQuery.execute();
	}

	@Override
	public int getSensorType() {
		return 1;
	}

	public static JSONObject[] getDoses(Date from, Date to) throws SQLException {
		PreparedStatement prep = DatabaseConnection
				.getInstance()
				.prepare(
						"SELECT receivernode, `from`, received, dose, datapoints.locationlat AS lat, datapoints.locationlong AS lng FROM sensor_radiodose LEFT JOIN datapoints ON (datapoint = datapoints.id) WHERE received >= ? AND received <= ?");
		prep.setInt(1, (int) (from.getTime() / 1000));
		prep.setInt(2, (int) (to.getTime() / 1000));
		ResultSet resSet = prep.executeQuery();
		return parseQuery(resSet);
	}

	public static JSONObject[] getLatestDosesFromAllSensors()
			throws SQLException {
		PreparedStatement prep = DatabaseConnection
				.getInstance()
				.prepare(
						"SELECT receivernode, `from`, received, dose, a.locationlat AS lat, a.locationlong AS lng FROM sensor_radiodose LEFT JOIN datapoints a ON (datapoint = a.id) WHERE NOT EXISTS (SELECT 1 FROM datapoints b WHERE a.`from`=b.`from` and a.received < b.received)");
		ResultSet resSet = prep.executeQuery();
		return parseQuery(resSet);
	}

	public static JSONObject getLatestAvgDosesFromSensor(int sensor,
			Date upperLimit) throws SQLException {
		PreparedStatement prep = DatabaseConnection
				.getInstance()
				.prepare(
						"SELECT receivernode, `from`, received, dose, a.locationlat AS lat, a.locationlong AS lng FROM sensor_radiodose LEFT JOIN datapoints a ON (datapoint = a.id) WHERE `from` = ? AND received <= ? ORDER BY received DESC LIMIT 0,5 ");
		prep.setInt(1, sensor);
		prep.setLong(2, upperLimit.getTime() / 1000);
		ResultSet resSet = prep.executeQuery();
		int count = 0;
		HashMap<String, Object> out = new HashMap<String, Object>();
		if (resSet.last()) {
			count++;
			resSet.first();
			out.put("lat", resSet.getInt("lat") + "");
			out.put("lng", resSet.getInt("lng") + "");
			int avg = resSet.getInt("dose");
			for (int i = 0; i < 4; i++) {
				count++;
				if (resSet.next()) {
					avg += resSet.getInt("dose");
				} else {
					break;
				}
			}
			avg = avg /= count;
			out.put("count", avg);
			resSet.previous();
			out.put("uid", resSet.getInt("from"));
			resSet.close();
			return new JSONObject(out);
		}
		resSet.close();
		return null;
	}

	public static JSONObject[] getLatestAvgDosesFromAllSensors(Date upperLimit)
			throws SQLException {
		PreparedStatement sensors = DatabaseConnection
				.getInstance()
				.prepare(
						"SELECT `from` FROM datapoints WHERE type=3 AND class=3 AND received <= ? GROUP BY `from`");
		sensors.setLong(1, upperLimit.getTime() / 1000);
		ResultSet resSet = sensors.executeQuery();
		if (resSet.last()) {
			JSONObject[] res = new JSONObject[resSet.getRow()];
			resSet.beforeFirst();
			int i = 0;
			while (resSet.next()) {
				res[i++] = getLatestAvgDosesFromSensor(resSet.getInt("from"),
						upperLimit);
			}
			resSet.close();
			return res;
		}
		resSet.close();
		return new JSONObject[0];
	}

	public static JSONObject[] getDoses(Date from, Date to, int receiverNode)
			throws SQLException {
		PreparedStatement prep = DatabaseConnection
				.getInstance()
				.prepare(
						"SELECT receivernode, `from`, received, dose, datapoints.locationlat AS lat, datapoints.locationlong AS lng FROM sensor_radiodose LEFT JOIN datapoints ON (datapoint = datapoints.id) WHERE received >= ? AND received <= ? AND receivernode = ?");
		prep.setInt(1, (int) (from.getTime() / 1000));
		prep.setInt(2, (int) (to.getTime() / 1000));
		prep.setInt(3, receiverNode);
		ResultSet resSet = prep.executeQuery();
		return parseQuery(resSet);
	}

	private static JSONObject[] parseQuery(ResultSet resSet)
			throws SQLException {
		if (resSet.last()) {
			JSONObject[] res = new JSONObject[resSet.getRow()];
			resSet.beforeFirst();
			int i = 0;
			while (resSet.next()) {
				HashMap<String, Object> in = new HashMap<String, Object>();
				in.put("lat", resSet.getInt("lat") + "");
				in.put("lng", resSet.getInt("lng") + "");
				in.put("dose", resSet.getInt("dose") + "");
				in.put("received", resSet.getInt("received") + "");
				in.put("from", resSet.getInt("from") + "");
				in.put("receivernode", resSet.getInt("receivernode") + "");
				res[i++] = new JSONObject(in);
			}
			resSet.close();
			return res;
		}
		resSet.close();
		return new JSONObject[0];
	}

	public static String makeCoordinate(int coord) {
		String lng = NodesOverviewPage.ammendZero(coord + "", 6);
		lng = lng.substring(0, lng.length() - 5) + "."
				+ lng.substring(lng.length() - 5, lng.length());
		return lng;
	}

	@Override
	public int getSensorClass() {
		return 3;
	}
}
