package net.sensnet.node.pages;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.Page;
import net.sensnet.node.dbobjects.Node;
import net.sensnet.node.util.AuthUtils;

public class NodeAckPage extends Page {
	public static final String PATH = "/api/nodeAck";

	public NodeAckPage() {
		super("Node ACK");
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException {
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException {
		try {
			if (AuthUtils.checkNodeAuth(req)) {
				Node n = Node.getByUid(Integer.parseInt(req
						.getParameter("node_id")));
				resp.getWriter().print("name=");
				resp.getWriter().println(n.getName());
				resp.getWriter().print("description=");
				resp.getWriter().println(n.getDescription());
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean needsLogin() {
		return false;
	}

	@Override
	public boolean reallyNeedsLogin() {
		return false;
	}

	@Override
	public boolean needsTemplate() {
		return false;
	}

}
