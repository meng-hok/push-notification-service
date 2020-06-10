package com.kosign.push.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class HelloService {

	private static int activeSessions = 0;

	@Context
	private HttpServletRequest request;

	@SuppressWarnings("rawtypes")
	@Path("hello")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response hello(String body) {
		return Response.ok().entity("hello").build();

	}
}
