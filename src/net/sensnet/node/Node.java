package net.sensnet.node;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.pages.AuthAPIPage;
import net.sensnet.node.pages.DataPointSubmitPage;
import net.sensnet.node.pages.MainPage;
import net.sensnet.node.pages.MapPage;
import net.sensnet.node.pages.NodesOverviewPage;
import net.sensnet.node.pages.RegisterSensorPage;
import net.sensnet.node.pages.TypeDumpPage;

import org.cacert.gigi.output.template.Outputable;
import org.cacert.gigi.output.template.Template;

public class Node extends HttpServlet {
	private HashMap<String, Page> mapping = new HashMap<>();
	private Page mainPage = new MainPage("Node");
	private Template mainTemplate;

	@Override
	public void init() throws ServletException {
		super.init();
		mainTemplate = new Template(Node.class.getResource("Node.templ"));
		mapping.put("/auth", new AuthAPIPage("Auth API"));
		mapping.put("/typedump", new TypeDumpPage(""));
		mapping.put("/map", new MapPage("Map"));
		mapping.put(RegisterSensorPage.PATH, new RegisterSensorPage("Sensor"));
		mapping.put(DataPointSubmitPage.PATH, new DataPointSubmitPage(
				"Datapoint submit"));
		mapping.put(NodesOverviewPage.PATH, new NodesOverviewPage("Nodes"));
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp, false);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp, true);
	}

	private void handleRequest(final HttpServletRequest req,
			final HttpServletResponse resp, final boolean post)
			throws IOException {
		String pathInfo = req.getPathInfo();
		resp.setContentType("text/html; charset=utf-8");
		HashMap<String, Object> vars = new HashMap<String, Object>();
		final Page p;
		if (pathInfo == null || pathInfo == "/") {
			p = mainPage;
		} else {
			if (mapping.containsKey(pathInfo)) {
				p = mapping.get(pathInfo);
			} else {
				p = null;
				resp.sendError(404);
				return;
			}
		}
		if (p.needsLogin()
				&& SensNetNodeConfiguration.getInstance().isLoginRequired()) {
			resp.sendError(403);
			return;
		}
		if (p.needsTemplate()) {
			Outputable contnet = new Outputable() {

				@Override
				public void output(PrintWriter out, Map<String, Object> vars) {
					try {
						if (post) {
							p.doPost(req, resp);
						} else {
							p.doGet(req, resp);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			vars.put(p.getName(), "");
			vars.put("content", contnet);
			vars.put("year", Calendar.getInstance().get(Calendar.YEAR));
			vars.put("title", p.getName());
			try {
				vars.put("mynodename", "#"
						+ SensNetNodeConfiguration.getInstance().getNodeID()
						+ ": "
						+ SensNetNodeConfiguration.getInstance().getNodeName());
			} catch (SQLException e) {
			}
			mainTemplate.output(resp.getWriter(), vars);
		} else if (post) {
			p.doPost(req, resp);
		} else {
			p.doGet(req, resp);
		}
	}
}
