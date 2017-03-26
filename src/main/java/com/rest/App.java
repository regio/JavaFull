package com.rest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hsqldb.persist.HsqlProperties;

/**
 * Hello world!
 *
 */
public class App {	
	final static Logger lg = Logger.getLogger(App.class);
	public static void main(String[] args) throws Exception {
		System.out.println("Jetty starting...");
		lg.info("Jetty Server starting...");
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");

		Server jettyServer = new Server(8081);
		jettyServer.setHandler(context);

		ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(0);

		// Tells the Jersey Servlet which REST service/class to load.
		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
				EntryPoint.class.getCanonicalName());
				//EntryPoint.class.getCanonicalName());				

		try {
			jettyServer.start();
			lg.info("Jetty Server started!");

			lg.info("HSQLDB Server starting...");
			HsqlProperties p = new HsqlProperties();
			// p.setProperty("server.database.0", "file:./db/usuario");
			p.setProperty("server.database.0", "mem:memdb");
			p.setProperty("server.dbname.0", "mydb");
			p.setProperty("server.port", "9001");
			org.hsqldb.Server server = new org.hsqldb.Server();
			server.setProperties(p);
			server.setLogWriter(null);
			server.setErrWriter(null);
			server.start();
			databaseSetup();
			lg.info("HSQLDB Server started!!");

			jettyServer.join();
		} finally {
			jettyServer.destroy();
		}
	}

	private static void databaseSetup() {
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
			Connection con = DriverManager.getConnection("jdbc:hsqldb:mem:memdb", "SA", "");
			// Connection con = DriverManager.getConnection("jdbc:hsqldb:file:./db/usuario", "SA", "");
			Statement stmt = con.createStatement();

			stmt.executeUpdate(
					"CREATE TABLE usuario ( id INT NOT NULL, nome VARCHAR(50) NOT NULL, descricao VARCHAR(20) NOT NULL, PRIMARY KEY (id));");
			System.out.println("Table created!!");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
}
