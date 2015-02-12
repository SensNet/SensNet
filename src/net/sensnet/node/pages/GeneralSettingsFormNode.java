package net.sensnet.node.pages;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sensnet.node.Form;

import org.cacert.gigi.output.template.Template;

public class GeneralSettingsFormNode extends Form {
	private static Template t;
	static {
		t = new Template(
				GeneralSettingsFormNode.class
						.getResource("GeneralSettingsFormNode.templ"));
	}

	public GeneralSettingsFormNode(HttpServletRequest hsr) {
		super(hsr);
	}

	@Override
	public boolean submit(PrintWriter out, HttpServletRequest req) {
		return false;
	}

	@Override
	protected void outputContent(PrintWriter out, Map<String, Object> vars) {
		t.output(out, vars);
	}

}