package com.example.util;

import com.example.models.UserRole;
import com.sun.xml.internal.ws.client.RequestContext;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public class ResourceHelper {

    public static boolean canAccess(SecurityContext securityContext, ContainerRequestContext requestContext,
                                    int userId) {
        if (securityContext.isUserInRole(UserRole.USER.toString())) {
            if (requestContext.getProperty("user_id") instanceof Integer) {
                int id = (Integer) requestContext.getProperty("user_id");
                if (id == userId) {
                    return false;
                }
            }
            return false;
        }
        return true;
    }
}
