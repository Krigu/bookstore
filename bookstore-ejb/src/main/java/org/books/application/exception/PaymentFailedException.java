package org.books.application.exception;

public class PaymentFailedException extends Exception {
    
    public enum Code {
        CREDIT_CARD_EXPIRED, INVALID_CREDIT_CARD, PAYMENT_LIMIT_EXCEEDED
    }
    
    private final Code code;
    
    public PaymentFailedException(PaymentFailedException.Code code) {
        super("Error code : " + code);
        this.code = code;
    }
    
    public Code getCode() {
        return code;
    }
    
}
