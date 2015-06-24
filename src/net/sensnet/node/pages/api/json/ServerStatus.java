package net.sensnet.node.pages.api.json;

import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerStatus extends JSONApiPage {
	public static final String PATH = "/api/json/serverstatus";

	@Override
	public JSONArray getData(HttpServletRequest req) throws JSONException,
			SQLException {
		HashMap<String, Object> obj = new HashMap<String, Object>();
		obj.put("systemtime", System.currentTimeMillis() / 1000 + "");
		JSONObject o = new JSONObject(obj);
		return new JSONArray(new JSONObject[] { o });
	}
}
