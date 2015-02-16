package net.sensnet.node.pages.sensors;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.Page;
import net.sensnet.node.dbobjects.sensors.RadioDoseSensor;
import net.sensnet.node.pages.NodesOverviewPage;

import org.cacert.gigi.output.template.IterableDataset;

public class RadioDosePage extends Page {
	public static final String PATH = "/sensors/radio/dose";

	public RadioDosePage() {
		super("Radiodose sensor");
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException {
		try {
			final int[][] data = RadioDoseSensor.getDoses(
					new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24),
					new Date(System.currentTimeMillis()));
			vars.put("sensdata", new IterableDataset() {
				int i = 0;

				@Override
				public boolean next(Map<String, Object> vars) {
					if (data == null || data.length == i) {
						return false;
					}
					int[] dat = data[i++];
					vars.put("lt", makeCoordinate(dat[0]));
					vars.put("lg", makeCoordinate(dat[1]));
					vars.put("val", dat[2]);
					return true;
				}
			});
		} catch (SQLException e) {
			e.printStackTrace();
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

	private static String makeCoordinate(int coord) {
		String lng = NodesOverviewPage.ammendZero(coord + "", 8);
		lng = lng.substring(0, lng.length() - 7) + "."
				+ lng.substring(lng.length() - 7, lng.length());
		return lng;
	}
}
