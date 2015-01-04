package net.sensnet.node.dbobjects;

import java.io.IOException;
import java.sql.SQLException;

import net.sensnet.node.InvalidNodeAuthException;

public interface Syncable {
	public void commit() throws IOException, SQLException,
			InvalidNodeAuthException;
}
