package net.sensnet.node;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.dbobjects.User;

import org.cacert.gigi.output.template.Template;

public abstract class Page {
	private Template defaultTemplate;
	private String name;

	public Page(String name) {
		this.name = name;
		if (needsTemplate()) {
			URL resource = getClass().getResource(
					getClass().getSimpleName() + ".templ");
			if (resource != null) {
				defaultTemplate = new Template(resource);
			}
		}
	}

	/**
	 * By default, {@link #doGet()} is called.
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doGet(req, resp);
	}

	public abstract void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException;

	public String getName() {
		return name;
	}

	public Template getDefaultTemplate() {
		return defaultTemplate;
	}

	public abstract boolean needsLogin();

	public abstract boolean needsTemplate();

	public static boolean isLoggedIn(HttpServletRequest req) {
		return req.getSession().getAttribute("user") != null;
	}

	public static User getUser(HttpServletRequest req) {
		return (User) req.getSession().getAttribute("user");
	}
}
