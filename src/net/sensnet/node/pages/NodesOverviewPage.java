package net.sensnet.node.pages;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.Page;
import net.sensnet.node.SensNetNodeConfiguration;

import org.cacert.gigi.output.template.IterableDataset;

public class NodesOverviewPage extends Page {
	public static final String PATH = "/nodes";
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss z");
	static {
		DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public NodesOverviewPage(String name) {
		super(name);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException {
		try {
			PreparedStatement nodes = DatabaseConnection.getInstance().prepare(
					"SELECT * FROM nodes");
			final ResultSet nodeSet = nodes.executeQuery();
			vars.put("nodes", new IterableDataset() {
				boolean first = true;

				@Override
				public boolean next(Map<String, Object> vars) {
					try {
						String sensname = null;
						String sensdescr = null;
						int receiver = 0;
						if (first) {
							first = false;
							sensname = SensNetNodeConfiguration.getInstance()
									.getNodeName() + " (this node)";
							sensdescr = SensNetNodeConfiguration.getInstance()
									.getNodeDescription();
							receiver = SensNetNodeConfiguration.getInstance()
									.getNodeID();
						} else if (nodeSet.next()) {
							sensname = nodeSet.getString("name");
							sensdescr = nodeSet.getString("description");
							receiver = nodeSet.getInt("uid");
						} else {
							return false;
						}
						vars.put("sensname", sensname);
						vars.put("sensdescr", sensdescr);
						PreparedStatement prep = DatabaseConnection
								.getInstance()
								.prepare(
										"select * from datapoints a where not exists ( select * from datapoints b where b.`from` = a.`from` and a.`received` < b.`received` and b.`receivernode` = ?) and a.`receivernode` = ?");
						prep.setInt(1, receiver);
						prep.setInt(2, receiver);
						final ResultSet res = prep.executeQuery();
						vars.put("sensors", new IterableDataset() {

							@Override
							public boolean next(Map<String, Object> vars) {
								try {
									if (res.next()) {
										vars.put(
												"lastseen",
												DATE_FORMAT.format(new Date(
														res.getInt("received") * 1000)));
										vars.put("sensorid", res.getInt("from"));
										vars.put("sensorbattery",
												res.getInt("battery") + " %");
										String lat = res.getInt("locationlat")
												+ "";
										lat = ammendZero(lat, 8);
										lat = lat.substring(0, lat.length() - 7)
												+ "."
												+ lat.substring(
														lat.length() - 7,
														lat.length());
										vars.put("lat", lat);
										String lng = res.getInt("locationlong")
												+ "";
										lng = ammendZero(lng, 8);
										lng = lng.substring(0, lng.length() - 7)
												+ "."
												+ lng.substring(
														lng.length() - 7,
														lng.length());
										vars.put("long", lng);
										return true;
									}
								} catch (SQLException e) {
									e.printStackTrace();
								}
								return false;
							}

						});
						return true;
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

	public static String ammendZero(String lat, int i) {
		while (lat.length() < i) {
			lat += "0";
		}
		return lat;
	}

	@Override
	public boolean reallyNeedsLogin() {
		return false;
	}

}
