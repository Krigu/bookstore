package org.books.presentation.bean.account;

import org.books.util.MessageFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import org.books.application.CustomerService;
import org.books.application.exception.CustomerNotFoundException;

/**
 *
 * @author tjd
 */
@RequestScoped
@Named("changePasswordBean")
public class ChangePasswordBean implements Serializable {

    public static String PASSWORD_CHANGED_SUCCESSFUL = "org.books.presentation.bean.account.changepasswordbean.PASSWORD_CHANGED_SUCCESSFUL";
    public static String PASSWORD_NOT_CHANGED = "org.books.presentation.bean.account.changepasswordbean.PASSWORD_NOT_CHANGED";

    private static final Logger LOGGER = Logger.getLogger(ChangePasswordBean.class.getName());
    
    private String newPassword;
    @Inject
    private CustomerBean customerBean;
    @EJB
    private CustomerService customerService;

    /**
     * Use this methode to update the password
     * @return null - No navigation
     */
    public String updatePassword() {
        try {
            customerService.changePassword(customerBean.getCustomer().getEmail(), newPassword);
            MessageFactory.info(PASSWORD_CHANGED_SUCCESSFUL);
        } catch (CustomerNotFoundException ex) {
            //TODO update the message to CUSTOMER NOT FOUND
            LOGGER.log(Level.SEVERE, "Customer not found", ex);
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
