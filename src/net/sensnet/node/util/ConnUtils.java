package net.sensnet.node.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;

import net.sensnet.node.InvalidNodeAuthException;
import net.sensnet.node.SensNetNodeConfiguration;

public class ConnUtils {
	public static BufferedReader postNodeAuthenticatedData(String path,
			String postData)
 throws IOException, SQLException,
			InvalidNodeAuthException {
		HttpURLConnection con = (HttpURLConnection) new URL(
				SensNetNodeConfiguration.getInstance().getSuperNode() + path)
				.openConnection();
		String data = "node_token=";
		data += URLEncoder.encode(SensNetNodeConfiguration.getInstance()
				.getSuperNodeAuth(), "UTF-8");
		data += "&node_id=" + SensNetNodeConfiguration.getInstance().getNodeID();
		data += postData;
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		con.setRequestProperty("Content-Length", String.valueOf(data.length()));
		OutputStream os = con.getOutputStream();
		os.write(data.getBytes());
		if (con.getResponseCode() != 200) {
			throw new InvalidNodeAuthException();
		}
		return new BufferedReader(new InputStreamReader(con.getInputStream()));
	}
}
