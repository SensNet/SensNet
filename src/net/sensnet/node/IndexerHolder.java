package net.sensnet.node;

import java.util.HashMap;

import net.sensnet.node.plugins.SensorIndexer;

public class IndexerHolder {
	private static final IndexerHolder instance = new IndexerHolder();
	private HashMap<Integer, HashMap<Integer, SensorIndexer>> indexizer = new HashMap<Integer, HashMap<Integer, SensorIndexer>>();

	private IndexerHolder() {
	}

	public static IndexerHolder getInstance() {
		return instance;
	}

	public SensorIndexer getIndexizer(int sensorclass, int sensortype) {
		return indexizer.get(sensorclass).get(sensortype);
	}

	public void put(SensorIndexer indexizer) {
		if (!this.indexizer.containsKey(indexizer.getSensorClass())) {
			this.indexizer.put(indexizer.getSensorClass(),
					new HashMap<Integer, SensorIndexer>());
		}
		this.indexizer.get(indexizer.getSensorClass()).put(
				indexizer.getSensorType(), indexizer);
	}
}
