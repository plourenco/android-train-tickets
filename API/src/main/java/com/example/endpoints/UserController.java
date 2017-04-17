package com.example.endpoints;

import com.example.exceptions.InvalidUserDataException;
import com.example.security.PasswordSecurity;
import com.example.util.TokenHelper;
import com.example.models.TokenModel;
import com.example.models.RawUserModel;
import com.example.dao.UserManager;
import com.example.models.UserModel;
import com.example.models.UserRole;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Objects;

@Path("users")
public class UserController {

    private final UserManager userManager = new UserManager();

    @GET
    @Path("role/{id}")
    @Produces("application/json")
    public int getUserRole(@PathParam("id")int id){
        return userManager.getUserRole(id);
    }

    /**
     * Authenticates the user and generates a token
     * Expects a non encrypted password (assuming HTTPs)
     */
    @POST
    @Path("auth")
    @Produces("application/json")
    @Consumes("application/json")
    public TokenModel authenticate(RawUserModel credentials) {
        if(credentials.getEmail().isEmpty()) {
            throw new InvalidUserDataException("Invalid user data");
        }
        UserModel user = userManager.getUserByEmail(credentials.getEmail());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 24);

        if(!credentials.getPassword().isEmpty()) {
            try {
                if(PasswordSecurity.compareHashes(user.getHash(), credentials.getPassword())) {
                    return new TokenModel(
                            TokenHelper.getJWTString(user.getEmail(), user.getRoleUser(), calendar.getTime()),
                            TokenHelper.getJWTRefresh(user.getEmail(), user.getRoleUser()),
                                    calendar.getTime(), user.getId());
                }
            }
            catch(NoSuchAlgorithmException ignored) { }
        }
        // if we get here, throw invalid auth
        throw new InvalidUserDataException("Unauthorized");
    }

    /**
     * Generate a new token (expects specific refresh token)
     * @param refresh TokenModel
     */
    @POST
    @Path("refresh")
    @Produces("application/json")
    @Consumes("application/json")
    public TokenModel refresh(TokenModel refresh) {
        String token = refresh.getToken();
        if(TokenHelper.isValid(token)) {
            if(Objects.equals(TokenHelper.getIssuer(token), "TrainTickets-Refresh")) {
                String email = TokenHelper.getEmail(token);
                int role = TokenHelper.getRole(token);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.HOUR, 24);

                if(email != null && role > 0) {
                    return new TokenModel(
                            TokenHelper.getJWTString(email, role, calendar.getTime()),
                            token,
                            calendar.getTime());
                }
            }
        }
        throw new InvalidUserDataException("Unauthorized");
    }

    /**
     * Creates a new user with provided info
     * @param user RawUserModel
     */
    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int createUser(RawUserModel user) {
        return userManager.createUser(user, UserRole.USER);
    }
}
