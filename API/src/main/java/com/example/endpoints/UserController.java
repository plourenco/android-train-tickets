package com.example.endpoints;

import com.example.annotations.Secured;
import com.example.exceptions.InvalidUserDataException;
import com.example.models.*;
import com.example.security.PasswordSecurity;
import com.example.security.SecurityContextAuthorizer;
import com.example.util.ResourceHelper;
import com.example.util.TokenHelper;
import com.example.dao.UserManager;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@Path("users")
public class UserController {

    @Context
    protected SecurityContext securityContext;

    @Context
    protected ContainerRequestContext requestContext;

    private final UserManager userManager = new UserManager();

    @POST
    @Secured({ UserRole.USER, UserRole.INSPECTOR })
    @Path("cc/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String saveCC(CreditCardModel cc, @PathParam("id") int id) {
        // Check resource permissions
        if(!ResourceHelper.canAccess(securityContext, requestContext, id)) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        return userManager.saveCard(cc, id);
    }

    @GET
    @Path("role/{id}")
    @Produces("application/json")
    public int getUserRole(@PathParam("id")int id) {
        // Check resource permissions
        if(!ResourceHelper.canAccess(securityContext, requestContext, id)) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
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
    public RawTokenModel authenticate(RawUserModel credentials) {
        if(credentials.getEmail().isEmpty()) {
            throw new InvalidUserDataException("Invalid user data");
        }
        UserModel user = userManager.getUserByEmail(credentials.getEmail());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 24);

        if(user != null) {
            if (!credentials.getPassword().isEmpty()) {
                try {
                    if (PasswordSecurity.compareHashes(user.getHash(), credentials.getPassword())) {
                        return new RawTokenModel(
                                TokenHelper.getJWTString(user.getEmail(), user.getRoleUser(), calendar.getTime()),
                                TokenHelper.getJWTRefresh(user.getEmail(), user.getRoleUser()),
                                calendar.getTime(), user.getId(), user.getRoleUser());
                    }
                } catch (NoSuchAlgorithmException ignored) {

                }
            }
        }
        // if we get here, throw invalid auth
        throw new InvalidUserDataException("Unauthorized");
    }

    /**
     * Checks if the token is valid and actually belongs to an user
     * @param tokenModel TokenModel
     */
    @POST
    @Path("check")
    @Produces("application/json")
    @Consumes("application/json")
    public Response valid(TokenModel tokenModel) {
        String token = tokenModel.getToken();
        if(TokenHelper.isTrustworthy("TrainTickets-Security", token)) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_ACCEPTABLE).build();
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
        if(TokenHelper.isTrustworthy("TrainTickets-Refresh", token)) {
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
