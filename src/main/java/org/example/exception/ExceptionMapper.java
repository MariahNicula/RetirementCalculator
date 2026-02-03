package org.example.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
class InvalidNameExceptionMapper implements ExceptionMapper<InvalidNameException> {

    @Override
    public Response toResponse(InvalidNameException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid Name");
        error.put("message", exception.getMessage());
        error.put("status", "400");

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(error)
                .build();
    }
}


