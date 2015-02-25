package net.sensnet.node.pages.api.json;

import java.sql.SQLException;
import java.util.Date;

import net.sensnet.node.dbobjects.sensors.RadioDoseSensor;

import org.json.JSONArray;
import org.json.JSONException;

public class JSONRadioDosePage extends DatapointJSONApiPage {
	public static final String PATH = "/api/json/sensors/radio/dose";

	@Override
	public JSONArray getData(Date from, Date to, int node)
			throws JSONException, SQLException {
		return new JSONArray(RadioDoseSensor.getDoses(from, to, node));
	}

	@Override
	public JSONArray getData(Date from, Date to) throws JSONException,
			SQLException {
		return new JSONArray(RadioDoseSensor.getDoses(from, to));
	}

}
