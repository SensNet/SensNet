package net.sensnet.node.pages;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.Page;

public abstract class SimpleTextPage extends Page {
	public SimpleTextPage(String name) {
		super(name);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException {
		getDefaultTemplate().output(resp.getWriter(), vars);
	}

	@Override
	public boolean needsTemplate() {
		return true;
	}

}
