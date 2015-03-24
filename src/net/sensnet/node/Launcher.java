package net.sensnet.node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;

import net.sensnet.node.plugins.Plugin;
import net.sensnet.node.sensor.BluetoothSensorReceiver;
import net.sensnet.node.sensor.SensorReceiver;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

public class Launcher {
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
		Logger logger = Log.getRootLogger();
		logger.info("Loading plugins...");
		try (BufferedReader read = new BufferedReader(new FileReader(new File(
				"conf/plugins.txt")))) {
			String line;
			while ((line = read.readLine()) != null) {
				try {
					Class<?> forName = Class.forName(line);
					Constructor<?> c = forName
							.getConstructor(SensNetNodeConfiguration.class);
					Object plugin = c.newInstance(SensNetNodeConfiguration
							.getInstance());
					if (!(plugin instanceof Plugin)) {
						throw new InstantiationException("Class not a plugin!");
					}
					logger.info("Loaded plugin '" + forName.getName() + "'.");
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException | NoSuchMethodException
						| SecurityException | IllegalArgumentException
						| InvocationTargetException e) {
					logger.warn("Error loading plugin " + line + ".", e);
				}
			}
		} catch (IOException e) {
			logger.warn(
					"No plugins.txt found or couldn't read file! No plugin loaded.",
					e);
		}
		logger.info("Finished plugin loading.");
		if (SensNetNodeConfiguration.getInstance().isBLEEnabled()) {
			new Thread(new BluetoothSensorReceiver()).start();
		}
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
}
