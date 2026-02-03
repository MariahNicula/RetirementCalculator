package org.example.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;

@Provider
public class InvalidDateExceptionMapper implements ExceptionMapper<InvalidDateException> {

    @Override
    public Response toResponse(InvalidDateException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid Date");
        error.put("message", exception.getMessage());
        error.put("status", "400");

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(error)
                .build();
    }
}
