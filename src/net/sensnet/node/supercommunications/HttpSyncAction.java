package net.sensnet.node.supercommunications;

import java.io.UnsupportedEncodingException;

public abstract class HttpSyncAction {
	public abstract String getPath();

	public abstract String getPostData() throws UnsupportedEncodingException;
}
