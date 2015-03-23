package net.sensnet.node.plugins;

import net.sensnet.node.SensNetNodeConfiguration;

public abstract class HardwareInput extends Plugin implements Runnable {
	public HardwareInput(SensNetNodeConfiguration configuration) {
		super(configuration);
	}

}
