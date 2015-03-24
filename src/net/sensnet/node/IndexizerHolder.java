package net.sensnet.node;

import java.util.HashMap;

import net.sensnet.node.plugins.SensorIndexizer;

public class IndexizerHolder {
	private static final IndexizerHolder instance = new IndexizerHolder();
	private HashMap<Integer, SensorIndexizer> indexizer = new HashMap<Integer, SensorIndexizer>();

	private IndexizerHolder() {
	}

	public static IndexizerHolder getInstance() {
		return instance;
	}

	public SensorIndexizer getIndexizer(int sensortype) {
		return indexizer.get(sensortype);
	}

	public void put(SensorIndexizer indexizer) {
		this.indexizer.put(indexizer.getSensorType(), indexizer);
	}
}
