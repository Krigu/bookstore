package org.books.rest;

import org.books.application.exception.ValidationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

@Provider
public class ViolationExceptionMapper
        implements ExceptionMapper<ValidationException> {

    private static final Logger LOGGER = Logger.getLogger(ViolationExceptionMapper.class.getName());

    @Override
    public Response toResponse(final ValidationException exception) {

        LOGGER.info("Validation exception:" + exception.getMessage());

        return Response
                .status(400)
                .entity("Invalid data: " + exception.getMessage())
                .build();
    }


}
