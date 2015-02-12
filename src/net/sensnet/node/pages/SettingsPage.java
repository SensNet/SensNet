package net.sensnet.node.pages;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.Form;
import net.sensnet.node.Page;

public class SettingsPage extends Page {
	public static final String PATH = "/settings";

	public SettingsPage() {
		super("Settings");
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		if (req.getParameter("registernode") != null) {
			RegisterDirectSubNodeForm form = Form.getForm(req,
					RegisterDirectSubNodeForm.class);

			PrintWriter wr = resp.getWriter();
			if (form.submit(resp.getWriter(), req)) {
				wr.write("<div class=\"container\"><div class=\"alert alert-success\" role=\"alert \"><b><span class=\"glyphicon glyphicon-ok\" aria-hidden=\"true\"></span></b> Node registerd.<br/>The node auth token is <pre>"
						+ form.getToken() + "</pre></div></div>");
			} else {
				wr.write("<div class=\"container\"><div class=\"alert alert-danger\" role=\"alert \"><b><span class=\"glyphicon glyphicon-warning-sign\" aria-hidden=\"true\"></span></b> Please check the inserted data.</div></div>");
			}
		}
		super.doPost(req, resp);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		HashMap<String, Object> vars = new HashMap<String, Object>();
		vars.put("generalSettingsFormNode", new GeneralSettingsFormNode(req));
		vars.put("generalSettingsFormSuper", new GeneralSettingsFormSuper(req));
		vars.put("registerNodeForm", new RegisterDirectSubNodeForm(req));
		getDefaultTemplate().output(resp.getWriter(), vars);
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
