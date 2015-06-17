package net.sensnet.plugins.visualizer;

import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.dbobjects.DataPoint;
import net.sensnet.node.pages.NodesOverviewPage;
import net.sensnet.node.plugins.SensorIndexer;

public class ParticleSensor extends SensorIndexer {

	public ParticleSensor() throws SQLException {
		super();
	}

	@Override
	public String getSensorName() {
		return "particlesensnet";
	}

	@Override
	public String createIndexTableArguments() {
		return "`id` int(11) unsigned NOT NULL AUTO_INCREMENT, `datapoint` "
				+ "int(11) unsigned NOT NULL, `particles` int(11) "
				+ "unsigned NOT NULL, PRIMARY KEY (`id`)";
	}

	@Override
	public String getInsertionPreparedQuery() {
		return "INSERT INTO " + getTableName()
				+ " (datapoint,particles) VALUES(?,?)";
	}

	@Override
	public boolean indexize(PreparedStatement insertQuery, DataPoint target,
			int id) throws SQLException {
		ByteBuffer buf = ByteBuffer.wrap(target.getValues());
		insertQuery.setInt(1, id);
		long particles = Short.toUnsignedLong(buf.getShort(0));
		particles = (long) ((Math.pow(1.1f * (particles * 0.001), 3) - 3.8f
				* Math.pow(particles * 0.001f, 2) + 0.52f * particles + 0.62f) * 3533);
		insertQuery.setLong(2, particles);
		return insertQuery.execute();
	}

	@Override
	public int getSensorType() {
		return 2;
	}

	private static String[][] parseQuery(ResultSet resSet) throws SQLException {
		if (resSet.last()) {
			String[][] res = new String[resSet.getRow()][];
			resSet.beforeFirst();
			int i = 0;
			while (resSet.next()) {
				String[] in = new String[7];
				in[0] = resSet.getInt("lat") + "";
				in[1] = resSet.getInt("lng") + "";
				in[2] = resSet.getInt("particles") + "";
				in[5] = resSet.getInt("received") + "";
				in[5] = resSet.getInt("from") + "";
				in[6] = resSet.getInt("receivernode") + "";
				res[i++] = in;
			}
			resSet.close();
			return res;
		}
		resSet.close();
		return new String[0][];
	}

	public static String[][] getLatestDosesFromAllSensors(Date upperLimit)
			throws SQLException {
		PreparedStatement prep = DatabaseConnection
				.getInstance()
				.prepare(
						"SELECT receivernode, `from`, received, particles, a.locationlat AS lat, a.locationlong AS lng FROM sensor_particlesensnet LEFT JOIN datapoints a ON (datapoint = a.id) WHERE received <= ? ORDER BY received DESC LIMIT 0,1");
		prep.setLong(1, upperLimit.getTime() / 1000);
		ResultSet resSet = prep.executeQuery();
		return parseQuery(resSet);
	}

	public static String makeCoordinate(int coord) {
		String lng = NodesOverviewPage.ammendZero(coord + "", 6);
		lng = lng.substring(0, lng.length() - 5) + "."
				+ lng.substring(lng.length() - 5, lng.length());
		return lng;
	}

	@Override
	public int getSensorClass() {
		return 4;
	}
}
