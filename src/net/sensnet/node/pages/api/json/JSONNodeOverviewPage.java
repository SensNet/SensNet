package net.sensnet.node.pages.api.json;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import net.sensnet.node.DatabaseConnection;

import org.json.JSONArray;
import org.json.JSONException;

public class JSONNodeOverviewPage extends JSONApiPage {
	public static final String PATH = "/api/json/nodes";

	@Override
	public JSONArray getData(HttpServletRequest req) throws JSONException,
			SQLException {
		PreparedStatement nodes = DatabaseConnection.getInstance().prepare(
				"SELECT * FROM nodes");
		ResultSet resSet = nodes.executeQuery();
		JSONArray jsonRes = new JSONArray();
		resSet.beforeFirst();
		while (resSet.next()) {
			LinkedList<String> res = new LinkedList<String>();
			res.add(resSet.getInt("uid") + "");
			res.add(resSet.getString("name"));
			res.add(resSet.getString("description"));
			jsonRes.put(res);
		}
		return jsonRes;
	}

}
