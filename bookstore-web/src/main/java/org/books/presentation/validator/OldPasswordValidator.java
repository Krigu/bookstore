package org.books.presentation.validator;

/*import org.books.application.Bookstore;
import org.books.application.BookstoreException;*/
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import org.books.presentation.bean.account.CustomerBean;
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
import org.books.application.exception.InvalidPasswordException;
import org.books.presentation.bean.account.RegisterBean;

/**
 *
 * @author tjd
 */
@FacesValidator(OldPasswordValidator.VALIDATOR_ID)
public class OldPasswordValidator implements Validator {

    public static final String VALIDATOR_ID = "org.books.presentation.validator.oldpasswordvalidator";
    public static final String WRONG_OLD_PASSWORD = "org.books.presentation.validator.oldpasswordvalidator.WRONG_OLD_PASSWORD";

    private static final Logger LOGGER = Logger.getLogger(OldPasswordValidator.class.getName());
    
    @Inject
    private CustomerBean customerBean;
    //@Inject
    //private Bookstore bookstore;
    @EJB
    private CustomerService customerService;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        try {
            /*try {
            bookstore.authenticateCustomer(customerBean.getCustomer().getEmail(), (String) value);
            } catch (BookstoreException ex) {
            FacesMessage facesMessage = MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, WRONG_OLD_PASSWORD);
            throw new ValidatorException(facesMessage);
            }*/
            customerService.authenticateCustomer(customerBean.getCustomer().getEmail(), (String) value);
        } catch (CustomerNotFoundException | InvalidPasswordException ex) {
            LOGGER.log(Level.SEVERE, "Fail to change the password", ex);
            FacesMessage facesMessage = MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, WRONG_OLD_PASSWORD);
            throw new ValidatorException(facesMessage);
        }
    }

}
