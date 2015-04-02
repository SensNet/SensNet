package net.sensnet.node;

import java.util.LinkedList;
import java.util.TreeSet;

import net.sensnet.node.plugins.DataVisualizerPlugin;
import net.sensnet.node.plugins.PagePlugin;

public class Menu {
	private static Menu instance = new Menu();
	private TreeSet<DataVisualizerPlugin> sensorsMenu = new TreeSet<DataVisualizerPlugin>();
	private LinkedList<PagePlugin> pageMenu = new LinkedList<PagePlugin>();

	private Menu() {
	}

	protected TreeSet<DataVisualizerPlugin> getSensorsMenu() {
		return sensorsMenu;
	}

	protected void addSensorsItem(DataVisualizerPlugin plugin) {
		sensorsMenu.add(plugin);
	}

	protected static Menu getInstance() {
		return instance;
	}

	protected LinkedList<PagePlugin> getPageMenu() {
		return pageMenu;
	}

	protected void addPageItem(PagePlugin plugin) {
		pageMenu.add(plugin);
	}

}
