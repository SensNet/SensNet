package net.sensnet.plugins.pages.foundation;

import net.sensnet.node.pages.SimpleTextPage;

public class AboutPage extends SimpleTextPage {

	public AboutPage() {
		super("About SensNet");
	}

	@Override
	public boolean needsLogin() {
		return false;
	}

	@Override
	public boolean reallyNeedsLogin() {
		return false;
	}

}
