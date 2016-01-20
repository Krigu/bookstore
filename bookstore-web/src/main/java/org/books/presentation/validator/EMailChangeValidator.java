package org.books.presentation.validator;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import org.books.application.Bookstore;
import org.books.application.BookstoreException;
import org.books.util.MessageFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import org.books.application.CustomerService;
import org.books.application.exception.CustomerNotFoundException;

/**
 *
 * @author tjd
 */
@FacesValidator(EMailChangeValidator.VALIDATOR_ID)
public class EMailChangeValidator implements Validator {

    public static final String VALIDATOR_ID = "org.books.presentation.validator.emailChangevalidator";
    public static final String USER_EXIST = "org.books.presentation.validator.emailChangevalidator.USER_EXIST";

    private static final Logger LOGGER = Logger.getLogger(EMailChangeValidator.class.getName());

    //@Inject
    //private Bookstore bookstore;
    @EJB
    private CustomerService customerService;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        /*try {
            bookstore.findCustomer((String) value);
            //User find --> throw Exception
            FacesMessage facesMessage = MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, USER_EXIST);
            throw new ValidatorException(facesMessage);
            } catch (BookstoreException ex) {
            //No user found --> OK !
            }*/
        try {
            customerService.findCustomerByEmail((String) value);
            //User find --> throw Exception
            FacesMessage facesMessage = MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, USER_EXIST);
            throw new ValidatorException(facesMessage);
        } catch (CustomerNotFoundException ex) {
            //No user found --> OK !
        }
    }

}
