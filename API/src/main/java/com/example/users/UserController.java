package com.example.users;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("users")
public class UserController {

    private UserManager userManager = new UserManager();

    /**
     * Returns a user object by username or id
     * @param param String
     */
    @GET
    @Path("{param}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserModel getUserByName(@PathParam("param") String param) {
        try {
            return userManager.getUserById(Integer.parseInt(param));
        }
        catch(NumberFormatException e) {
            return userManager.getUserByName(param);
        }
    }
}
