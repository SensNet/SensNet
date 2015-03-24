package net.sensnet.node.plugins;

import net.sensnet.node.SensNetNodeConfiguration;

public abstract class HardwareInputPlugin extends Plugin implements Runnable {
	public HardwareInputPlugin(SensNetNodeConfiguration configuration) {
		super(configuration);
	}

}
