/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation.validator;

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

/**
 *
 * @author tjd
 */
@FacesValidator(EMailChangeValidator.VALIDATOR_ID)
public class EMailChangeValidator implements Validator {

    public static final String VALIDATOR_ID = "org.books.presentation.validator.emailChangevalidator";

    public static final String USER_EXIST = "org.books.presentation.validator.emailChangevalidator.USER_EXIST";

    @Inject
    private Bookstore bookstore;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        try {
            bookstore.findCustomer((String) value);
            //User find --> throw Exception
            FacesMessage facesMessage = MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, USER_EXIST);
            throw new ValidatorException(facesMessage);
        } catch (BookstoreException ex) {
            //No user found --> OK !
        }
    }

}
