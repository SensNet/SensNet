package net.sensnet.node.plugins;

import net.sensnet.node.Page;
import net.sensnet.node.SensNetNodeConfiguration;

public abstract class PagePlugin extends Plugin {
	public PagePlugin(SensNetNodeConfiguration configuration) {
		super(configuration);
	}

	public abstract Page getPage();

	public abstract String getPathName();

}
