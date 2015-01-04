package net.sensnet.node;


import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import net.sensnet.node.dbobjects.Node;

public class SensNetNodeConfiguration {
    private Properties p;
	private static SensNetNodeConfiguration instance;
	private String superNode;
	private Node me = null;

	public SensNetNodeConfiguration(InputStream in) throws IOException,
			SQLException {
        this.p = new Properties();
        p.load(in);
		instance = this;
		if (!isRootNode()) {
			superNode = p.getProperty("node.supernode");
		}
    }

    public int getPort() {
		return Integer.parseInt(p.getProperty("node.port"));
    }

    public String getHostName() {
		return p.getProperty("node.name");
    }

    public String getDB() {
		return p.getProperty("node.db");
    }

    public String getDBUser() {
		return p.getProperty("node.db.user");
    }

    public String getDBPW() {
		return p.getProperty("node.db.pw");
    }

    public String getJDBCDriver() {
		return p.getProperty("node.db.driver");
    }

	public String getTargetNode() {
		return p.getProperty("node.target");
	}

	public boolean isRootNode() {
		String property = p.getProperty("node.supernode");
		return property == null
				|| property.isEmpty();
	}

	public static SensNetNodeConfiguration getInstance() {
		return instance;
	}

	public String getSuperNode() {
		return superNode;
	}

	public String getSuperNodeAuth() throws SQLException {
		PreparedStatement auth = DatabaseConnection.getInstance().prepare(
				"SELECT superauthtoken FROM nodeconf");
		ResultSet executeQuery = auth.executeQuery();
		executeQuery.first();
		String token = executeQuery.getString("superauthtoken");
		executeQuery.close();
		return token;
	}

	public int getNodeID() throws SQLException {
		PreparedStatement auth = DatabaseConnection.getInstance().prepare(
				"SELECT id FROM nodeconf");
		ResultSet executeQuery = auth.executeQuery();
		executeQuery.first();
		int id = executeQuery.getInt("id");
		executeQuery.close();
		return id;
	}

	public String getNodeDescription() throws SQLException {
		PreparedStatement descr = DatabaseConnection.getInstance().prepare(
				"SELECT description FROM nodeconf");
		ResultSet executeQuery = descr.executeQuery();
		executeQuery.first();
		String description = executeQuery.getString("description");
		executeQuery.close();
		return description;
	}

	public String getNodeName() throws SQLException {
		PreparedStatement name = DatabaseConnection.getInstance().prepare(
				"SELECT name FROM nodeconf");
		ResultSet executeQuery = name.executeQuery();
		executeQuery.first();
		String nodeName = executeQuery.getString("name");
		executeQuery.close();
		return nodeName;
	}

	public boolean isLoginRequired() {
		return Boolean.parseBoolean(p.getProperty("node.requireLogin"));
	}

	public Node getThisNode() throws SQLException {
		if (me == null) {
			me = new Node(getNodeID(), getNodeName(), getNodeDescription());
		}
		return me;
	}

	public String getSerialInputDevice() {
		return p.getProperty("node.serial");
	}

}
