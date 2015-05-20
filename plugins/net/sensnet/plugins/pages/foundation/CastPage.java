package net.sensnet.plugins.pages.foundation;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.SensNetNodeConfiguration;
import net.sensnet.node.plugins.PlainPagePlugin;

import org.cacert.gigi.output.template.Template;

public class CastPage extends PlainPagePlugin {
	private static Template templ;

	static {
		templ = new Template(CastPage.class.getResource("CastPage.templ"));
	}

	public CastPage(SensNetNodeConfiguration configuration) {
		super(configuration);
	}

	@Override
	public String getPageName() {
		return "castRceiver";
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException {
		templ.output(resp.getWriter(), vars);
	}

	@Override
	public String getPath() {
		return "castApp";
	}

}
