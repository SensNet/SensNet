package net.sensnet.node.pages.sensors;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.Page;
import net.sensnet.node.SensNetNodeConfiguration;
import net.sensnet.node.dbobjects.sensors.BLEGasSensor;
import net.sensnet.node.pages.NodesOverviewPage;

import org.cacert.gigi.output.template.IterableDataset;

public class GasPhasePage extends Page {
	public static final String PATH = "/sensors/gas";

	public GasPhasePage() {
		super("Gas pahse shift");
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException {
		try {
			final int[][] data = BLEGasSensor.getLatestShifts(20);
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
										"select * from datapoints a where not exists ( select * from datapoints b where b.`from` = a.`from` and a.`received` < b.`received` and b.`receivernode` = ?) and a.`receivernode` = ? and `type`='1'");
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
												NodesOverviewPage.DATE_FORMAT.format(new Date(
														res.getLong("received") * 1000)));
										vars.put("sensorid", res.getInt("from"));
										vars.put("sensorbattery",
												res.getInt("battery") + " %");
										vars.put("lat", RadioDosePage
												.makeCoordinate(res
														.getInt("locationlat")));
										vars.put(
												"long",
												RadioDosePage.makeCoordinate(res
														.getInt("locationlong")));
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
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		getDefaultTemplate().output(resp.getWriter(), vars);
	}

	@Override
	public boolean needsLogin() {
		return false;
	}

	@Override
	public boolean reallyNeedsLogin() {
		return false;
	}

	@Override
	public boolean needsTemplate() {
		return true;
	}

}
