package net.sensnet.node.pages.api.json;

import java.sql.SQLException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;

public abstract class DatapointJSONApiPage extends JSONApiPage {

	@Override
	public JSONArray getData(HttpServletRequest req) throws JSONException,
			SQLException {
		if (req.getParameter("near") != null) {
			Date near;
			if (req.getParameter("near").equals("now")) {
				near = new Date();
			} else {
				near = new Date(Long.parseLong(req.getParameter("near")) * 1000);
			}
			return getLatestDataNear(near);
		} else {
			Date from = new Date(
					Long.parseLong(req.getParameter("from")) * 1000);
			Date to;
			int node = -1;
			if (req.getParameter("to").equals("now")) {
				to = new Date(System.currentTimeMillis());
			} else {
				to = new Date(Long.parseLong(req.getParameter("to")) * 1000);
			}
			if (req.getParameter("node") != null) {
				node = Integer.parseInt(req.getParameter("node"));
			}
			if (node != -1) {
				return getData(from, to, node);
			} else {
				return getData(from, to);
			}
		}
	}

	public abstract JSONArray getData(Date from, Date to, int node)
			throws JSONException, SQLException;

	public abstract JSONArray getData(Date from, Date to) throws JSONException,
			SQLException;

	public abstract JSONArray getLatestDataNear(Date upperLimit)
			throws JSONException, SQLException;
}
