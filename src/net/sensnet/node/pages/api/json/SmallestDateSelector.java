package net.sensnet.node.pages.api.json;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import net.sensnet.node.DatabaseConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SmallestDateSelector extends JSONApiPage {
	public static final String PATH = "/api/smallestdate";

	@Override
	public JSONArray getData(HttpServletRequest req) throws JSONException,
			SQLException {
		int type = Integer.parseInt(req.getParameter("type"));
		int sensorClass = Integer.parseInt(req.getParameter("class"));
		PreparedStatement prep;
		try {
			prep = DatabaseConnection
					.getInstance()
					.prepare(
							"SELECT received FROM datapoints WHERE type=? AND class=? ORDER BY received ASC LIMIT 1");
			prep.setInt(1, type);
			prep.setInt(2, sensorClass);
			ResultSet res = prep.executeQuery();
			JSONObject obj = null;
			if (res.first()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("eraliest", res.getLong(1));
				obj = new JSONObject(map);
			}
			return new JSONArray(obj);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// public SmallestDateSelector() {
	// super("Smallest date selector");
	// }
	//
	// @Override
	// public void doGet(HttpServletRequest req, HttpServletResponse resp,
	// Map<String, Object> vars) throws IOException {
	// int type = Integer.parseInt(req.getParameter("type"));
	// int sensorClass = Integer.parseInt(req.getParameter("class"));
	// PreparedStatement prep;
	// try {
	// prep = DatabaseConnection
	// .getInstance()
	// .prepare(
	// "SELECT received FROM datapoints WHERE type=? AND class=? ORDER BY received ASC LIMIT 1");
	// prep.setInt(1, type);
	// prep.setInt(2, sensorClass);
	// ResultSet res = prep.executeQuery();
	// if (res.first()) {
	// resp.getWriter().println(res.getLong(1));
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// @Override
	// public boolean reallyNeedsLogin() {
	// return false;
	// }
	//
	// @Override
	// public void doAction(HttpServletRequest req, HttpServletResponse resp)
	// throws SQLException, IOException, InvalidNodeAuthException {
	// // TODO Auto-generated method stub
	//
	// }

}
