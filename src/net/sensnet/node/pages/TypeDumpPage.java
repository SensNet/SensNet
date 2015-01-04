package net.sensnet.node.pages;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.Launcher;
import net.sensnet.node.dbobjects.SensorType;

public class TypeDumpPage extends APIPage {

	public TypeDumpPage(String name) {
		super(name);
	}

	@Override
	public void doAction(HttpServletRequest req, HttpServletResponse resp)
			throws SQLException, IOException {
		HashMap<Integer, SensorType> types = Launcher.getSensorTypes();
		PrintWriter writer = resp.getWriter();
		Iterator<SensorType> iter = types.values().iterator();
		while (iter.hasNext()) {
			writer.println(iter.next().toString());
		}
	}

}
