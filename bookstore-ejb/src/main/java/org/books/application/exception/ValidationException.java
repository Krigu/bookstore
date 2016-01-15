package org.books.application.exception;

import javax.ejb.ApplicationException;
import java.util.Set;

@ApplicationException
public class ValidationException extends RuntimeException {

    public ValidationException() {
    }

    public ValidationException(Set violations) {
        super(violations.toString());
    }
}
