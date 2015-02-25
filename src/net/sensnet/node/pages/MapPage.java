package net.sensnet.node.pages;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.Page;
import net.sensnet.node.pages.sensors.RadioDosePage;

import org.cacert.gigi.output.template.IterableDataset;

public class MapPage extends Page {
	public static final String PATH = "/map";

	public MapPage(String name) {
		super(name);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException {
		try {
			PreparedStatement prep = DatabaseConnection
					.getInstance()
					.prepare(
							"select * from datapoints a where not exists ( select * from datapoints b where b.`from` = a.`from` and a.`received` < b.`received`)");
			final ResultSet res = prep.executeQuery();
			vars.put("sensors", new IterableDataset() {

				@Override
				public boolean next(Map<String, Object> vars) {
					try {
						if (res.next()) {
							vars.put("lat", RadioDosePage.makeCoordinate(res
									.getInt("locationlat")));
							vars.put("long", RadioDosePage.makeCoordinate(res
									.getInt("locationlong")));
							vars.put("sensorid", res.getInt("from"));
							return true;
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return false;
				}
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}

		getDefaultTemplate().output(resp.getWriter(), vars);
	}

	@Override
	public boolean needsLogin() {
		return true;
	}

	@Override
	public boolean needsTemplate() {
		return true;
	}

	@Override
	public boolean reallyNeedsLogin() {
		return false;
	}

}
