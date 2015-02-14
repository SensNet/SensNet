package net.sensnet.node;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
		return property == null || property.isEmpty();
	}

	public static SensNetNodeConfiguration getInstance() {
		return instance;
	}

	public String getSuperNode() {
		return superNode;
	}

	public String getSuperNodeAuth() {
		return p.getProperty("node.supernode.auth");
	}

	public int getNodeID() {
		return Integer.parseInt(p.getProperty("node.id.uid"));
	}

	public String getNodeDescription() {
		return p.getProperty("node.id.description");
	}

	public String getNodeName() {
		return p.getProperty("node.id.name");
	}

	public void setNodeName(String name) {
		p.setProperty("node.id.name", name);
		store();
	}

	public void setNodeUID(int uid) {
		p.setProperty("node.id.uid", uid + "");
		store();
	}

	public void setNodeDescription(String description) {
		p.setProperty("node.id.description", description);
		store();
	}

	public void setSuperNodeAuth(String token) {
		p.setProperty("node.supernode.auth", token);
		store();
	}

	public void setSuperNode(String superNode) {
		p.setProperty("node.supernode", superNode);
		store();
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

	private void store() {
		File f = new File("conf/");
		if (!f.exists()) {
			f.mkdir();
		}
		f = new File("conf/node.properties");
		try {
			p.store(new FileOutputStream(f), "");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
