package net.sensnet.node.pages.api.json;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import net.sensnet.node.dbobjects.sensors.BLEGasSensor;

import org.json.JSONArray;
import org.json.JSONException;

public class BLEGasPhaseShiftApiPage extends JSONApiPage {
	public static final String PATH = "/api/json/sensors/gas/phaseshift";

	@Override
	public JSONArray getData(HttpServletRequest req) throws JSONException,
			SQLException {
		int sensor = Integer.parseInt(req.getParameter("sensor"));
		int limit = Integer.parseInt(req.getParameter("limit"));

		return new JSONArray(BLEGasSensor.getLatestShifts(sensor, limit));
	}

}
