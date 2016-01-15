
package org.books.application;

import org.books.application.exception.CreditCardValidationException;

import javax.ejb.Local;

@Local
public interface CreditCardValidatorLocal {
    public void checkCreditCard(String number, String type) throws CreditCardValidationException;
    public void checkCreditCard(String number, String type, Integer expirationMonth, Integer expirationYear) throws CreditCardValidationException;
}
