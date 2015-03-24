package net.sensnet.node.plugins;

import net.sensnet.node.SensNetNodeConfiguration;
import net.sensnet.node.pages.api.json.DatapointJSONApiPage;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;

public abstract class VisualizerPlugin extends Plugin {

	public VisualizerPlugin(SensNetNodeConfiguration configuration) {
		super(configuration);
	}

	public abstract String getSensorName();

	public abstract int getSensorType();

	public abstract Template getTemplate();

	public abstract DatapointJSONApiPage getDatapointJSONApiPage();

}
