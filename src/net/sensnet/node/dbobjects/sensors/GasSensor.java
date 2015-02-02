package net.sensnet.node.dbobjects.sensors;

import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.dbobjects.DataPoint;

//TODO Update before year 2038 ;)
public class GasSensor {
	public static int[] getShifts(Date from, Date to) throws SQLException {
		PreparedStatement prep = DatabaseConnection
				.getInstance()
				.prepare(
						"SELECT pahseshift FROM snesor_gas WHERE datapoint IN (SELECT id FROM datapoints WHERE received >= ? AND received <= ?)");
		prep.setInt(1, (int) (from.getTime() / 1000));
		prep.setInt(2, (int) (from.getTime() / 1000));
		ResultSet resSet = prep.executeQuery();
		if (resSet.last()) {
			int[] res = new int[resSet.getRow()];
			resSet.beforeFirst();
			int i = 0;
			while (resSet.next()) {
				res[i++] = resSet.getInt("phaseshift");
			}
			return res;
		}
		return null;
	}

	public static boolean insert(DataPoint point, int id) throws SQLException {
		PreparedStatement pre = DatabaseConnection
				.getInstance()
				.prepare(
						"INSERT INTO sensor_radiodose (id,datapoint,phaseshift) VALUES('',?,?)");
		pre.setInt(2, id);
		ByteBuffer bf = ByteBuffer.wrap(point.getValues());
		pre.setInt(3, bf.getInt(0));
		return pre.execute();
	}

}
