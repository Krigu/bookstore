package org.books.presentation;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
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
    private String email;
    private String password;
    private boolean authenticated = false;
    @Inject
    private Bookstore bookstore;
    private Customer customer;



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
    
    public String login() {
        try {
            bookstore.authenticateCustomer(email, password);
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
    
    @PostConstruct
    public void init() {
        customer = new Customer();
    }
}

