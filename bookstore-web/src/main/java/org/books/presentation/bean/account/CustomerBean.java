package org.books.presentation.bean.account;

import org.books.util.MessageFactory;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import org.books.application.CustomerService;
import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.InvalidPasswordException;
import org.books.data.dto.CustomerDTO;

/**
 *
 * @author Tilmann Bück
 */
@SessionScoped
@Named("customerBean")
public class CustomerBean implements Serializable {

    public static final String CUSTOMER_NOT_FOUND = "org.books.presentation.bean.account.customerbean.CUSTOMER_NOT_FOUND";
    public static final String CUSTOMER_ALREADY_EXISTS = "org.books.presentation.bean.account.customerbean.CUSTOMER_ALREADY_EXISTS";
    public static final String INVALID_PASSWORD = "org.books.presentation.bean.account.customerbean.INVALID_PASSWORD";
    public static final String UPDATE_PROFIL_SUCCESSFUL = "org.books.presentation.bean.account.customerbean.UPDATE_PROFIL_SUCCESSFUL";

    private static final Logger LOGGER = Logger.getLogger(CustomerBean.class.getName());

    private String email;
    private String password;
    private boolean authenticated = false;
    private String navigationTarget;
    @Inject
    private LocaleBean localeBean;
    private String countryDisplayName;
    @EJB
    private CustomerService customerService;
    private CustomerDTO customer;

    /**
     * If the user is authenticated is redierct on the page account
     */
    public void checkIfAuthenticated() {
        //Redirect if the user is allready authenticated
        if (isAuthenticated()) {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                ec.redirect(ec.getRequestContextPath() + "/user/account.xhtml?faces-redirect=true&menuId=3");
            } catch (IOException ex) {
                //Nothing
            }
        }
    }

    /**
     * Use this method to authentificated an user
     *
     * @return the target navigation
     */
    public String login() {
        try {
            customerService.authenticateCustomer(email, password);
            authenticated = true;
            customer = customerService.findCustomerByEmail(email);
            return goOnPageOfNavigationTarget();
        } catch (CustomerNotFoundException ex) {
            LOGGER.log(Level.WARNING, "Customer not found", ex);
            MessageFactory.error(CUSTOMER_NOT_FOUND);
        } catch (InvalidPasswordException ex) {
            LOGGER.log(Level.WARNING, "Invalide password", ex);
            MessageFactory.error(INVALID_PASSWORD);
        }
        return null;
    }

    /**
     * Use this methode to logout
     *
     * @return
     */
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/catalogSearch?faces-redirect=true&menuId=0";
    }

    /**
     * Use this method to update the information of the customer
     *
     * @return
     */
    public String updateCustomer() {
        try {
            customerService.updateCustomer(customer);
            MessageFactory.info(UPDATE_PROFIL_SUCCESSFUL);
        } catch (CustomerNotFoundException ex) {
            LOGGER.log(Level.WARNING, "Customer not found", ex);
            MessageFactory.error(CUSTOMER_NOT_FOUND);
        } catch (CustomerAlreadyExistsException ex) {
            LOGGER.log(Level.WARNING, "Customer already exist", ex);
            MessageFactory.error(RegisterBean.EMAIL_NOT_FREE);
        }
        //return navigationTarget;
        return goOnPageOfNavigationTarget();
    }

    public String goOnPageCustomerDetailsFromAccount() {
        navigationTarget = "/user/account?faces-redirect=true&menuId=3";
        return "customerDetails?faces-redirect=true&menuId=3";
    }

    public String goOnPageCustomerDetailsFromCheckout() {
        navigationTarget = "/user/orderSummary?faces-redirect=true&menuId=2";
        return "customerDetails?faces-redirect=true&menuId=3";
    }

    public String goOnPageChangePassword() {
        navigationTarget = "/user/account?faces-redirect=true&menuId=3";
        return "/user/changePassword?faces-redirect=true&menuId=3";
    }

    public String goOnPageOfNavigationTarget() {
        if (navigationTarget == null || navigationTarget.isEmpty()) {
            navigationTarget = "user/account?faces-redirect=true&menuId=3";
        }
        return navigationTarget;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getCountryDisplayName() {
        countryDisplayName = localeBean.getCountryDisplayName(customer.getAddress().getCountry());
        return countryDisplayName;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public void setNavigationTarget(String navigationTarget) {
        this.navigationTarget = navigationTarget;
    }
}
