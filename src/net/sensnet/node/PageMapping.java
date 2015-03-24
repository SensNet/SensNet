package net.sensnet.node;

import java.util.HashMap;

public class PageMapping {
	private static final PageMapping instance = new PageMapping();
	private HashMap<String, Page> mapping = new HashMap<String, Page>();

	private PageMapping() {
	}

	protected static PageMapping getInstance() {
		return instance;
	}

	protected Page getPageForPath(String path) {
		return mapping.get(path);
	}

	protected void put(String path, Page page) {
		mapping.put(path, page);
	}
}
