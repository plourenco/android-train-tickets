package com.example.security;

import com.example.models.UserRole;

import javax.inject.Provider;
import javax.security.auth.Subject;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.security.Principal;

public class SecurityContextAuthorizer implements SecurityContext {

    private Principal principal;
    private javax.inject.Provider<UriInfo> uriInfo;
    private UserRole role;

    public SecurityContextAuthorizer(final Provider<UriInfo> uriInfo, final Principal principal, UserRole role) {
        this.principal = principal;
        if (principal == null) {
            this.principal = new Principal() {
                @Override
                public String getName() {
                    return "anonymous";
                }

                @Override
                public boolean implies(Subject subject) {
                    return true;
                }
            };
        }
        this.uriInfo = uriInfo;
        this.role = role;
    }

    public Principal getUserPrincipal() {
        return this.principal;
    }

    public boolean isUserInRole(String role) {
        if(role != null) {
            try {
                UserRole obj = UserRole.valueOf(role.toUpperCase());
                return obj.equals(this.role);
            }
            catch(IllegalArgumentException ignored) { }
        }
        return false;
    }

    public boolean isSecure() {
        return true;
    }

    public String getAuthenticationScheme() {
        return SecurityContext.DIGEST_AUTH;
    }
}