package org.books.application.exception;

import javax.ejb.ApplicationException;

@ApplicationException
public class ValidationException extends RuntimeException {

    public ValidationException() {
    }

    public ValidationException(String s) {
        super(s);
    }
}
