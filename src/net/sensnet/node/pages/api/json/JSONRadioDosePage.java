package net.sensnet.node.pages.api.json;

import java.sql.SQLException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sensnet.node.dbobjects.sensors.RadioDoseSensor;

import org.json.JSONArray;
import org.json.JSONException;

public class JSONRadioDosePage extends JSONApiPage {
	public static final String PATH = "/api/json/sensors/radio/dose";

	@Override
	public JSONArray getData(HttpServletRequest req) throws JSONException,
			SQLException {
		Date from = new Date(Long.parseLong(req.getParameter("from")) * 1000);
		Date to = new Date(Long.parseLong(req.getParameter("to")) * 1000);
		int[][] res;
		if (req.getParameter("node") != null) {
			res = RadioDoseSensor.getDoses(from, to,
					Integer.parseInt(req.getParameter("node")));
		} else {
			res = RadioDoseSensor.getDoses(from, to);
		}
		return new JSONArray(res);
	}

}