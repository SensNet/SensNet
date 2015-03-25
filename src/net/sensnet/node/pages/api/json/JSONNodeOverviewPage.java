package net.sensnet.node.pages.api.json;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.InvalidNodeAuthException;
import net.sensnet.node.dbobjects.Node;
import net.sensnet.node.dbobjects.Sensor;

import org.json.JSONArray;
import org.json.JSONException;

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
				return new JSONArray(new String[] { n.getUid() + "",
						n.getName(), n.getDescription() });
			} catch (NumberFormatException | IOException
					| InvalidNodeAuthException e) {
				e.printStackTrace();
				return null;
			}
		}
		if (req.getParameter("sensorsbynode") != null
				&& req.getParameter("type") == null) {
			Node n = Node.getByUid(Integer.parseInt(req
					.getParameter("sensorsbynode")));
			return n.getLatestSensors();
		} else if (req.getParameter("sensorsbynode") != null
				&& req.getParameter("type") != null) {
			Node n = Node.getByUid(Integer.parseInt(req
					.getParameter("sensorsbynode")));
			return n.getLatestSensors(Integer.parseInt(req.getParameter("type")));

		}
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
