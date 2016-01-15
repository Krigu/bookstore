
package org.books.application;

import javax.ejb.Local;
import org.books.application.exception.CreditCardValidationException;

@Local
public interface CreditCardValidatorLocal {
    public void checkCreditCard(String number, String type) throws CreditCardValidationException;
    public void checkCreditCard(String number, String type, Integer expirationMonth, Integer expirationYear) throws CreditCardValidationException;
}
