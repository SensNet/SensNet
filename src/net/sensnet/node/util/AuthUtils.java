package net.sensnet.node.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.InvalidNodeAuthException;
import net.sensnet.node.dbobjects.User;

public class AuthUtils {
	public static User checkUserAgainstDB(String usr, String pw)
			throws SQLException {
		PreparedStatement s = DatabaseConnection.getInstance().prepare(
				"SELECT readonly FROM users WHERE name=? AND pw=?");
		s.setString(1, usr);
		s.setString(2, pw);
		ResultSet res;
		if (!(res = s.executeQuery()).first()) {
			return null;
		}
		User u = new User(res, usr);
		res.close();
		return u;
	}
	
	public static boolean checkUserExistenceDB(String usr) {
		PreparedStatement s;
		try {
			s = DatabaseConnection.getInstance().prepare(
					"SELECT id FROM users WHERE name=?");
		s.setString(1, usr);
		ResultSet res;
		if (!(res = s.executeQuery()).first()) {
			res.close();
			return false;
		}
		res.close();
		
		return true;
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
	}
	
	public static void registerNewUserDB(String usr, String pw)
			throws SQLException {
		PreparedStatement s = DatabaseConnection.getInstance().prepare(
				"INSERT INTO users (name, pw, readonly) VALUES (?, ?,'0')");
		s.setString(1, usr);
		s.setString(2, pw);
        s.executeUpdate();
	}

	public static User checkUserAgaistSuperNode(String usr, String pw)
			throws MalformedURLException, IOException, SQLException,
			InvalidNodeAuthException {
		String data = "&username=" + URLEncoder.encode(usr, "UTF-8");
		data += "&password=" + URLEncoder.encode(pw, "UTF-8");
		BufferedReader r = ConnUtils.postNodeAuthenticatedData("/auth", data);
		String tmp;
		boolean auth = false, readOnly = false;
		while ((tmp = r.readLine()) != null) {
			String[] res = tmp.split(": ");
			if (res[0].equals("Auth")) {
				auth = Boolean.parseBoolean(res[1]);
			} else if (res[0].equals("readonly")) {
				readOnly = Boolean.parseBoolean(res[1]);
			}
		}
		if (!auth) {
			return null;
		}
		User u = new User(usr, readOnly);
		return u;
	}

	public static boolean checkNodeAuth(HttpServletRequest req)
			throws SQLException {
		String nodeAuthToken = req.getParameter("node_token");
		String nodeID = req.getParameter("node_id");
		return checkNodeAuth(nodeID, nodeAuthToken);
	}

	public static boolean checkNodeAuth(String nodeID, String nodeAuthToken)
			throws SQLException {
		PreparedStatement checkNodeAuthToken = DatabaseConnection
				.getInstance()
				.prepare(
						"SELECT id FROM directsubnodesauth WHERE node=? AND token=?");
		checkNodeAuthToken.setString(1, nodeID);
		checkNodeAuthToken.setString(2, nodeAuthToken);
		return checkNodeAuthToken.executeQuery().first();
	}

}
