
package org.books.application;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.books.application.exception.CreditCardValidationException;
import org.books.application.interceptor.ValidationInterceptor;

@Stateless(name="CreditCardValidator")
@Interceptors(ValidationInterceptor.class)
public class CreditCardValidatorBean implements CreditCardValidatorLocal {
    
    private static final Logger LOGGER = Logger.getLogger(CreditCardValidatorBean.class.getName());
    private static final String VISA_REGEX = "^4\\d{15}$";
    private static final String MASTERCARD_REGEX = "^5[1-5]\\d{14}$";
    
    @Override
    public void checkCreditCard(String number, String type) throws CreditCardValidationException {
        checkFormat(number);
        checkLuhndigit(number);
        checkType(type, number);
    }
    
    @Override
    public void checkCreditCard(String number, String type, Integer expirationMonth, Integer expirationYear) throws CreditCardValidationException {
        checkCreditCard(number, type);
        checkExpiredDate(expirationMonth, expirationYear);
    }
    
    /**
     * Check the length and the content of a card number
     *
     * @param cardNumber
     * @throws CreditCardValidationException
     */
    private void checkFormat(String cardNumber) throws CreditCardValidationException {
        //Check the length
        if (cardNumber.length() != 16) {
            throw new CreditCardValidationException(CreditCardValidationException.Code.INVALID_CREDIT_CARD_FORMAT);
        }
        //Check the digit
        if (!cardNumber.matches("\\d{16}$")) {
            throw new CreditCardValidationException(CreditCardValidationException.Code.INVALID_CREDIT_CARD_FORMAT);
        }
    }

    /**
     * Check the card number (throw a CreditCardValidationException if it's not validate)
     *
     * @param cardNumber
     * @throws CreditCardValidationException
     */
    private void checkLuhndigit(String cardNumber) throws CreditCardValidationException {
        //Source : http://rosettacode.org/wiki/Luhn_test_of_credit_card_numbers
        int s1 = 0, s2 = 0;
        String reverse = new StringBuffer(cardNumber).reverse().toString();
        for (int i = 0; i < reverse.length(); i++) {
            int digit = Character.digit(reverse.charAt(i), 10);
            if (i % 2 == 0) {//this is for odd digits, they are 1-indexed in the algorithm
                s1 += digit;
            } else {//add 2 * digit for 0-4, add 2 * digit - 9 for 5-9
                s2 += 2 * digit;
                if (digit >= 5) {
                    s2 -= 9;
                }
            }
        }
        //return (s1 + s2) % 10 == 0;
        if ((s1 + s2) % 10 != 0) {
            throw new CreditCardValidationException(CreditCardValidationException.Code.INVALID_CREDIT_CARD_NUMBER);
        }
    }

    /**
     * Check the type of credit card with the card number
     *
     * @param cardType
     * @param cardNumber
     * @throws CreditCardValidationException
     */
    private void checkType(String cardType, String cardNumber) throws CreditCardValidationException {
        String regEx;
        switch (cardType) {
            case "MasterCard":
                regEx = MASTERCARD_REGEX;
                break;
            case "Visa":
                regEx = VISA_REGEX;
                break;
            default:
                regEx = "";
        }

        if (regEx.isEmpty() || !cardNumber.matches(regEx)) {
            throw new CreditCardValidationException(CreditCardValidationException.Code.INVALID_CREDIT_CARD_NUMBER);
        }
    }
    
    /**
     * Throw an exception if the date is expired
     *
     * @param expirationMonth
     * @param expirationYear
     * @throws CreditCardValidationException
     */
    private void checkExpiredDate(Integer expirationMonth, Integer expirationYear) throws CreditCardValidationException {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, expirationMonth);
        calendar.set(Calendar.YEAR, expirationYear);
        Date exiprationDate = calendar.getTime();
        //Date is expried?
        if (exiprationDate.before(new Date())) {
            throw new CreditCardValidationException(CreditCardValidationException.Code.CREDIT_CARD_EXPIRED);
        }
    }
}
