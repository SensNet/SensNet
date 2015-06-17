package net.sensnet.plugins.visualizer;

import java.sql.SQLException;
import java.util.Date;

import net.sensnet.node.IndexerHolder;
import net.sensnet.node.SensNetNodeConfiguration;
import net.sensnet.node.pages.api.json.DatapointJSONApiPage;
import net.sensnet.node.plugins.DataVisualizerPlugin;

import org.json.JSONArray;
import org.json.JSONException;

public class ParticleVisualizer extends DataVisualizerPlugin {

	public ParticleVisualizer(SensNetNodeConfiguration configuration)
			throws SQLException {
		super(configuration);
		IndexerHolder.getInstance().put(new ParticleSensor());
	}

	@Override
	public String getSensorName() {
		return "Particle sensor";
	}

	@Override
	public int getSensorType() {
		return 2;
	}

	@Override
	public String getSensorTypeName() {
		return "particlesensnet";
	}

	@Override
	public DatapointJSONApiPage getDatapointJSONApiPage() {
		return new DatapointJSONApiPage() {

			@Override
			public JSONArray getLatestDataNear(Date upperLimit)
					throws JSONException, SQLException {
				return new JSONArray(
						ParticleSensor.getLatestDosesFromAllSensors(upperLimit));
			}

			@Override
			public JSONArray getData(Date from, Date to) throws JSONException,
					SQLException {
				return null;
			}

			@Override
			public JSONArray getData(Date from, Date to, int node)
					throws JSONException, SQLException {
				return null;
			}
		};
	}

	@Override
	public int getSensorClass() {
		return 4;
	}

}
