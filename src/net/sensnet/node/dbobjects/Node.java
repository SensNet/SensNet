package net.sensnet.node.dbobjects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import net.sensnet.node.DatabaseConnection;

public class Node {
	private int id;
	private String name, description;
	private static HashMap<Integer, Node> cache = new HashMap<Integer, Node>();

	public Node(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public Node(ResultSet res) throws SQLException {
		this.id = res.getInt(2);
		this.name = res.getString(3);
		this.description = res.getString(4);
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

	public static Node getByUid(int uid) throws SQLException {
		if (cache.containsKey(uid)) {
			return cache.get(uid);
		}
		PreparedStatement prep = DatabaseConnection.getInstance().prepare(
				"SELECT * FROM nodes WHERE uid = ?");
		prep.setInt(1, uid);
		ResultSet query = prep.executeQuery();
		if(query.next()) {
		Node node = new Node(query);
		cache.put(uid, node);
		return node;
		} else {
			return null;
		}
	}
}
