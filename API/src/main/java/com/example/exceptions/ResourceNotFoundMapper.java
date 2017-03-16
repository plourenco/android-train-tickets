package com.example.exceptions;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by mercurius on 15/03/17.
 */

@Provider
public class ResourceNotFoundMapper implements ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(NotFoundException notFoundException) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Couldn't find a resource with that name")
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
