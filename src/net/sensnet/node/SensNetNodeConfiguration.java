package net.sensnet.node;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import net.sensnet.node.dbobjects.Node;
import net.sensnet.node.pages.NodeAckPage;
import net.sensnet.node.util.ConnUtils;

public class SensNetNodeConfiguration {
	private Properties p;
	private static SensNetNodeConfiguration instance;
	private String superNode;
	private Node me = null;

	protected SensNetNodeConfiguration(InputStream in) throws IOException,
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

	protected String getDB() {
		return p.getProperty("node.db");
	}

	protected String getDBUser() {
		return p.getProperty("node.db.user");
	}

	protected String getDBPW() {
		return p.getProperty("node.db.pw");
	}

	protected String getJDBCDriver() {
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

	public void setSuperNodeAuth(String token) throws IOException,
			SQLException, InvalidNodeAuthException {
		p.setProperty("node.supernode.auth", token);
		store();
		Properties p = new Properties();
		p.load(ConnUtils.postNodeAuthenticatedData(NodeAckPage.PATH, ""));
		setNodeDescription(p.getProperty("description"));
		setNodeName(p.getProperty("name"));
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

	public String getProperty(String property) {
		return p.getProperty("plugin." + property);
	}

	public String getMapSource() {
		return p.getProperty("node.mapsource");
	}

	public int getLocationLat() {
		return Integer.parseInt(p.getProperty("node.location.lat"));
	}

	public int getLocationLng() {
		return Integer.parseInt(p.getProperty("node.location.long"));
	}

	public void setLocationLat(int lat) {
		p.setProperty("node.location.lat", lat + "");
		store();
	}

	public void setLocationLong(int lng) {
		p.setProperty("node.location.long", lng + "");
		store();
	}

	public boolean isHSTSEnabled() {
		return Boolean.getBoolean(p.getProperty("node.hsts", "true"));
	}
}
