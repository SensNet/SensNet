package net.sensnet.node.pages;

import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sensnet.node.DatabaseConnection;
import net.sensnet.node.Form;
import net.sensnet.node.RandomToken;
import net.sensnet.node.dbobjects.Node;

import org.cacert.gigi.output.template.Template;

public class RegisterDirectSubNodeForm extends Form {
	private static Template t;
	private String token;
	static {
		t = new Template(
				RegisterDirectSubNodeForm.class
						.getResource("RegisterDirectSubNodeForm.templ"));
	}

	public RegisterDirectSubNodeForm(HttpServletRequest hsr) {
		super(hsr);
	}

	@Override
	public boolean submit(PrintWriter out, HttpServletRequest req) {
		String name = req.getParameter("nodename");
		String description = req.getParameter("nodedescription");
		String uid = req.getParameter("uid");
		if (name != null && !name.trim().isEmpty() && description != null
				&& !description.trim().isEmpty() && uid != null) {
			token = RandomToken.generateToken(30);
			try {
				Node n = new Node(Integer.parseInt(req.getParameter("uid")),
						name, description);
				try {
					n.commit();
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
				PreparedStatement prep = DatabaseConnection
						.getInstance()
						.prepare(
								"INSERT INTO directsubnodesauth VALUES(NULL,?,?)");
				prep.setInt(1, Integer.parseInt(uid));
				prep.setString(2, token);
				prep.execute();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	protected void outputContent(PrintWriter out, Map<String, Object> vars) {
		t.output(out, vars);
	}

	public String getToken() {
		return token;
	}
}
