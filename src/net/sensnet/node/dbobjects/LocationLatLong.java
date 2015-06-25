package net.sensnet.node.dbobjects;

import net.sensnet.node.SensNetNodeConfiguration;

public class LocationLatLong {
	private int lat, lng;

	public LocationLatLong(int lat, int lng) {
		this.lat = lat;
		this.lng = lng;
	}

	public int getLat() {
		if (lat == 0) {
			return SensNetNodeConfiguration.getInstance().getLocationLat();
		}
		return lat;
	}

	public int getLng() {
		if (lng == 0) {
			return SensNetNodeConfiguration.getInstance().getLocationLng();
		}
		return lng;
	}
}
