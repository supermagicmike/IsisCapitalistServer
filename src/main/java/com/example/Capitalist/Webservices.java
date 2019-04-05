/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.Capitalist;

/**
 *
 * @author Mick
 */
import com.google.gson.Gson;
import generated.PallierType;
import generated.ProductType;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

@Path("")
public class Webservices {

    Services services;

    public Webservices() {
        services = new Services();
    }

    @GET
    @Path("world")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getWorld(@Context HttpServletRequest request) throws JAXBException {
        String username= request.getHeader("X-user");
        System.out.println("username: "+username);
        return Response.ok(services.readWorldFromXml(username)).build();
    }
    
    @PUT
    @Path("product")
    public Response Product(@Context HttpServletRequest request,String body) throws JAXBException {
        String username= request.getHeader("X-user");
        ProductType product = new Gson().fromJson(body, ProductType.class); 
        return Response.ok(services.updateProduct(username,product)).build();
    }
    
    @PUT
    @Path("manager")
    public Response Manager(@Context HttpServletRequest request, String body) throws JAXBException {
        String username= request.getHeader("X-user");
        PallierType manager = new Gson().fromJson(body, PallierType.class); 
        
        return Response.ok(services.updateManager(username,manager)).build();
    }
    

}
