package net.sensnet.node.pages.api.system;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.InvalidNodeAuthException;
import net.sensnet.node.Page;
import net.sensnet.node.util.AuthUtils;

public abstract class APIPage extends Page {

	public APIPage(String name) {
		super(name);
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
				doAction(req, resp);
			} else {
				resp.sendError(403);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			resp.sendError(500);
		} catch (InvalidNodeAuthException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean needsLogin() {
		return false;
	}

	@Override
	public boolean needsTemplate() {
		return false;
	}

	public abstract void doAction(HttpServletRequest req,
			HttpServletResponse resp) throws SQLException, IOException,
			InvalidNodeAuthException;

}
