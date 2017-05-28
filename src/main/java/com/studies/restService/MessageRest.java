package com.studies.restService;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studies.SpellingChecker.SpellCheckResponse;
import com.studies.SpellingChecker.SpellingCheckClass;
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
	@Path("calculate/{text}")
	@Produces(MediaType.APPLICATION_XML)
	public String calculate(@PathParam("text") String text) {
		String guess = Prognoze.getInstance().Calculate(text);
		System.out.println(guess);
		return guess;
	}
}