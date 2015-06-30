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
			if (res.first()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("first", res.getLong(1));
				return new JSONArray(new JSONObject[] { new JSONObject(map) });
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
