package net.sensnet.node.plugins;

import net.sensnet.node.SensNetNodeConfiguration;

public abstract class Plugin {
	public SensNetNodeConfiguration configuration;

	public Plugin(SensNetNodeConfiguration configuration) {
		this.configuration = configuration;
	}

	public SensNetNodeConfiguration getConfiguration() {
		return configuration;
	}
}
