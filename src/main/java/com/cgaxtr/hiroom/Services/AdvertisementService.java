package com.cgaxtr.hiroom.Services;

import com.cgaxtr.hiroom.DAO.DAOAdvertisement;
import com.cgaxtr.hiroom.Exceptions.InternalServerError;
import com.cgaxtr.hiroom.POJO.Advertisement;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/advertisement")
public class AdvertisementService {

    private DAOAdvertisement dao = new DAOAdvertisement();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAdvertisement(@PathParam("id") int id){
        Advertisement add;
        try {
            add = dao.getAdvertisement(id);
        } catch (InternalServerError internalServerError) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Response.Status.OK).entity(add).build();
    }

    @GET
    @Path("/user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSelfAdvertisement(@PathParam("id") int id){
        List<Advertisement> list;

        try{
            list = dao.getSelfAdvertisements(id);
        } catch (InternalServerError internalServerError) {
            internalServerError.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Response.Status.OK).entity(list).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAdvertisements(){
        List<Advertisement> advertisements;

        try {
            advertisements = dao.getAllAdvertisements();
        } catch (InternalServerError internalServerError) {
            internalServerError.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Response.Status.OK).entity(advertisements).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addvertisements(Advertisement add){

        try {
            dao.addAdvertisement(add);
        } catch (InternalServerError internalServerError) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return  Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAdvertisement(@PathParam("id") int id){

        try {
            dao.deleteAdvertisement(id);
        } catch (InternalServerError internalServerError) {
            internalServerError.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/search/{city}")
    public Response searchAdvertisements(@PathParam("city") String city){
        List<Advertisement> list;

        try {
            list = dao.search(city);
        } catch (InternalServerError internalServerError) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Response.Status.OK).entity(list).build();
    }
    /*
    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyAdvertisement(@PathParam("id") int id){
        return null;
    }
    */
}
