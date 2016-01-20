/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation.bean.account;

/*import org.books.application.Bookstore;
import org.books.application.BookstoreException;
import org.books.data.entity.Customer;*/
import java.util.logging.Level;
import java.util.logging.Logger;
import org.books.util.MessageFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.books.application.CustomerService;
import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.data.dto.CustomerDTO;

/**
 *
 * @author tjd
 */
@Named("registerBean")
@RequestScoped
public class RegisterBean {

    public static final String EMAIL_NOT_FREE = "org.books.presentation.bean.account.registerbean.EMAIL_NOT_FREE";
    
    private static final Logger LOGGER = Logger.getLogger(RegisterBean.class.getName());

    @Inject
    private CustomerBean customerBean;
    @EJB
    private CustomerService customerService;
    //@Inject
    //private Bookstore bookstore;
    @Inject
    private LocaleBean localeBean;
    //private Customer customer;
    private CustomerDTO customer;
    private String password;

    @PostConstruct
    public void init() {
        //customer = new Customer();
        //customer.getAddress().setCountry(localeBean.getCountry());
        customer = new CustomerDTO();
        customer.getAddress().setCountry(localeBean.getCountry());
    }

    /**
     * Add a new customer in the bookstore
     *
     * @return redirect on the account page
     */
    public String register() {
        
            /*try {
            bookstore.registerCustomer(customer, password);
            customerBean.setCustomer(customer);
            customerBean.setAuthenticated(true);
            return customerBean.goOnPageOfNavigationTarget();
            } catch (BookstoreException ex) {
            //An account exist with this email
            MessageFactory.info(EMAIL_NOT_FREE);
            return null;
            }*/
        try {
            customer = customerService.registerCustomer(customer, password);
            customerBean.setCustomer(customer);
            customerBean.setAuthenticated(true);
            return customerBean.goOnPageOfNavigationTarget();
        } catch (CustomerAlreadyExistsException ex) {
            //An account exist with this email
            LOGGER.log(Level.WARNING, "Customer already exist", ex);
            MessageFactory.info(EMAIL_NOT_FREE);
        } 
        return null;
    }

    /*public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }*/

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }
    

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}