package net.sensnet.node.pages.api.json;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.InvalidNodeAuthException;
import net.sensnet.node.dbobjects.Node;
import net.sensnet.node.dbobjects.Sensor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONNodeOverviewPage extends JSONApiPage {
	public static final String PATH = "/api/json/nodes";

	@Override
	public JSONArray getData(HttpServletRequest req) throws JSONException,
			SQLException {
		if (req.getParameter("forsensor") != null) {
			try {
				Node n = Sensor.getBySensorUid(
						Integer.parseInt(req.getParameter("forsensor")))
						.getNode();
				if (n == null) {
					return new JSONArray();
				}
				HashMap<String, Object> obj = new HashMap<String, Object>();
				obj.put("uid", n.getUid() + "");
				obj.put("name", n.getName());
				obj.put("description", n.getDescription());
				return new JSONArray(new JSONObject(obj));
			} catch (NumberFormatException | IOException
					| InvalidNodeAuthException e) {
				e.printStackTrace();
				return null;
			}
		}
		if (req.getParameter("sensorsbynode") != null
				&& req.getParameter("type") == null
				&& req.getParameter("class") == null) {
			Node n = Node.getByUid(Integer.parseInt(req
					.getParameter("sensorsbynode")));
			return n.getLatestSensors();
		} else if (req.getParameter("sensorsbynode") != null
				&& req.getParameter("type") != null
				&& req.getParameter("class") != null) {
			Node n = Node.getByUid(Integer.parseInt(req
					.getParameter("sensorsbynode")));
			return n.getLatestSensors(
					Integer.parseInt(req.getParameter("class")),
					Integer.parseInt(req.getParameter("type")));

		}
		PreparedStatement nodes = DatabaseConnection.getInstance().prepare(
				"SELECT * FROM nodes");
		ResultSet resSet = nodes.executeQuery();
		JSONArray jsonRes = new JSONArray();
		resSet.beforeFirst();
		while (resSet.next()) {
			HashMap<String, Object> res = new HashMap<String, Object>();
			res.put("uid", resSet.getInt("uid") + "");
			res.put("name", resSet.getString("name"));
			res.put("description", resSet.getString("description"));
			jsonRes.put(new JSONObject(res));
		}
		return jsonRes;
	}
}
