package com.example.users;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("users")
public class UserController {

    private UserManager userManager = new UserManager();

    /**
     * Returns a user object by email or id
     * @param param String
     */
    @GET
    @Path("{param}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserModel getUser(@PathParam("param") String param) {
        try {
            return userManager.getUserById(Integer.parseInt(param));
        }
        catch(NumberFormatException e) {
            return userManager.getUserByEmail(param);
        }
    }

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int createUser(UserModel user) {
        return userManager.createUser(user);
    }
}
