/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation.bean.account;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.books.application.Bookstore;
import org.books.util.MessageFactory;
import org.books.application.BookstoreException;

/**
 *
 * @author tjd
 */
//@SessionScoped
@RequestScoped
@Named("changePasswordBean")
public class ChangePasswordBean implements Serializable {

    public static String PASSWORD_CHANGED_SUCCESSFUL = "org.books.presentation.bean.account.changepasswordbean.PASSWORD_CHANGED_SUCCESSFUL";
    public static String PASSWORD_NOT_CHANGED = "org.books.presentation.bean.account.changepasswordbean.PASSWORD_NOT_CHANGED";

    private String newPassword;
    @Inject
    private CustomerBean customerBean;
    @Inject
    private Bookstore bookstore;

    /**
     * Use this methode to update the password
     * @return null - No navigation
     */
    public String updatePassword() {
        try {
            bookstore.changePassword(customerBean.getCustomer().getEmail(), newPassword);
            MessageFactory.info(PASSWORD_CHANGED_SUCCESSFUL);
        } catch (BookstoreException ex) {
            //Nothing
            MessageFactory.info(PASSWORD_NOT_CHANGED);
        }
        return null;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

}
