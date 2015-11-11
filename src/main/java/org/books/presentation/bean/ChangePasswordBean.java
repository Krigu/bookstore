/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation.bean;

import org.books.presentation.bean.CustomerBean;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.books.application.Bookstore;
import org.books.util.MessageFactory;
import javax.enterprise.context.SessionScoped;
import org.books.application.BookstoreException;

/**
 *
 * @author tjd
 */
//@SessionScoped
@RequestScoped
@Named("changePasswordBean")
public class ChangePasswordBean implements Serializable {

    private String newPassword;
    @Inject
    private CustomerBean customerBean;
    @Inject
    private Bookstore bookstore;
    
    public void updatePassword(){
        try {
            bookstore.changePassword(customerBean.getCustomer().getEmail(), newPassword);
            MessageFactory.info("passwordChanged");
        } catch (BookstoreException ex) {
           //Nothing
        }
    }

   public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
    
}
