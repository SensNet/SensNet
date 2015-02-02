package net.sensnet.node.dbobjects.sensors;

import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.dbobjects.DataPoint;

public class RadioDoseSensor {
	public static int[] getDoses(Date from, Date to) throws SQLException {
		PreparedStatement prep = DatabaseConnection
				.getInstance()
				.prepare(
						"SELECT dose FROM sensor_radiodose WHERE datapoint IN (SELECT id FROM datapoints WHERE received >= ? AND received <= ?)");
		prep.setTimestamp(1, new Timestamp(from.getTime()));
		prep.setTimestamp(2, new Timestamp(from.getTime()));
		ResultSet resSet = prep.executeQuery();
		if (resSet.last()) {
			int[] res = new int[resSet.getRow()];
			resSet.beforeFirst();
			int i = 0;
			while (resSet.next()) {
				res[i++] = resSet.getInt("dose");
			}
			return res;
		}
		return null;
	}

	public static boolean insert(DataPoint point, int id) throws SQLException {
		PreparedStatement pre = DatabaseConnection
				.getInstance()
				.prepare(
				"INSERT INTO sensor_radiodose (datapoint,dose) VALUES(?,?)");
		pre.setInt(1, id);
		ByteBuffer bf = ByteBuffer.wrap(point.getValues());
		pre.setInt(2, bf.getInt(0));
		return pre.execute();
	}

}
