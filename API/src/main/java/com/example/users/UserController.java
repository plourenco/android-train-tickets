package com.example.users;

import com.example.Main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("users")
public class UserController {

    private final UserManager userManager = new UserManager();

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

    /**
     * Register the user in the system
     * @param user the model of the user
     * @return returns the role id of the created user
     */
    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int createUser(UserModel user) {
        return userManager.createUser(user);
    }

    /**
     * Authenticates a user in the system
     * @param user the model of the user with at least email and password
     * @return UserModel - the model of the user with only id, username and role
     */
    @POST
    @Path("authenticate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public UserModel authUser(UserModel user){
        return userManager.authenticateUser(user.getEmail(), user.getPassword());
    }
}
