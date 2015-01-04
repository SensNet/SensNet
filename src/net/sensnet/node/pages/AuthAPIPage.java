package net.sensnet.node.pages;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.InvalidNodeAuthException;
import net.sensnet.node.SensNetNodeConfiguration;
import net.sensnet.node.dbobjects.User;
import net.sensnet.node.util.AuthUtils;

public class AuthAPIPage extends APIPage {

	public AuthAPIPage(String name) {
		super(name);
	}

	@Override
	public void doAction(HttpServletRequest req, HttpServletResponse resp)
			throws SQLException, IOException {
		String pw = req.getParameter("password");
		String user = req.getParameter("username");
		User u = null;
		if (SensNetNodeConfiguration.getInstance().isRootNode()) {
			u = AuthUtils.checkUserAgainstDB(user, pw);
		} else {
			try {
				u = AuthUtils.checkUserAgaistSuperNode(user, pw);
			} catch (InvalidNodeAuthException e) {
				e.printStackTrace();
			}
		}
		resp.getWriter().print("Auth: ");
		resp.getWriter().println(u != null);
		resp.getWriter().print("readonly: ");
		resp.getWriter().println(u != null && u.isReadOnly());
	}

	@Override
	public boolean reallyNeedsLogin() {
		return false;
	}

}
