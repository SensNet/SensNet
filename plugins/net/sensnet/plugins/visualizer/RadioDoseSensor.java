package net.sensnet.plugins.visualizer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.IndexizerHolder;
import net.sensnet.node.dbobjects.DataPoint;
import net.sensnet.node.plugins.SensorIndexizer;

public class RadioDoseSensor extends SensorIndexizer {

	public RadioDoseSensor() throws SQLException {
		super();
		IndexizerHolder.getInstance().put(new RadioDoseSensor());
	}

	@Override
	public String getSensorName() {
		return "radiodose";
	}

	@Override
	public String createIndexTableArguments() {
		return "`id` int(11) unsigned NOT NULL, `datapoint` "
				+ "int(11) unsigned NOT NULL, `dose` int(5) "
				+ "unsigned NOT NULL";
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
		return 3;
	}

	public static int[][] getDoses(Date from, Date to) throws SQLException {
		PreparedStatement prep = DatabaseConnection
				.getInstance()
				.prepare(
						"SELECT receivernode, `from`, received, dose, datapoints.locationlat AS lat, datapoints.locationlong AS lng FROM sensor_radiodose LEFT JOIN datapoints ON (datapoint = datapoints.id) WHERE received >= ? AND received <= ?");
		prep.setInt(1, (int) (from.getTime() / 1000));
		prep.setInt(2, (int) (to.getTime() / 1000));
		ResultSet resSet = prep.executeQuery();
		return parseQuery(resSet);
	}

	public static int[][] getLatestDosesFromAllSensors() throws SQLException {
		PreparedStatement prep = DatabaseConnection
				.getInstance()
				.prepare(
						"SELECT receivernode, `from`, received, dose, a.locationlat AS lat, a.locationlong AS lng FROM sensor_radiodose LEFT JOIN datapoints a ON (datapoint = a.id) WHERE NOT EXISTS (SELECT 1 FROM datapoints b WHERE a.`from`=b.`from` and a.received < b.received)");
		ResultSet resSet = prep.executeQuery();
		return parseQuery(resSet);
	}

	public static int[] getLatestAvgDosesFromSensor(int sensor, Date upperLimit)
			throws SQLException {
		PreparedStatement prep = DatabaseConnection
				.getInstance()
				.prepare(
						"SELECT receivernode, `from`, received, dose, a.locationlat AS lat, a.locationlong AS lng FROM sensor_radiodose LEFT JOIN datapoints a ON (datapoint = a.id) WHERE `from` = ? AND received <= ? ORDER BY received DESC LIMIT 0,5 ");
		prep.setInt(1, sensor);
		prep.setLong(2, upperLimit.getTime() / 1000);
		int[] res = new int[3];
		ResultSet resSet = prep.executeQuery();
		int count = 0;
		if (resSet.last()) {
			count++;
			resSet.first();
			res[0] = resSet.getInt("lat");
			res[1] = resSet.getInt("lng");
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
			res[2] = avg;
			return res;
		}
		return null;
	}

	public static int[][] getLatestAvgDosesFromAllSensors(Date upperLimit)
			throws SQLException {
		PreparedStatement sensors = DatabaseConnection
				.getInstance()
				.prepare(
						"SELECT `from` FROM datapoints WHERE type=3 AND received <= ? GROUP BY `from`");
		sensors.setLong(1, upperLimit.getTime() / 1000);
		ResultSet resSet = sensors.executeQuery();
		if (resSet.last()) {
			int[][] res = new int[resSet.getRow()][];
			resSet.beforeFirst();
			int i = 0;
			while (resSet.next()) {
				res[i++] = getLatestAvgDosesFromSensor(resSet.getInt("from"),
						upperLimit);
			}
			return res;
		}
		return null;
	}

	public static int[][] getDoses(Date from, Date to, int receiverNode)
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

	private static int[][] parseQuery(ResultSet resSet) throws SQLException {
		if (resSet.last()) {
			int[][] res = new int[resSet.getRow()][];
			resSet.beforeFirst();
			int i = 0;
			while (resSet.next()) {
				int[] in = new int[6];
				in[0] = resSet.getInt("lat");
				in[1] = resSet.getInt("lng");
				in[2] = resSet.getInt("dose");
				in[3] = resSet.getInt("received");
				in[4] = resSet.getInt("from");
				in[5] = resSet.getInt("receivernode");
				res[i++] = in;
			}
			return res;
		}
		return new int[0][];
	}
}
