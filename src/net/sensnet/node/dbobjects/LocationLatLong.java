package net.sensnet.node.dbobjects;

public class LocationLatLong {
	private int lat, lng;

	public LocationLatLong(int lat, int lng) {
		this.lat = lat;
		this.lng = lng;
	}

	public int getLat() {
		return lat;
	}

	public int getLng() {
		return lng;
	}
}
