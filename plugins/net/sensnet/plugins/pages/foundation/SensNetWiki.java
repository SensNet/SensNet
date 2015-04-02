package net.sensnet.plugins.pages.foundation;

import net.sensnet.node.Page;
import net.sensnet.node.SensNetNodeConfiguration;
import net.sensnet.node.pages.LinkPage;
import net.sensnet.node.plugins.PagePlugin;

public class SensNetWiki extends PagePlugin {
	private LinkPage page;

	public SensNetWiki(SensNetNodeConfiguration configuration) {
		super(configuration);
		page = new LinkPage("Wiki", "//wiki.sensnet.net") {
		};
	}

	@Override
	public Page getPage() {
		return page;
	}

	@Override
	public String getPathName() {
		return "wiki";
	}

}
