package net.sensnet.node;

import java.util.TreeSet;

import net.sensnet.node.plugins.DataVisualizerPlugin;

public class Menu {
	private static Menu instance = new Menu();
	private TreeSet<DataVisualizerPlugin> menu = new TreeSet<DataVisualizerPlugin>();

	private Menu() {
	}

	public TreeSet<DataVisualizerPlugin> getMenu() {
		return menu;
	}

	public void addItem(DataVisualizerPlugin plugin) {
		menu.add(plugin);
	}

	public static Menu getInstance() {
		return instance;
	}

}
