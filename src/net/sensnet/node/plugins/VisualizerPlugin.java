package net.sensnet.node.plugins;

import net.sensnet.node.SensNetNodeConfiguration;
import net.sensnet.node.pages.api.json.DatapointJSONApiPage;

import org.cacert.gigi.output.template.Template;

public abstract class VisualizerPlugin extends Plugin {
	private Template defaultTemplate;

	public VisualizerPlugin(SensNetNodeConfiguration configuration) {
		super(configuration);
		try {
			defaultTemplate = new Template(getClass().getResource(
					getClass().getSimpleName() + ".templ"));
		} catch (Exception e) {
			getLoger().info("No fitting template found.");
		}
	}

	public abstract String getSensorName();

	public abstract int getSensorType();

	public Template getTemplate() {
		return defaultTemplate;
	}

	public abstract DatapointJSONApiPage getDatapointJSONApiPage();

}
