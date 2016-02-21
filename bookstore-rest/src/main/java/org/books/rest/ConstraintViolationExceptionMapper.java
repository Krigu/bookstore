package org.books.rest;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

@Provider
public class ConstraintViolationExceptionMapper
        implements ExceptionMapper<ConstraintViolationException> {

    private static final Logger LOGGER = Logger.getLogger(ConstraintViolationExceptionMapper.class.getName());

    @Override
    public Response toResponse(final ConstraintViolationException exception) {

        LOGGER.info("Validation exception:" + exception.getMessage());

        return Response
                .status(400)
                .entity("Invalid data: " + exception.getMessage())
                .build();
    }


}
