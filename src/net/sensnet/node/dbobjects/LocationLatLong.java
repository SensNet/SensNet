package net.sensnet.node.dbobjects;

public class LocationLatLong {
	private int lat, lng;

	public LocationLatLong(int lat, int lng) {
		this.lat = lat;
		this.lng = lng;
	}

	public int getLat() {
		// Frankfurt(Main) Fair
		// if (lat == 0) {
		return 5011350;
		// }
		// return lat;
	}

	public int getLng() {
		if (lng == 0) {
			return 863810;
		}
		return lng;
	}
}
