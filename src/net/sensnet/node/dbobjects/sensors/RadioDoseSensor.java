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
						"SELECT dose, datapoints.locationlat AS lat, datapoints.locationlong AS lng FROM sensor_radiodose LEFT JOIN datapoints ON (datapoint = datapoints.id) WHERE received >= ? AND received <= ?");
		prep.setLong(1, from.getTime() / 1000);
		prep.setLong(2, to.getTime() / 1000);
		ResultSet resSet = prep.executeQuery();
		if (resSet.last()) {
			int[][] res = new int[resSet.getRow()][];
			resSet.beforeFirst();
			int i = 0;
			while (resSet.next()) {
				int[] in = new int[3];
				in[0] = resSet.getInt("lat");
				in[1] = resSet.getInt("lng");
				in[2] = resSet.getInt("dose");
				res[i++] = in;
			}
			return res;
		}
		return null;
	}

	public static boolean insert(DataPoint point, int id) throws SQLException {
		PreparedStatement pre = DatabaseConnection.getInstance().prepare(
				"INSERT INTO sensor_radiodose (datapoint,dose) VALUES(?,?)");
		pre.setInt(1, id);
		ByteBuffer bf = ByteBuffer.wrap(point.getValues());
		pre.setInt(2, bf.getInt(0));
		return pre.execute();
	}

}
