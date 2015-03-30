package net.sensnet.node.plugins;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.dbobjects.DataPoint;

public abstract class SensorIndexer {
	private PreparedStatement insertQuery;

	public SensorIndexer() throws SQLException {
		String insertString = getInsertionPreparedQuery();
		if (!insertString.toLowerCase().startsWith(
				"insert into sensor_" + getSensorName())
				|| insertString.contains(";")) {
			throw new IllegalArgumentException("Invalid insertion query: "
					+ insertString);
		}
		insertQuery = DatabaseConnection.getInstance().prepare(insertString);
		PreparedStatement prepare = DatabaseConnection.getInstance().prepare(
				"CREATE TABLE IF NOT EXISTS " + getTableName() + " ("
						+ createIndexTableArguments()
						+ ")  ENGINE=InnoDB DEFAULT CHARSET=utf8;");
		prepare.execute();
		prepare.close();
	}

	public abstract String getSensorName();

	public abstract String createIndexTableArguments();

	public abstract String getInsertionPreparedQuery();

	public boolean indexize(DataPoint target, int id) throws SQLException {
		synchronized (insertQuery) {
			return indexize(insertQuery, target, id);
		}
	}

	public abstract boolean indexize(PreparedStatement insertQuery,
			DataPoint target, int id) throws SQLException;

	public String getTableName() {
		return "sensor_" + getSensorName();
	}

	public abstract int getSensorType();
}