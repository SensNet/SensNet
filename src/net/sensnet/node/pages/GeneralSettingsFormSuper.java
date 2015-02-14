package net.sensnet.node.pages;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sensnet.node.Form;
import net.sensnet.node.SensNetNodeConfiguration;

import org.cacert.gigi.output.template.Template;

public class GeneralSettingsFormSuper extends Form {
	private static Template t;

	static {
		t = new Template(
				GeneralSettingsFormSuper.class
						.getResource("GeneralSettingsFormSuper.templ"));
	}

	public GeneralSettingsFormSuper(HttpServletRequest hsr) {
		super(hsr);
	}

	@Override
	public boolean submit(PrintWriter out, HttpServletRequest req) {
		String description = req.getParameter("owndescription");
		String uid = req.getParameter("ownuid");
		String name = req.getParameter("ownname");
		if (uid != null && !uid.trim().isEmpty() && description != null
				&& !description.trim().isEmpty() && description != null
				&& !description.trim().isEmpty() && name != null
				&& !name.trim().isEmpty()
				&& SensNetNodeConfiguration.getInstance().isRootNode()) {
			try {
				SensNetNodeConfiguration.getInstance().setNodeUID(
						Integer.parseInt(uid));
				SensNetNodeConfiguration.getInstance().setNodeName(name);
				SensNetNodeConfiguration.getInstance().setNodeDescription(
						description);
			} catch (NumberFormatException e) {
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	protected void outputContent(PrintWriter out, Map<String, Object> vars) {
		t.output(out, vars);
	}

}
