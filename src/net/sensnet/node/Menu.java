package net.sensnet.node;

import java.util.LinkedList;
import java.util.TreeSet;

import net.sensnet.node.plugins.DataVisualizerPlugin;
import net.sensnet.node.plugins.PagePlugin;
import net.sensnet.node.plugins.PlainPagePlugin;

public class Menu {
	private static final Menu instance = new Menu();
	private TreeSet<DataVisualizerPlugin> sensorsMenu = new TreeSet<DataVisualizerPlugin>();
	private LinkedList<PagePlugin> pageMenu = new LinkedList<PagePlugin>();
	private LinkedList<PlainPagePlugin> plainItems = new LinkedList<PlainPagePlugin>();

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

	protected LinkedList<PlainPagePlugin> getPlainItems() {
		return plainItems;
	}

	protected void addPageItem(PagePlugin plugin) {
		pageMenu.add(plugin);
	}

	protected void addRawPageItem(PlainPagePlugin plugin) {
		plainItems.add(plugin);
	}

}
