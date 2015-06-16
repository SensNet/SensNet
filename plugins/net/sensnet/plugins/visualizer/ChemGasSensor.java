package net.sensnet.plugins.visualizer;

import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.dbobjects.DataPoint;
import net.sensnet.node.plugins.SensorIndexer;

public class ChemGasSensor extends SensorIndexer {

	public ChemGasSensor() throws SQLException {
		super();
	}

	@Override
	public String getSensorName() {
		return "chemgas5vbutan";
	}

	@Override
	public String createIndexTableArguments() {
		return "`id` int(11) unsigned NOT NULL AUTO_INCREMENT, `datapoint` "
				+ "int(11) unsigned NOT NULL, `ppm` int(11) NOT NULL, PRIMARY KEY (`id`)";
	}

	@Override
	public String getInsertionPreparedQuery() {
		return "INSERT INTO " + getTableName() + " (datapoint,ppm) VALUES(?,?)";
	}

	@Override
	public boolean indexize(PreparedStatement insertQuery, DataPoint target,
			int id) throws SQLException {
		insertQuery.setInt(1, id);
		ByteBuffer buf = ByteBuffer.wrap(target.getValues());
		short res = buf.getShort(0);

		res <<= 8;
		res += buf.getShort(2);
		System.out.println("RES: " + res);
		buf.clear();
		float voltage = (3.5f / (float) Math.pow(2, 11)) * res;
		System.out.println("Voltage: " + voltage);
		float ohm = (20000 * voltage) / (7 - 2 * voltage);
		int ppm = (int) ((186500 / 17) - ((33 * ohm) / 85));
		if (ohm > 1000000 || ohm < 0) {
			return true;
		}
		if (ppm < 100) {
			ppm = 0;
		}
		System.out.println("ppm: " + ppm + " ohm: " + ohm);
		insertQuery.setInt(2, ppm);
		return insertQuery.execute();
	}

	@Override
	public int getSensorType() {
		return 2;
	}

	public static String[][] getLatestDosesFromAllSensors(Date upperLimit)
			throws SQLException {
		PreparedStatement prep = DatabaseConnection
				.getInstance()
				.prepare(
						"SELECT receivernode, `from`, received, voltage, a.locationlat AS lat, a.locationlong AS lng FROM sensor_chemgas LEFT JOIN datapoints a ON (datapoint = a.id) WHERE received <= ? ORDER BY received DESC LIMIT 0,1");
		prep.setLong(1, upperLimit.getTime() / 1000);
		ResultSet resSet = prep.executeQuery();
		return parseQuery(resSet);
	}

	private static String[][] parseQuery(ResultSet resSet) throws SQLException {
		if (resSet.last()) {
			String[][] res = new String[resSet.getRow()][];
			resSet.beforeFirst();
			int i = 0;
			while (resSet.next()) {
				String[] in = new String[6];
				in[0] = resSet.getInt("lat") + "";
				in[1] = resSet.getInt("lng") + "";
				in[2] = resSet.getFloat("voltage") + "";
				in[3] = resSet.getInt("received") + "";
				in[4] = resSet.getInt("from") + "";
				in[5] = resSet.getInt("receivernode") + "";
				res[i++] = in;
			}
			resSet.close();
			return res;
		}
		resSet.close();
		return new String[0][];
	}

	@Override
	public int getSensorClass() {
		return 2;
	}

}
