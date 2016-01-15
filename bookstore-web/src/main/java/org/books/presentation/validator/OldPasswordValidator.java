/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation.validator;

import org.books.application.Bookstore;
import org.books.application.BookstoreException;
import org.books.presentation.bean.account.CustomerBean;
import org.books.util.MessageFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

/**
 *
 * @author tjd
 */
@FacesValidator(OldPasswordValidator.VALIDATOR_ID)
public class OldPasswordValidator implements Validator {

    public static final String VALIDATOR_ID = "org.books.presentation.validator.oldpasswordvalidator";
    public static final String WRONG_OLD_PASSWORD = "org.books.presentation.validator.oldpasswordvalidator.WRONG_OLD_PASSWORD";

    @Inject
    private CustomerBean customerBean;
    @Inject
    private Bookstore bookstore;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        try {
            bookstore.authenticateCustomer(customerBean.getCustomer().getEmail(), (String) value);
        } catch (BookstoreException ex) {
            FacesMessage facesMessage = MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, WRONG_OLD_PASSWORD);
            throw new ValidatorException(facesMessage);
        }
    }

}
