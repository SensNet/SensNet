package net.sensnet.node.pages.api.json;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.Page;

import org.json.JSONArray;
import org.json.JSONException;

public abstract class JSONApiPage extends Page {

	public JSONApiPage() {
		super(JSONApiPage.class.getName());
	}

	public abstract JSONArray getData(HttpServletRequest req)
			throws JSONException, SQLException;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException {
		try {
			JSONArray obj = getData(req);
			if (obj != null) {
				resp.getWriter().println(getData(req).toString());
			} else {
				resp.getWriter().println(new JSONArray());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean needsTemplate() {
		return false;
	}

	@Override
	public boolean needsLogin() {
		return false;
	}

	@Override
	public boolean reallyNeedsLogin() {
		return false;
	}

}
