package org.books.application;

import javax.ejb.Remote;
import org.books.application.exception.CreditCardValidationException;

@Remote
public interface CreditCardValidatorRemote {

    public void validateCreditCard(String number, String type) throws CreditCardValidationException;

    public void validateCreditCard(String number, String type, Integer expirationMonth, Integer expirationYear) throws CreditCardValidationException;
}
