package net.sensnet.node.pages;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.Form;
import net.sensnet.node.Page;

public class RegisterDirectSubNodePage extends Page {
	public static final String PATH = "/noderegister";
	public RegisterDirectSubNodePage(String name) {
		super(name);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Form f = new RegisterDirectSubNodeForm(req);
		f.output(resp.getWriter(), new HashMap<String, Object>());

	}

	@Override
	public boolean needsLogin() {
		return true;
	}

	@Override
	public boolean reallyNeedsLogin() {
		return true;
	}

	@Override
	public boolean needsTemplate() {
		return true;
	}

}
