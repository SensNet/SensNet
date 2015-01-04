package net.sensnet.node.dbobjects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import net.sensnet.node.DatabaseConnection;

public class SensorType {
	private int id;
	private String name, description, unitSuffix;
	private static HashMap<Integer, SensorType> cache = new HashMap<Integer, SensorType>();

	public SensorType(int id, String name, String description, String unitSuffix) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.unitSuffix = unitSuffix;
	}

	public SensorType(HttpServletRequest req) {
		this.id = Integer.parseInt(req.getParameter("sensor_id"));
		this.name = req.getParameter("sensor_name");
		this.description = req.getParameter("sensor_description");
		this.unitSuffix = req.getParameter("unit_suffix");
	}

	public SensorType(ResultSet res) throws SQLException {
		this.id = res.getInt(1);
		this.name = res.getString(2);
		this.description = res.getString(3);
		this.unitSuffix = res.getString(4);
	}

	public String getDescription() {
		return description;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getUnitSuffix() {
		return unitSuffix;
	}

	@Override
	public String toString() {
		return id + ", " + name + ", " + unitSuffix + ", " + description;
	}

	public void updateToDB() throws SQLException {
		PreparedStatement check = DatabaseConnection.getInstance().prepare(
				"SELECT name from sensortypes WHERE id=?");
		check.setInt(1, id);
		ResultSet checkres = check.executeQuery();
		if (checkres.first()) {
			PreparedStatement upd = DatabaseConnection.getInstance().prepare(
							"UPDATE sensortypes SET name = ?, description = ?, unitsuffix = ? WHERE id = ?");
			upd.setString(1, name);
			upd.setString(2, description);
			upd.setString(3, unitSuffix);
			upd.setInt(4, id);
			upd.executeUpdate();
		} else {
			PreparedStatement upd = DatabaseConnection.getInstance().prepare(
					"INSERT INTO sensortypes VALUES(?, ?, ?, ?)");
			upd.setInt(1, id);
			upd.setString(2, name);
			upd.setString(3, description);
			upd.setString(4, unitSuffix);
			upd.executeUpdate();
		}
	}

	public static SensorType getById(int id) throws SQLException {
		if (cache.containsKey(id)) {
			return cache.get(id);
		}
		PreparedStatement prep = DatabaseConnection.getInstance().prepare(
				"SELECT * FROM sensortypes WHERE id=?");
		prep.setInt(1, id);
		ResultSet query = prep.executeQuery();
		if (query.next()) {
			SensorType sensorType = new SensorType(query);
		cache.put(id, sensorType);
		return sensorType;
		}
		return null;
	}
}
