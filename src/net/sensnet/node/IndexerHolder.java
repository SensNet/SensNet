package net.sensnet.node;

import java.util.HashMap;

import net.sensnet.node.plugins.SensorIndexer;

public class IndexerHolder {
	private static final IndexerHolder instance = new IndexerHolder();
	private HashMap<Integer, SensorIndexer> indexizer = new HashMap<Integer, SensorIndexer>();

	private IndexerHolder() {
	}

	public static IndexerHolder getInstance() {
		return instance;
	}

	public SensorIndexer getIndexizer(int sensortype) {
		return indexizer.get(sensortype);
	}

	public void put(SensorIndexer indexizer) {
		this.indexizer.put(indexizer.getSensorType(), indexizer);
	}
}
