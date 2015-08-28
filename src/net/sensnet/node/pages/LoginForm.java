package net.sensnet.node.pages;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sensnet.node.InvalidNodeAuthException;
import net.sensnet.node.SensNetNodeConfiguration;
import net.sensnet.node.dbobjects.User;
import net.sensnet.node.util.AuthUtils;

import org.cacert.gigi.output.template.Form;
import org.cacert.gigi.output.template.Template;

public class LoginForm extends Form {
	static Template t;
	static {
		t = new Template(LoginForm.class.getResource("loginform.templ"));
	}

	public LoginForm(HttpServletRequest hsr) {
		super(hsr);
	}

	@Override
	public boolean submit(PrintWriter out, HttpServletRequest req) {
		try {
			String usr = req.getParameter("user");
			System.out.println(req.getParameter("createUser"));
			System.out.println(req.getParameter("createPw"));
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			messageDigest.update(req.getParameter("pw").getBytes());
			String pw = byteArrayToHexString(messageDigest.digest());
			if (usr == null || usr.trim().isEmpty() || pw == null
					|| pw.trim().isEmpty()) {
				return false;
			}
			User u = AuthUtils.checkUserAgainstDB(usr, pw);
			if (u != null) {
				req.getSession().setAttribute("user", u);
			} else if (!SensNetNodeConfiguration.getInstance().isRootNode()
					&& (u = AuthUtils.checkUserAgaistSuperNode(usr, pw)) != null) {
				req.getSession().setAttribute("user", u);
			}
			return u != null;
		} catch (SQLException | NoSuchAlgorithmException | IOException
				| InvalidNodeAuthException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String register(PrintWriter out, HttpServletRequest req) {
		try {
			String usr = req.getParameter("createUser");

			if (!req.getParameter("createPw").equals(
					req.getParameter("validatePw"))) {
				return "Passwords don't match.";
			}

			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			messageDigest.update(req.getParameter("createPw").getBytes());
			String pw = byteArrayToHexString(messageDigest.digest());
			if (usr == null || usr.trim().isEmpty() || pw == null
					|| pw.trim().isEmpty()) {
				return null;
			}

			if (AuthUtils.checkUserExistenceDB(usr)) {
				return "Username already exists";
			}

			else {
				AuthUtils.registerNewUserDB(usr, pw);
				return "Success! You can now log in.";
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void outputContent(PrintWriter out, Map<String, Object> vars) {
		t.output(out, vars);
	}

	private static String byteArrayToHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}
}
