package net.sensnet.node.pages;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.Page;

import org.cacert.gigi.output.template.IterableDataset;

public class MapPage extends Page {
	public static final String PATH = "/map";

	public MapPage(String name) {
		super(name);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		HashMap<String, Object> vars = new HashMap<String, Object>();
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
							String lat = res.getInt("locationlat") + "";
							lat = NodesOverviewPage.ammendZero(lat, 8);
							lat = lat.substring(0, lat.length() - 7)
									+ "."
									+ lat.substring(lat.length() - 7,
											lat.length());
							vars.put("lat", lat);
							String lng = res.getInt("locationlong") + "";
							lng = NodesOverviewPage.ammendZero(lng, 8);
							lng = lng.substring(0, lng.length() - 7)
									+ "."
									+ lng.substring(lng.length() - 7,
											lng.length());
							vars.put("long", lng);
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

		getDefaultTemplate().output(resp.getWriter(), vars
				);
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
