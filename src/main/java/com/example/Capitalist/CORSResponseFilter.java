/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.Capitalist;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Mick
 */

@Provider
public class CORSResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext crc, ContainerResponseContext c) throws IOException {
      c.getHeaders().add("Access-Control-Allow-Origin", "*"); 
      c.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PATCH,DELETE, PUT, OPTIONS");
      c.getHeaders().add("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia, authorization,X-User");
      
      crc.getHeaders().add("Access-Control-Allow-Origin", "*"); 
      crc.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PATCH,DELETE, PUT, OPTIONS");
      crc.getHeaders().add("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia, authorization,X-User");
    }
    

    
}
