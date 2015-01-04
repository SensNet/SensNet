package net.sensnet.node.pages;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.InvalidNodeAuthException;
import net.sensnet.node.dbobjects.Node;

public class RegisterNodePage extends APIPage {
	public static final String PATH = "/api/regsiter/node";

	public RegisterNodePage(String name) {
		super(name);
	}

	@Override
	public void doAction(HttpServletRequest req, HttpServletResponse resp)
			throws SQLException, IOException, InvalidNodeAuthException {
		Node n = new Node(req);
		n.commit();
	}
}
