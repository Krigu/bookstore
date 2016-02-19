package org.books.rest;

import org.books.application.exception.ValidationException;
import org.books.application.interceptor.ValidationInterceptor;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Set;
import java.util.logging.Logger;

@Provider
public class ConstraintViolationExceptionMapper
        implements ExceptionMapper<ValidationException> {

    private static final Logger LOGGER = Logger.getLogger(ConstraintViolationExceptionMapper.class.getName());

    @Override
    public Response toResponse(final ValidationException exception) {

        LOGGER.info("Validation exception:" + exception.getMessage());

        return Response
                .status(400)
                .entity("Invalid data: " + exception.getMessage())
                .build();
    }


}
