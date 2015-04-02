package net.sensnet.node.pages.api.json;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;

public class ServerStatus extends JSONApiPage {
	public static final String PATH = "/api/json/serverstatus";

	@Override
	public JSONArray getData(HttpServletRequest req) throws JSONException,
			SQLException {
		return new JSONArray(new String[] { System.currentTimeMillis() / 1000
				+ "" });
	}

}
