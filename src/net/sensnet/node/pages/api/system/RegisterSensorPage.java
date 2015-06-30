package net.sensnet.node.pages.api.system;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.InvalidNodeAuthException;
import net.sensnet.node.dbobjects.Node;
import net.sensnet.node.dbobjects.Sensor;

public class RegisterSensorPage extends APIPage {
	public static final String PATH = "/api/register/sensor";
	
	public RegisterSensorPage(String name) {
		super(name);
	}

	@Override
	public void doAction(HttpServletRequest req, HttpServletResponse resp)
			throws SQLException, IOException, InvalidNodeAuthException {
		Sensor sensor = new Sensor(Integer.parseInt(req.getParameter("uid")),
				Node.getByUid(Integer.parseInt(req.getParameter("node"))));
		sensor.commit();
	}

	@Override
	public boolean reallyNeedsLogin() {
		return false;
	}

}
