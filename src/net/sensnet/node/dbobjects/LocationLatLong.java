package net.sensnet.node.dbobjects;

public class LocationLatLong {
	private int lat, lng;

	public LocationLatLong(int lat, int lng) {
		this.lat = lat;
		this.lng = lng;
	}

	public int getLat() {
		if (lat == 0) {
			return 4927519;
		}
		return lat;
	}

	public int getLng() {
		if (lng == 0) {
			return 971268;
		}
		return lng;
	}
}
