package com.cgaxtr.hiroom.Services;

import com.cgaxtr.hiroom.Auth.JWTTokenNeeded;
import com.cgaxtr.hiroom.Auth.JWTUtils;
import com.cgaxtr.hiroom.DAO.DAOUser;
import com.cgaxtr.hiroom.Exceptions.InternalServerError;
import com.cgaxtr.hiroom.Exceptions.UserAlreadyExists;
import com.cgaxtr.hiroom.POJO.Credential;
import com.cgaxtr.hiroom.POJO.User;
import com.cgaxtr.hiroom.POJO.UserData;
import com.cgaxtr.hiroom.Utils.Config;
import com.cgaxtr.hiroom.Utils.File;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Path("/user")
public class UserService {

    private DAOUser userDAO = new DAOUser();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/register")
    public Response registerUser(User user){
        UserData response;
        try {
            int id = userDAO.register(user);
            String token = JWTUtils.issueToken(user.getEmail());
            User u = new User();
            u.setId(id);
            u.setEmail(user.getEmail());
            u.setName(user.getName());
            response = new UserData(token, u);

        } catch (UserAlreadyExists userAlreadyExists) {
            return Response.status(Response.Status.CONFLICT).build();
        } catch (InternalServerError internalServerError) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok().entity(response).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Response login(Credential credential){
        User login;

        try{
            login = userDAO.login(credential);
        } catch (InternalServerError internalServerError) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        if (login != null){
            String token = JWTUtils.issueToken(credential.getEmail());
            UserData response = new UserData(token, login);
            return Response.status(Response.Status.OK).entity(response).build();
        }else{
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Path("/update_profile_image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadUserProfile(@FormDataParam("file") InputStream fileInputStream, @FormDataParam("file") FormDataContentDisposition fileMetaData, @FormDataParam("id") int id){

        try {
            File.save(fileInputStream, File.USER_PROFILE_PATH + fileMetaData.getFileName());

            String url = Config.DOMAIN + ":" + Config.PORT + Config.AVATAR_FOLDER + fileMetaData.getFileName();
            userDAO.updateAvatar(url,id);
        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public User getProfile(@PathParam("id") int id) {
        User u = null;
        try{
            u = userDAO.getUserById(id);
        } catch (InternalServerError internalServerError) {
            internalServerError.printStackTrace();
        }
        return u;
    }

    @PUT
    //@JWTTokenNeeded
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProfile(@PathParam("id") int id, User user){

        try {
            if (userDAO.updateUser(id, user)){
                return Response.status(Response.Status.OK).build();
            }else{
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (InternalServerError internalServerError) {
            internalServerError.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
