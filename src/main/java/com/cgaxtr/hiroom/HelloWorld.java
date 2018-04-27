package com.cgaxtr.hiroom;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/hello")
public class HelloWorld {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getMessage(){

        Connection con = DB.getInstance().getConnection();
        if (con != null) {
            return "happy";
        }else{
            return "sad";
        }
        //return "Hello world!";
    }
}