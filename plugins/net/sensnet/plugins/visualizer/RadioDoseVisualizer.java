package net.sensnet.plugins.visualizer;

import java.sql.SQLException;
import java.util.Date;

import net.sensnet.node.SensNetNodeConfiguration;
import net.sensnet.node.dbobjects.sensors.RadioDoseSensor;
import net.sensnet.node.pages.api.json.DatapointJSONApiPage;
import net.sensnet.node.plugins.DataVisualizerPlugin;

import org.json.JSONArray;
import org.json.JSONException;

public class RadioDoseVisualizer extends DataVisualizerPlugin {
	private DatapointJSONApiPage apiPage;

	public RadioDoseVisualizer(SensNetNodeConfiguration configuration) {
		super(configuration);
		apiPage = new DatapointJSONApiPage() {

			@Override
			public JSONArray getData(Date from, Date to, int node)
					throws JSONException, SQLException {
				return new JSONArray(RadioDoseSensor.getDoses(from, to, node));
			}

			@Override
			public JSONArray getData(Date from, Date to) throws JSONException,
					SQLException {
				return new JSONArray(RadioDoseSensor.getDoses(from, to));
			}
		};
	}

	@Override
	public String getSensorName() {
		return "Radio dose";
	}

	@Override
	public int getSensorType() {
		return 3;
	}

	@Override
	public DatapointJSONApiPage getDatapointJSONApiPage() {
		return apiPage;
	}

	@Override
	public String getSensorTypeName() {
		return "radiodose";
	}

}
