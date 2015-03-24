package net.sensnet.node.pages;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensnet.node.Page;
import net.sensnet.node.plugins.DataVisualizerPlugin;

public class PluginPage extends Page {
	private DataVisualizerPlugin plugin;

	public PluginPage(DataVisualizerPlugin plugin) {
		super(plugin.getSensorName());
		this.plugin = plugin;
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException {
		plugin.getTemplate().output(resp.getWriter(), vars);
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
		return true;
	}

}
