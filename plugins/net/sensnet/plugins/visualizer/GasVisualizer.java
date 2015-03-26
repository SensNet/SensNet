package net.sensnet.plugins.visualizer;

import java.sql.SQLException;
import java.util.Date;

import net.sensnet.node.IndexizerHolder;
import net.sensnet.node.SensNetNodeConfiguration;
import net.sensnet.node.pages.api.json.DatapointJSONApiPage;
import net.sensnet.node.plugins.DataVisualizerPlugin;

import org.json.JSONArray;
import org.json.JSONException;

public class GasVisualizer extends DataVisualizerPlugin {

	public GasVisualizer(SensNetNodeConfiguration configuration)
			throws SQLException {
		super(configuration);
		IndexizerHolder.getInstance().put(new ChemGasSensor());
	}

	@Override
	public String getSensorName() {
		return "Gas detcteor (chemical)";
	}

	@Override
	public int getSensorType() {
		return 2;
	}

	@Override
	public String getSensorTypeName() {
		return "chemgas";
	}

	@Override
	public DatapointJSONApiPage getDatapointJSONApiPage() {
		return new DatapointJSONApiPage() {

			@Override
			public JSONArray getLatestDataNear(Date upperLimit)
					throws JSONException, SQLException {
				return new JSONArray(
						ChemGasSensor.getLatestDosesFromAllSensors(upperLimit));
			}

			@Override
			public JSONArray getData(Date from, Date to) throws JSONException,
					SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public JSONArray getData(Date from, Date to, int node)
					throws JSONException, SQLException {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

}
