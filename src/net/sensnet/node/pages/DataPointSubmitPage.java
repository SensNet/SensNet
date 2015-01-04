package net.sensnet.node.pages;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.InvalidNodeAuthException;
import net.sensnet.node.dbobjects.DataPoint;

public class DataPointSubmitPage extends APIPage {
	public static final String PATH = "/api/submit/datapoint";
	public DataPointSubmitPage(String name) {
		super(name);
	}

	@Override
	public void doAction(HttpServletRequest req, HttpServletResponse resp)
			throws SQLException, IOException, InvalidNodeAuthException {
		DataPoint dp = new DataPoint(req);
		dp.commit();
	}

	@Override
	public boolean reallyNeedsLogin() {
		return false;
	}

}
