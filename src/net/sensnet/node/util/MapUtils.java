package net.sensnet.node.util;

import net.sensnet.node.pages.NodesOverviewPage;

public class MapUtils {
	public static String makeCoordinate(int coord) {
		String lng = NodesOverviewPage.ammendZero(coord + "", 6);
		lng = lng.substring(0, lng.length() - 5) + "."
				+ lng.substring(lng.length() - 5, lng.length());
		return lng;
	}
}
