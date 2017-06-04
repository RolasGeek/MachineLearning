package com.studies.restService;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studies.classifiers.KNearest;
import com.studies.classifiers.User;
import com.studies.classifiers.prepareClassifiers;
import com.studies.model.Messages;
import com.studies.prognoze.Prognoze;
import com.studies.prognoze.Teacher;
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
		Teacher.getInstance().execute();
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
	public String calculate(@PathParam("text") String text, @PathParam("method") Integer method) throws Exception {
		String guess = "";
		prepareClassifiers.countLenghts();
		KNearest.execute();
		switch (method) {
		case 0:
			guess = Prognoze.getInstance().Calculate(text).get(0).getName();
			break;
		case 1:
			guess = Prognoze.getInstance().Calculate2(text).get(0).getName();
			break;
		case 2:
			List<User> result = Prognoze.getInstance().Calculate3(text);
			guess = result.get(0).getKoef() != 0 ? result.get(0).getName() : "Not enought data";  
			break;
		default:
			//Balsavimo metodas
			guess = Prognoze.getInstance().executeAll(text);
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
		Teacher.getInstance().execute();
		return "Success";
		} catch (Exception e) {
			
			return "";
			// TODO: handle exception
		}

	}
	
	@GET
	@Path("teacher/{text}")
	@Produces(MediaType.APPLICATION_XML)
	public String  teacherGuess(@PathParam("text") String text) {
		String guess = Teacher.getInstance().calculute(text);
		return guess;
	}
	
	@GET
	@Path("teacherlearn/{text}/{method}")
	@Produces(MediaType.APPLICATION_XML)
	public String  teacherWrong(@PathParam("text") String text, @PathParam("method") Integer method) {
		Teacher.getInstance().learn(text, method);
		return "Try now!!";
	}

	
	
}