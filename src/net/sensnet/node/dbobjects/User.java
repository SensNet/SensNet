package net.sensnet.node.dbobjects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sensnet.node.DatabaseConnection;

public class User {
	private String name;
	private boolean readOnly;

	public User(ResultSet res, String name) throws SQLException {
		this.name = name;
		readOnly = res.getBoolean("readonly");
	}

	public User(String username, boolean readOnly) {
		this.name = username;
		this.readOnly = readOnly;
	}

	public String getName() {
		return name;
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		this.readOnly = readOnly;
		PreparedStatement ps = DatabaseConnection.getInstance().prepare(
				"UPDATE users SET readonly = ?");
		ps.setBoolean(1, readOnly);
		ps.execute();
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void create(String pw) throws SQLException {
		PreparedStatement ps = DatabaseConnection.getInstance().prepare(
				"INSERT INTO users VALUES('', ?, ? , ?)");
		ps.setString(1, name);
		ps.setString(2, pw);
		ps.setBoolean(3, readOnly);
		ps.executeUpdate();
	}
}
