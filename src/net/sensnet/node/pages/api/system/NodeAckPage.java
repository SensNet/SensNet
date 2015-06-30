package net.sensnet.node.pages.api.system;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.InvalidNodeAuthException;
import net.sensnet.node.dbobjects.Node;
import net.sensnet.node.util.AuthUtils;

public class NodeAckPage extends APIPage {
	public static final String PATH = "/api/nodeAck";

	public NodeAckPage() {
		super("Node ACK");
	}

	@Override
	public void doAction(HttpServletRequest req, HttpServletResponse resp)
			throws SQLException, IOException, InvalidNodeAuthException {
		try {
			if (AuthUtils.checkNodeAuth(req)) {
				Node n = Node.getByUid(Integer.parseInt(req
						.getParameter("node_id")));
				resp.getWriter().print("name=");
				resp.getWriter().println(n.getName());
				resp.getWriter().print("description=");
				resp.getWriter().println(n.getDescription());
			} else {
				resp.sendError(403);
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean reallyNeedsLogin() {
		return false;
	}

}
