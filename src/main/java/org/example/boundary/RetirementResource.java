package org.example.boundary;


import org.example.entity.Person;
import org.example.controller.RetirementService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.util.Map;

@Path("/api/retirement")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Retirement Calculator", description = "Calculate years until retirement in Romania")
public class RetirementResource {

    @Inject
    RetirementService retirementService;

    @POST
    @Path("/calculate")
    @Operation(
            summary = "Calculate years until retirement",
            description = "Calculates how many years a person has until retirement in Romania based on their PIN and work history"
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Successful calculation",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = Map.class)
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Bad Request - Invalid input data (PIN, name, or date validation failed)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Internal Server Error - Unexpected error occurred",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)
            )
    })
    public Response calculateRetirement(@Valid Person person) {
        try {
            Map<String, Object> result = retirementService.calculateYearsUntilRetirement(person);
            return Response.ok(result).build();
        } catch (Exception e) {
            // This will be caught by the exception mappers
            throw e;
        }
    }

    @GET
    @Path("/health")
    @Operation(summary = "Health check", description = "Check if the service is running")
    @APIResponse(responseCode = "200", description = "Service is healthy")
    public Response healthCheck() {
        return Response.ok(Map.of("status", "UP", "service", "Retirement Calculator")).build();
    }
}
