/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import org.books.application.Bookstore;
import org.books.application.BookstoreException;
import org.books.presentation.CustomerBean;
import org.books.util.MessageFactory;

/**
 *
 * @author tjd
 */
@FacesValidator("org.books.presentation.validator.oldPasswordValidator")
public class OldPasswordValidator implements Validator {

    @Inject
    private CustomerBean customerBean;
    @Inject
    private Bookstore bookstore;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        System.out.println("org.books.presentation.ChangePasswordBean.checkOldPassword()");
        try {
            bookstore.authenticateCustomer(customerBean.getCustomer().getEmail(), (String) value);
        } catch (BookstoreException ex) {
            FacesMessage facesMessage = MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, "wrongOldPassword");
            System.out.println("org.books.presentation.ChangePasswordBean.checkOldPassword() FAILDE");
            throw new ValidatorException(facesMessage);
        }
        System.out.println("org.books.presentation.ChangePasswordBean.checkOldPassword() Success");
    }

}
