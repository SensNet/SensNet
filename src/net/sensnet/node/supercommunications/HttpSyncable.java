package net.sensnet.node.supercommunications;

import java.io.UnsupportedEncodingException;

public interface HttpSyncable {
	public abstract String getPath();

	public abstract String getPostData() throws UnsupportedEncodingException;
}
