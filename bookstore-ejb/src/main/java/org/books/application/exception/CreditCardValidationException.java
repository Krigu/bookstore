
package org.books.application.exception;

public class CreditCardValidationException extends Exception {
    public enum Code{INVALID_CREDIT_CARD_FORMAT,INVALID_CREDIT_CARD_NUMBER,CREDIT_CARD_EXPIRED}
    
    private final Code code;
    
    public CreditCardValidationException(CreditCardValidationException.Code code) {
        super();
        this.code = code;
    }
    
    public Code getCode() {
        return code;
    }
    
}
