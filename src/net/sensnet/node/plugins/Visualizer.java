package net.sensnet.node.plugins;

import net.sensnet.node.SensNetNodeConfiguration;
import net.sensnet.node.pages.api.json.DatapointJSONApiPage;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;

public abstract class Visualizer extends Plugin {

	public Visualizer(SensNetNodeConfiguration configuration) {
		super(configuration);
	}

	public abstract String getSensorName();

	public abstract int getSensorType();

	public abstract Template getTemplate();

	public abstract DatapointJSONApiPage getDatapointJSONApiPage();

}
