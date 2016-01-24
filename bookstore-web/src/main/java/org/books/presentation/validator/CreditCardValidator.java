package org.books.presentation.validator;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import org.books.util.MessageFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.books.application.CreditCardValidatorRemote;
import org.books.application.exception.CreditCardValidationException;

/**
 *
 * @author Tilmann Bück
 */
@FacesValidator(CreditCardValidator.VALIDATOR_ID)
public class CreditCardValidator implements Validator, StateHolder {

    public static final String VALIDATOR_ID = "org.books.presentation.validator.creditcardvalidator";

    public static final String CREDIT_CARD_WRONG_FORMAT = "org.books.presentation.validator.creditcardvalidator.CREDIT_CARD_WRONG_FORMAT";
    public static final String CREDIT_CARD_WRONG_NUMBER = "org.books.presentation.validator.creditcardvalidator.CREDIT_CARD_WRONG_NUMBER";
    public static final String CREDIT_CARD_EXPIRED = "org.books.presentation.validator.creditcardvalidator.CREDIT_CARD_EXPIRED";

    private static final Logger LOGGER = Logger.getLogger(CreditCardValidator.class.getName());
    
    private String cardTypeId;
    boolean transientValue = false;
    @EJB
    private CreditCardValidatorRemote creditCardValidatorRemote;

    public void setCardTypeId(String cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        UIInput ccType = (UIInput) component.findComponent(cardTypeId);
        Object type = ccType.getValue();
        String cardType = type.toString();
        String cardNumber = (String) value;

        try {
            creditCardValidatorRemote.validateCreditCard(cardNumber, cardType);
        } catch (CreditCardValidationException ex) {
            LOGGER.log(Level.WARNING, "Card not valide", ex);
            FacesMessage message = null;
            switch (ex.getCode()) {
                case INVALID_CREDIT_CARD_NUMBER:
                    message = MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, CREDIT_CARD_WRONG_NUMBER); 
                    break;
                case INVALID_CREDIT_CARD_FORMAT:
                    message = MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, CREDIT_CARD_WRONG_FORMAT);   
                    break;
                case CREDIT_CARD_EXPIRED:
                    message = MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, CREDIT_CARD_EXPIRED);   
                    break;
                default:
                    //Nothing       
            }
            throw new ValidatorException(message);
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
