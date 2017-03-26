package com.rest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/ep")
public class EntryPoint {

	@GET
	@Path("test")
	@Produces(MediaType.TEXT_PLAIN)
	public String testea(){
		return "Testado";
	}
	
	@GET
	@Path("insert")
	@Produces(MediaType.TEXT_PLAIN)	
	public String insert(@QueryParam("nome") String theName){
		String s = ""+System.currentTimeMillis();
		String pk = s.substring(10,s.length());
		System.out.println("pk="+pk);
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
			//Connection con = DriverManager.getConnection("jdbc:hsqldb:file:./db/usuario", "SA", "");
			Connection con = DriverManager.getConnection("jdbc:hsqldb:mem:memdb", "SA", "");
			Statement stmt = con.createStatement();

			stmt.execute("INSERT INTO usuario (id, nome, descricao) VALUES ("+Integer.valueOf(pk)+", '"+theName+"', 'testando')");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return pk;
	}
	
	@GET
	@Path("select")
	@Produces(MediaType.TEXT_PLAIN)	
	public String select(){
		String s= "";
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
			//Connection con = DriverManager.getConnection("jdbc:hsqldb:file:./db/usuario", "SA", "");
			Connection con = DriverManager.getConnection("jdbc:hsqldb:mem:memdb", "SA", "");
			Statement stmt = con.createStatement();

			ResultSet res = stmt.executeQuery("SELECT * FROM usuario");
			while(res.next()){
				s = s+res.getInt("id")+" - "+res.getString("nome") + " - " +res.getString("descricao")+ "\n"; 
			}

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return s;
	}
	
}
