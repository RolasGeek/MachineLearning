package com.studies.restService;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studies.classifiers.prepareClassifiers;
import com.studies.model.Messages;
import com.studies.prognoze.Prognoze;
import com.studies.service.MessageService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

@Path("/message")
public class MessageRest {

	@POST
	@Path("insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_XML)
	public String insertMessage(Messages message) {
		Date date = new Date();
		message.setDate(date);
		try {
		MessageService.getInstance().create(message);
		Prognoze.getInstance().executeClassifiers();
		return "Message has been successfully posted";
		} catch (Exception e) {
			return "failed";
		}
	}
	
	@GET
	@Path("all/{amount}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Messages> getAll(@PathParam("amount") Integer amount) throws IOException {
		List<Messages> m = MessageService.getInstance().listAll(0, amount);
		return m;
	}
	
	@GET
	@Path("calculate/{text}/{method}")
	@Produces(MediaType.APPLICATION_XML)
	public String calculate(@PathParam("text") String text, @PathParam("method") Integer method) {
		String guess = "";
		switch (method) {
		case 0:
			guess = Prognoze.getInstance().Calculate(text);
			break;
		case 1:
			guess = Prognoze.getInstance().Calculate2(text);
			break;
		case 2:
			guess = Prognoze.getInstance().Calculate3(text);
			break;
		default:
			//Balsavimo metodas
			break;
		}
		System.out.println(guess);
		return guess;
	}
	
	@GET
	@Path("remove/{id}")
	@Produces(MediaType.APPLICATION_XML)
	public String remove(@PathParam("id") Integer id) {
		try {
		MessageService.getInstance().deleteById(id);
		Prognoze.getInstance().executeClassifiers();
		return "Success";
		} catch (Exception e) {
			
			return "";
			// TODO: handle exception
		}

	}
	
}