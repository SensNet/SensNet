package net.sensnet.node.pages.api.json;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.InvalidNodeAuthException;
import net.sensnet.node.pages.APIPage;

public class SmallestDateSelector extends APIPage {
	public static final String PATH = "/api/smallestdate";

	public SmallestDateSelector() {
		super("Smallest date selector");
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException {
		int type = Integer.parseInt(req.getParameter("type"));
		PreparedStatement prep;
		try {
			prep = DatabaseConnection
					.getInstance()
					.prepare(
							"SELECT received FROM datapoints WHERE type=? ORDER BY received ASC LIMIT 1");
			prep.setInt(1, type);
			ResultSet res = prep.executeQuery();
			if (res.first()) {
				resp.getWriter().println(res.getLong(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean reallyNeedsLogin() {
		return false;
	}

	@Override
	public void doAction(HttpServletRequest req, HttpServletResponse resp)
			throws SQLException, IOException, InvalidNodeAuthException {
		// TODO Auto-generated method stub

	}

}
