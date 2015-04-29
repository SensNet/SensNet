package net.sensnet.plugins.pages.foundation;

import net.sensnet.node.Page;
import net.sensnet.node.SensNetNodeConfiguration;
import net.sensnet.node.plugins.PagePlugin;

public class AboutPagePlugin extends PagePlugin {
	public AboutPagePlugin(SensNetNodeConfiguration configuration) {
		super(configuration);
	}

	@Override
	public Page getPage() {
		return new AboutPage();
	}

	@Override
	public String getPathName() {
		return "about";
	}

}
