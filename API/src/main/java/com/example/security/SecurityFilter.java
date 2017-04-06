package com.example.security;

import com.example.annotations.Secured;
import com.example.util.TokenHelper;
import com.example.dao.UserManager;
import com.example.models.UserModel;
import com.example.models.UserRole;
import org.glassfish.jersey.server.ContainerRequest;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {

    @Inject
    private javax.inject.Provider<UriInfo> uriInfo;

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String method = requestContext.getMethod().toLowerCase();
        String path = ((ContainerRequest) requestContext).getPath(true).toLowerCase();

        Class<?> resourceClass = resourceInfo.getResourceClass();
        Method resourceMethod = resourceInfo.getResourceMethod();

        if (("get".equals(method) && ("application.wadl".equals(path) || "application.wadl/xsd0.xsd".equals(path)))
                || ("post".equals(method) && "authentication".equals(path))) {
            // pass through the filter.
            requestContext.setSecurityContext(new SecurityContextAuthorizer(uriInfo, () -> "anonymous", UserRole.ANONYMOUS));
            return;
        }

        String authorizationHeader = ((ContainerRequest) requestContext).getHeaderString("authorization");
        if (authorizationHeader == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        String strToken = extractJwtTokenFromAuthorizationHeader(authorizationHeader);
        UserManager manager = new UserManager();
        if (TokenHelper.isValid(strToken)) { // check if token is valid
            if(Objects.equals(TokenHelper.getIssuer(strToken), "TrainTickets-Security")) {

                String email = TokenHelper.getEmail(strToken);
                UserRole role = UserRole.idToRole(TokenHelper.getRole(strToken));
                if (email != null && role != null) {
                    // check if token identifies a user
                    UserModel model = manager.getUserByEmail(email);
                    // check if user exists
                    if (model != null) {
                        // check if token role corresponds to user role
                        if (role.id() == model.getRoleUser()) {
                            // if role changes, generate a new token
                            List<UserRole> classRoles = extractRoles(resourceClass);
                            List<UserRole> methodRoles = extractRoles(resourceMethod);
                            // check if user has permission
                            if (classRoles.isEmpty() || methodRoles.isEmpty() ||
                                    classRoles.contains(role) || methodRoles.contains(role)) {
                                requestContext.setSecurityContext(
                                        new SecurityContextAuthorizer(uriInfo, () -> email, role));
                                return;
                            }
                        }
                    }
                }
            }
        }
        throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

    // Extract the roles from the annotated element
    private List<UserRole> extractRoles(AnnotatedElement annotatedElement) {
        if (annotatedElement == null) {
            return new ArrayList<UserRole>();
        } else {
            Secured secured = annotatedElement.getAnnotation(Secured.class);
            if (secured == null) {
                return new ArrayList<UserRole>();
            } else {
                UserRole[] allowedRoles = secured.value();
                return Arrays.asList(allowedRoles);
            }
        }
    }

    private static String extractJwtTokenFromAuthorizationHeader(String auth) {
        //Replacing "Bearer Token" to "Token" directly
        return auth.replaceFirst("[B|b][E|e][A|a][R|r][E|e][R|r] ", "")
                .replace(" ", "");
    }
}
