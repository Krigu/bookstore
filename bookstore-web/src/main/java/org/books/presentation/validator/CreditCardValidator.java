/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.books.util.MessageFactory;

/**
 *
 * @author Tilmann Bück
 */
@FacesValidator(CreditCardValidator.VALIDATOR_ID)
public class CreditCardValidator implements Validator, StateHolder {

    public static final String VALIDATOR_ID = "org.books.presentation.validator.creditcardvalidator";

    public static final String WRONG_CARD_FORMAT = "org.books.presentation.validator.creditcardvalidator.WRONG_CARD_FORMAT";
    public static final String WRONG_CARD_NUMBER = "org.books.presentation.validator.creditcardvalidator.WRONG_CARD_NUMBER";

    private String cardTypeId;
    boolean transientValue = false;
    private final String regExVisa = "^4\\d{15}$";
    private final String regExMaster = "^5[1-5]\\d{14}$";

    public void setCardTypeId(String cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        UIInput ccType = (UIInput) component.findComponent(cardTypeId);
        Object type = ccType.getValue();
        String cardType = type.toString();
        String cardNumber = (String) value;

        checkFormat(cardNumber);
        checkLuhndigit(cardNumber);
        checkType(cardType, cardNumber);
    }

    /**
     * Check the length and the content of a card number
     * @param cardNumber
     * @throws ValidatorException
     */
    private void checkFormat(String cardNumber) throws ValidatorException {
        //Check the length
        if (cardNumber.length() != 16) {
            throw new ValidatorException(MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, WRONG_CARD_FORMAT));
        }
        //Check the digit
        if (!cardNumber.matches("\\d{16}$")) {
            FacesMessage message = MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, WRONG_CARD_FORMAT);
            throw new ValidatorException(message);
        }
    }

    /**
     * Check the type of credit card with the card number
     * @param cardType
     * @param cardNumber
     * @throws ValidatorException 
     */
    private void checkType(String cardType, String cardNumber) throws ValidatorException {
        String regEx = regExMaster;
        if (cardType.equals("Visa")) {
            regEx = regExVisa;
        }
        if (!cardNumber.matches(regEx)) {
            FacesMessage message = MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, WRONG_CARD_NUMBER);
            throw new ValidatorException(message);
        }
    }

    /**
     * Check the card number
     * @param cardNumber
     * @throws ValidatorException 
     */
    private void checkLuhndigit(String cardNumber) throws ValidatorException {
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
            throw new ValidatorException(MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, WRONG_CARD_NUMBER));
        }
    }

    @Override
    public Object saveState(FacesContext context) {
        return cardTypeId;
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        cardTypeId = (String) state;
    }

    @Override
    public boolean isTransient() {
        return transientValue;
    }

    @Override
    public void setTransient(boolean newTransientValue) {
        transientValue = newTransientValue;
    }
}
