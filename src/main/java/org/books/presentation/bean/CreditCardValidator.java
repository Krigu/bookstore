/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation.bean;

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
@FacesValidator("org.books.presentation.CreditCardValidator")
public class CreditCardValidator implements Validator, StateHolder {

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
        String typeStr = type.toString();
        
        String regEx = regExMaster;
        if (typeStr.equals("Visa")) {
            regEx = regExVisa;
        }
        
        if (!value.toString().matches("\\d{16}$")) {
            FacesMessage message = MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, "formatValidationFailed");
            throw new ValidatorException(message);
        }

        String s = value.toString();
        int[] intArray=new int[16];
        for(int i=0;i<16;i++) {
             intArray[i] = Integer.parseInt(String.valueOf(s.charAt(i)));
        }
        
        if (!checkLuhn(intArray)) {
            FacesMessage message = MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, "validateCardNumberFailed");
            throw new ValidatorException(message);    
        }
        if (!value.toString().matches(regEx)) {
            FacesMessage message = MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, "validateCardNumberFailed");
            throw new ValidatorException(message);
        }
        
    }
    
   private boolean checkLuhn(int[] digits) {
       int sum = 0;
       int length = digits.length;
       for (int i = 0; i < length; i++) {
 
           // get digits in reverse order
           int digit = digits[length - i - 1];
 
           // every 2nd number multiply with 2
           if (i % 2 == 1) {
               digit *= 2;
           }
           sum += digit > 9 ? digit - 9 : digit;
       }
       return sum % 10 == 0;
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


