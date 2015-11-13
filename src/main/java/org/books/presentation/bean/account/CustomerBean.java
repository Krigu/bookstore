package org.books.presentation.bean.account;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.books.application.Bookstore;
import org.books.application.BookstoreException;
import org.books.data.entity.Customer;
import org.books.util.MessageFactory;

/**
 *
 * @author Tilmann Bück
 */
@SessionScoped
@Named("customerBean")
public class CustomerBean implements Serializable {
    
    public static final String LOGIN_FAIL ="org.books.presentation.bean.account.customerbean.LOGIN_FAIL";
    public static final String UPDATE_PROFIL_SUCCESSFUL ="org.books.presentation.bean.account.customerbean.UPDATE_PROFIL_SUCCESSFUL";
    public static final String UPDATE_PROFIL_FAIL ="org.books.presentation.bean.account.customerbean.UPDATE_PROFIL_FAIL";
    
    private String email;
    private String password;
    private boolean authenticated = false;
    private String loginTarget;
    private String detailsTarget;
    @Inject
    private Bookstore bookstore;
    private Customer customer;
    @Inject
    private LocaleBean localeBean;
    private String countryDisplayName;
    
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
     * @return the target navigation
     */
    public String login() {
        try {
            bookstore.authenticateCustomer(email, password);
            customer = bookstore.findCustomer(email);
            authenticated = true;
            return getLoginTarget();
        } catch (BookstoreException ex) {
            //user doesn't exist or the password is wrong
            authenticated = false;
            MessageFactory.error(LOGIN_FAIL);
            return null;
        }
    }
    /**
     * Use this methode to logout
     * @return 
     */
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/catalogSearch?faces-redirect=true&menuId=0";
    }
    /**
     * Use this method to update the information of the customer
     * @return 
     */
    public String updateCustomer() {
        try {
            bookstore.updateCustomer(customer);
            MessageFactory.info(UPDATE_PROFIL_SUCCESSFUL);
        } catch (BookstoreException ex) {
            Logger.getLogger(CustomerBean.class.getName()).log(Level.SEVERE, null, ex);
            MessageFactory.error(UPDATE_PROFIL_FAIL);
        }
        return detailsTarget;
    }

    public String editCustomerDetailsFromAccount() {
        detailsTarget = "/user/account?faces-redirect=true&menuId=3";
        return "customerDetails?faces-redirect=true&menuId=3";
    }

    public String editCustomerDetailsFromCheckout() {
        detailsTarget = "/user/orderSummary?faces-redirect=true&menuId=2";
        return "customerDetails?faces-redirect=true&menuId=3";
    }

    public String goOnPageCustomerDetails(){
        return "/user/customerDetails?faces-redirect=true&menuId=3";
    }

    public String goOnPageChangePassword(){
        return "/user/changePassword?faces-redirect=true&menuId=3";
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
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public boolean isAuthenticated() {
        return authenticated;
    }
    
    public void setLoginTarget(String loginTarget) {
        this.loginTarget = loginTarget;
    }
    
    public String getCountryDisplayName() {
        countryDisplayName = localeBean.getCountryDisplayName(customer.getAddress().getCountry());
        return countryDisplayName;
    }
    
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
    
    public String getLoginTarget() {
        if (loginTarget == null || loginTarget.isEmpty()) {
            loginTarget = "user/account?faces-redirect=true&menuId=3";
        }
        return loginTarget;
    }
    
    public String getDetailsTarget() {
        return detailsTarget;
    }
}
