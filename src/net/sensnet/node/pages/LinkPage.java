package net.sensnet.node.pages;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.Page;

public abstract class LinkPage extends Page {
	private String link;

	public LinkPage(String name, String link) {
		super(name);
		this.link = link;
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException {
		resp.sendRedirect(link);
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
		return false;
	}
}
