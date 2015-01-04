package net.sensnet.node.pages;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.Form;
import net.sensnet.node.Page;
import net.sensnet.node.SensNetNodeConfiguration;

public class MainPage extends Page {

	public MainPage(String name) {
		super(name);
	}
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		if (!isLoggedIn(req)
				&& SensNetNodeConfiguration.getInstance().isLoginRequired()) {
			LoginForm f = new LoginForm(req);
			HashMap<String, Object> vars = new HashMap<String, Object>();
			f.output(resp.getWriter(), vars);
		} else {
			resp.sendRedirect(MapPage.PATH);
		}
	}
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		LoginForm lg = Form.getForm(req, LoginForm.class);
		if (!lg.submit(resp.getWriter(), req)) {
			resp.getWriter().println("Invalid Login");
			doGet(req, resp);
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
