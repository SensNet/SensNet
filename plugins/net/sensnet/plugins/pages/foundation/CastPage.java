package net.sensnet.plugins.pages.foundation;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.Page;
import net.sensnet.node.SensNetNodeConfiguration;
import net.sensnet.node.pages.NodesOverviewPage;
import net.sensnet.node.plugins.PlainPagePlugin;

import org.cacert.gigi.output.template.Outputable;
import org.cacert.gigi.output.template.Template;

public class CastPage extends PlainPagePlugin {
	private static Template templ;
	private Page nodePage;
	static {
		templ = new Template(CastPage.class.getResource("CastPage.templ"));
	}

	public CastPage(SensNetNodeConfiguration configuration) {
		super(configuration);
		nodePage = new NodesOverviewPage("ov");
	}

	@Override
	public String getPageName() {
		return "castRceiver";
	}

	@Override
	public void doGet(final HttpServletRequest req,
			final HttpServletResponse resp, Map<String, Object> vars)
			throws IOException {
		vars.put("content", new Outputable() {

			@Override
			public void output(PrintWriter out, Map<String, Object> vars) {
				try {
					nodePage.doGet(req, resp, vars);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		templ.output(resp.getWriter(), vars);
	}

	@Override
	public String getPath() {
		return "castApp";
	}

}
