package br.ufsc.lrg.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import br.com.srs.gsonld.GsonLD;
import br.ufsc.lrg.lists.SemanticUsersList;
import br.ufsc.lrg.lists.UsersList;

import com.google.gson.Gson;
import com.sun.jersey.spi.resource.Singleton;

@Path("/service")
@Singleton
public class Controller {
	private String[] dataList = new String[1000];
	private GsonLD gsonLD = new GsonLD();
	private Gson gson = new Gson();	
	@Context
    ServletContext context;
	
	private SemanticUsersList semanticUsersList;
	private UsersList usersList;
	
	@PostConstruct
	public void contruct(){
		semanticUsersList = new SemanticUsersList(context.getInitParameter("dataset"));
		usersList = new UsersList(context.getInitParameter("dataset"));
	}
	@Path("/semantic/{c}")
	@GET
	public String getSemanticJson(@PathParam("c") int c) {
		
		long time = System.nanoTime();
		String returne = gsonLD.toJsonLD(
				semanticUsersList.getSemanticUserById(c)).toString();
		time = System.nanoTime() - time;
		dataList[c] = (c + 1) + "," + time + "\n";

		return returne;
	}

	@Path("/nonsemantic/{c}")
	@GET
	public String getJson(@PathParam("c") int c) {
		long time = System.nanoTime();
		String returne = gson.toJson(usersList.getUserById(c));
		time = System.nanoTime() - time;
		dataList[c] = (c + 1) + "," + time/1000000 + "\n";

		return returne;
	}

	@Path("/getData/")
	@GET
	public String getData() {
		try {
			for (int i = 0; i < 1000; i++) {
				Files.write(Paths.get(context.getInitParameter("filepath")), dataList[i].getBytes(),
						StandardOpenOption.APPEND);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "Dados Gravados";
	}
}