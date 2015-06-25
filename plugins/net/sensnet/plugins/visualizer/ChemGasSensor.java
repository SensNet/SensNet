package net.sensnet.plugins.visualizer;

import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.dbobjects.DataPoint;
import net.sensnet.node.plugins.SensorIndexer;

import org.json.JSONObject;

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

	public static JSONObject[] getLatestDosesFromAllSensors(Date upperLimit)
			throws SQLException {
		PreparedStatement prep = DatabaseConnection
				.getInstance()
				.prepare(
						"SELECT receivernode, `from`, received, ppm, a.locationlat AS lat, a.locationlong AS lng FROM sensor_chemgas5vbutan LEFT JOIN datapoints a ON (datapoint = a.id) WHERE received <= ? ORDER BY received DESC LIMIT 0,1");
		prep.setLong(1, upperLimit.getTime() / 1000);
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
				in.put("ppm", resSet.getFloat("ppm") + "");
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

	@Override
	public int getSensorClass() {
		return 2;
	}

}
