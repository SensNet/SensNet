package net.sensnet.node.plugins;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.Page;
import net.sensnet.node.SensNetNodeConfiguration;

public abstract class PlainPagePlugin extends Plugin {

	private Page p;

	public PlainPagePlugin(SensNetNodeConfiguration configuration) {
		super(configuration);
		p = new Page("") {

			@Override
			public boolean reallyNeedsLogin() {
				return false;
			}

			@Override
			public boolean needsTemplate() {
				return false;
			}

			@Override
			public boolean needsLogin() {
				return false;
			}

			@Override
			public void doGet(HttpServletRequest req, HttpServletResponse resp,
					Map<String, Object> vars) throws IOException {
				doGet(req, resp, vars);
			}
		};

	}

	public abstract String getPageName();

	public abstract void doGet(HttpServletRequest req,
			HttpServletResponse resp, Map<String, Object> vars)
			throws IOException;

	public Page getPage() {
		return p;
	}

	public abstract String getPath();
}
