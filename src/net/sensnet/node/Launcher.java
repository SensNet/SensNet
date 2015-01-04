package net.sensnet.node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.regex.Pattern;

import net.sensnet.node.dbobjects.SensorType;
import net.sensnet.node.sensor.SensorReceiver;
import net.sensnet.node.util.ConnUtils;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class Launcher {
	private static HashMap<Integer, SensorType> sensorTypes;

	public static void main(String[] args) throws Exception {
		SensNetNodeConfiguration conf;
		if (args.length != 1) {
			InputStream ins;
			File confFile = new File("conf/node.properties");
			if (confFile.exists()) {
				ins = new FileInputStream(confFile);
			} else {
				ins = Launcher.class.getResourceAsStream("node.properties");
			}
			conf = new SensNetNodeConfiguration(ins);
		} else {
			conf = new SensNetNodeConfiguration(new FileInputStream(new File(
					args[0])));
		}
		DatabaseConnection.init(conf);
		sensorTypes = new HashMap<Integer, SensorType>();
		if (!conf.isRootNode()) {
			SuperCommunicationsManager.getInstance().putJob(
					new ExceptionRunnable() {

						@Override
						public void run() throws Exception {
							System.out.println("Prestart DB Sync...");
							BufferedReader read = ConnUtils
									.postNodeAuthenticatedData("/typedump", "");
							String tmp;
							while ((tmp = read.readLine()) != null) {
								String[] split = tmp.split(Pattern.quote(", "));
								SensorType e = new SensorType(Integer
										.parseInt(split[0]), split[1],
										split[3], split[2]);
								e.updateToDB();
								sensorTypes.put(e.getId(), e);
							}
						}
					});

		} else {
			ResultSet res = DatabaseConnection.getInstance()
					.prepare("SELECT * FROM sensortypes").executeQuery();
			while (res.next()) {
				sensorTypes.put(res.getInt(1), new SensorType(res));
			}
			res.close();
		}
		Server s = new Server(new InetSocketAddress(conf.getHostName(),
				conf.getPort()));
		ServletContextHandler h = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		h.setInitParameter(SessionManager.__SessionCookieProperty,
				"Node-Session");
		h.addServlet(SensNetNode.class, "/*");
		HandlerList hl = new HandlerList();
		hl.setHandlers(new Handler[] { generateStaticContext(), h });
		s.setHandler(hl);
		s.start();
		new Thread(new SensorReceiver(SensNetNodeConfiguration.getInstance()
				.getSerialInputDevice())).start();
	}

	private static Handler generateStaticContext() {
		final ResourceHandler rh = new ResourceHandler();
		rh.setResourceBase("static/");
		ContextHandler ch = new ContextHandler("/static");
		ch.setHandler(rh);
		return ch;
	}

	public static HashMap<Integer, SensorType> getSensorTypes() {
		return sensorTypes;
	}
}
