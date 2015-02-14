package net.sensnet.node.pages;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sensnet.node.Form;
import net.sensnet.node.SensNetNodeConfiguration;

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
		String superNode = req.getParameter("super");
		String token = req.getParameter("token");
		if (token != null && !token.trim().isEmpty() && superNode != null
				&& !token.trim().isEmpty()) {
			SensNetNodeConfiguration.getInstance().setSuperNode(superNode);
			SensNetNodeConfiguration.getInstance().setSuperNodeAuth(token);
			return true;
		}
		return false;
	}

	@Override
	protected void outputContent(PrintWriter out, Map<String, Object> vars) {
		t.output(out, vars);
	}

}
