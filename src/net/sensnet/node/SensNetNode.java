package net.sensnet.node;

import java.io.IOException;
import java.io.PrintWriter;
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
import net.sensnet.node.pages.RegisterNodePage;
import net.sensnet.node.pages.RegisterSensorPage;
import net.sensnet.node.pages.SettingsPage;
import net.sensnet.node.pages.sensors.RadioDosePage;

import org.cacert.gigi.output.template.Outputable;
import org.cacert.gigi.output.template.Template;

public class SensNetNode extends HttpServlet {
	private HashMap<String, Page> mapping = new HashMap<>();
	private Page mainPage = new MainPage("Login");
	private Template mainTemplate;

	@Override
	public void init() throws ServletException {
		super.init();
		mainTemplate = new Template(SensNetNode.class.getResource("Node.templ"));
		mapping.put("/auth", new AuthAPIPage("Auth API"));
		mapping.put("/map", new MapPage("Map"));
		mapping.put(RegisterSensorPage.PATH, new RegisterSensorPage("Sensor"));
		mapping.put(DataPointSubmitPage.PATH, new DataPointSubmitPage(
				"Datapoint submit"));
		mapping.put(MainPage.PATH, mainPage);
		mapping.put(NodesOverviewPage.PATH, new NodesOverviewPage("Nodes"));
		mapping.put(RegisterNodePage.PATH, new RegisterNodePage("RgisterNode"));
		mapping.put(SettingsPage.PATH, new SettingsPage());
		mapping.put(RadioDosePage.PATH, new RadioDosePage());
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
		resp.setHeader("Strict-Transport-Security", "max-age=" + 60 * 60 * 24
				* 366 + "; includeSubDomains; preload");
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
		if (((p.needsLogin() && SensNetNodeConfiguration.getInstance()
				.isLoginRequired()) || p.reallyNeedsLogin())
				&& !MainPage.isLoggedIn(req)) {
			req.getSession().setAttribute("redirOrig", pathInfo);
			resp.sendRedirect("/login");
			return;
		}
		if (p.needsTemplate()) {
			Outputable content = new Outputable() {

				@Override
				public void output(PrintWriter out, Map<String, Object> vars) {
					try {
						if (post) {
							p.doPost(req, resp, vars);
						} else {
							p.doGet(req, resp, vars);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			vars.put(p.getName(), "");
			vars.put("content", content);
			vars.put("year", Calendar.getInstance().get(Calendar.YEAR));
			vars.put("title", p.getName());
			vars.put("isRootNode", SensNetNodeConfiguration.getInstance()
					.isRootNode());

			vars.put("mynodename", "#"
					+ SensNetNodeConfiguration.getInstance().getNodeID() + ": "
					+ SensNetNodeConfiguration.getInstance().getNodeName());
			mainTemplate.output(resp.getWriter(), vars);
		} else if (post) {
			p.doPost(req, resp, vars);
		} else {
			p.doGet(req, resp, vars);
		}
	}
}
