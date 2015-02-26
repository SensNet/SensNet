package net.sensnet.node.dbobjects.sensors;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.dbobjects.DataPoint;

//TODO Update before year 2038 ;)
public class BLEGasSensor {
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

	public static int[][] getLatestShifts(int limit) throws SQLException {
		PreparedStatement prep = DatabaseConnection
				.getInstance()
				.prepare(
						"SELECT phaseshift, `from` FROM sensor_gas LEFT JOIN datapoints on (sensor_gas.datapoint=datapoints.id) ORDER BY received LIMIT 0, ?");
		prep.setInt(1, limit);
		ResultSet resSet = prep.executeQuery();
		if (resSet.last()) {
			int[][] res = new int[resSet.getRow()][];
			resSet.beforeFirst();
			int i = 0;
			while (resSet.next()) {
				res[i] = new int[2];
				res[i][0] = resSet.getInt("phaseshift");
				res[i++][1] = resSet.getInt("from");
			}
			return res;
		}
		return null;
	}

	public static boolean insert(DataPoint point, int id) throws SQLException {
		PreparedStatement pre = DatabaseConnection.getInstance().prepare(
				"INSERT INTO sensor_gas (datapoint,phaseshift) VALUES(?,?)");
		String data = new String(point.getValues()).trim();
		pre.setInt(1, point.getBattery());
		pre.setInt(2, Integer.parseInt(data.split(";")[0]));
		return pre.execute();
	}

	public static int[] getLatestShifts(int sensor, int limit)
			throws SQLException {
		PreparedStatement prep = DatabaseConnection
				.getInstance()
				.prepare(
						"SELECT phaseshift FROM sensor_gas LEFT JOIN datapoints on (sensor_gas.datapoint=datapoints.id) WHERE `from` = ? ORDER BY received LIMIT 0, ?");
		prep.setInt(1, sensor);
		prep.setInt(2, limit);
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

}
