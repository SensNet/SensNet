package net.sensnet.node.pages;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.Page;
import net.sensnet.node.SensNetNodeConfiguration;

import org.cacert.gigi.output.template.Form;

public class MainPage extends Page {
	public static final String PATH = "/login";

	public MainPage(String name) {
		super(name);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException {
		if (!isLoggedIn(req)
				&& (SensNetNodeConfiguration.getInstance().isLoginRequired() || req
						.getPathInfo().equals(PATH))) {
			LoginForm f = new LoginForm(req);
			f.output(resp.getWriter(), vars);
		} else {
			resp.sendRedirect(MapPage.PATH);
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException {
		LoginForm lg = Form.getForm(req, LoginForm.class);
		
		String registerResponse = lg.register(resp.getWriter(), req);
		if (registerResponse != null) {
			resp.getWriter().println(registerResponse);
			doGet(req, resp, vars);
		}
		
		else {
			if (!lg.submit(resp.getWriter(), req)) {
				resp.getWriter().println("Invalid Login");
				doGet(req, resp, vars);
			} else {
				String redir = (String) req.getSession().getAttribute("redirOrig");
				if (redir == null) {
					redir = "/";
				} else {
					req.getSession().setAttribute("redirOrig", null);
				}
				resp.sendRedirect(redir);
			}
		}
	}

	@Override
	public boolean needsTemplate() {
		return true;
	}

	@Override
	public boolean needsLogin() {
		return false;
	}

	@Override
	public boolean reallyNeedsLogin() {
		return false;
	}

}
