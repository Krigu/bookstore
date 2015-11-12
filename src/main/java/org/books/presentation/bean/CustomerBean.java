package org.books.presentation.bean;

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
import org.books.data.entity.CreditCard.Type;
import org.books.data.entity.Customer;
import org.books.util.MessageFactory;

/**
 *
 * @author Tilmann Bück
 */
@SessionScoped
@Named("customerBean")
public class CustomerBean implements Serializable {
    
    private String email;
    private String password;
    private boolean authenticated = false;
    private String loginTarget;
    @Inject
    private Bookstore bookstore;
    private Customer customer;
    @Inject
    private LocaleBean localeBean;
    private String countryDisplayName;
    private final String defaultCountry = "CH";
    
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
    
    public String login() {
        try {
            bookstore.authenticateCustomer(email, password);
            customer = bookstore.findCustomer(email);
            authenticated = true;
            return getLoginTarget();
        } catch (BookstoreException ex) {
            //user doesn't exist or the password is wrong
            authenticated = false;
            MessageFactory.error("authenticationFailed");
            return null;
        }
    }
    
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login?faces-redirect=true&menuId=3";
    }
    
    public String updateCustomer() {
        try {
            bookstore.updateCustomer(customer);
            MessageFactory.info("profileUpdatedSuccessful");
        } catch (BookstoreException ex) {
            Logger.getLogger(CustomerBean.class.getName()).log(Level.SEVERE, null, ex);
            MessageFactory.error("profileNotUpdatede");
        }
        return null;
    }
    
    public Type[] getCreditCardTypes() {
        return Type.values();
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
    
    public String createCustomer() {
        return "account";
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
    
}
