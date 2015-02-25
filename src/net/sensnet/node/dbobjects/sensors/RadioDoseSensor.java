package net.sensnet.node.dbobjects.sensors;

import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.dbobjects.DataPoint;

public class RadioDoseSensor {
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

	public static boolean insert(DataPoint point, int id) throws SQLException {
		if (point.getValues().length == 0) {
			return true;
		}
		PreparedStatement pre = DatabaseConnection.getInstance().prepare(
				"INSERT INTO sensor_radiodose (datapoint,dose) VALUES(?,?)");
		pre.setInt(1, id);
		ByteBuffer bf = ByteBuffer.wrap(point.getValues());
		pre.setInt(2, bf.getInt(0));
		return pre.execute();
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
