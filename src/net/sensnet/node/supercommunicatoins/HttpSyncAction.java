package net.sensnet.node.supercommunicatoins;

import java.io.UnsupportedEncodingException;

public abstract class HttpSyncAction {
	public abstract String getPath();

	public abstract String getPostData() throws UnsupportedEncodingException;
}
