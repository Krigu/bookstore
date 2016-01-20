package org.books.application;

import javax.ejb.Remote;
import org.books.application.exception.CreditCardValidationException;

@Remote
public interface CreditCardValidatorRemote {

    public void checkCreditCard(String number, String type) throws CreditCardValidationException;

    public void checkCreditCard(String number, String type, Integer expirationMonth, Integer expirationYear) throws CreditCardValidationException;
}
