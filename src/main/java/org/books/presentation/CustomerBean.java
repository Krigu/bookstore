package org.books.presentation;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.books.application.Bookstore;
import org.books.application.BookstoreException;
import org.books.data.dto.OrderInfo;
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
    private String countryDisplayName;
    private String email;
    private String password;
    private boolean authenticated = false;
    @Inject
    private Bookstore bookstore;
    private Customer customer;
    @Inject
    private LocaleBean localeBean;
    
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
    
    public String createCustomer() {
        return "account";
    }

    public String getCountryDisplayName() {
        countryDisplayName = localeBean.getCountryDisplayName(customer.getAddress().getCountry());
        return countryDisplayName;
    }

    public String login() {
        try {
            bookstore.authenticateCustomer(email, password);
            customer = bookstore.findCustomer(email);
            authenticated = true;
            return "account?faces-redirect=true";
        }
        catch (BookstoreException ex) {
            authenticated = false;
            MessageFactory.info("authenticationFailed");
            return null;    
        }
  
    }
    
    public String register() {
        customer = new Customer();
        customer.setEmail(email);
        return "customerDetails";
    }
    
    public String insertNewCustomer() {
        try {
            bookstore.registerCustomer(customer, password);
            authenticated = true;
            return "account?faces-redirect=true";
        }
        catch (BookstoreException ex) {
            return null;
        }
    }

    public String updateCustomer() {
        //Update customer in DB
        try {
            bookstore.updateCustomer(customer);
            return "account?faces-redirect=true";
        }
        catch (BookstoreException ex) {
            return null;
        }
        
    }
 
    public String checkAuthentication() {
        if (authenticated)
            return "account?faces-redirect=true";

        return "login?faces-redirect=true";
    }

    public String changePassword() {
        try {
            bookstore.changePassword(email, password);
            MessageFactory.info("passwordChanged");
            return "account";
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public String logout() {
        authenticated = false;
        customer = null;
        email = null;
        password = null;
        return "login";
    }
    
}

