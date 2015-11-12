/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation.bean;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
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
 * @author tjd
 */
@Named("registerBean")
@RequestScoped
public class RegisterBean {

    @Inject
    private CustomerBean customerBean;
    @Inject
    private Bookstore bookstore;
    @Inject
    private LocaleBean localeBean;
    private Customer customer;
    private String password;

    @PostConstruct
    public void init() {
        customer = new Customer();
        customer.getAddress().setCountry(localeBean.getCountry());
    }

    /**
     * If the user is authenticated is redierct on the page account
     */
    public void checkIfAuthenticated() {
        //Redirect if the user is allready authenticated
        if (customerBean.isAuthenticated()) {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                ec.redirect(ec.getRequestContextPath() + "/user/account.xhtml?faces-redirect=true&menuId=3");
            } catch (IOException ex) {
                //Nothing
            }
        }
    }

    public String register() {
        try {
            bookstore.registerCustomer(customer, password);
            customerBean.setCustomer(customer);
            customerBean.setAuthenticated(true);
            return customerBean.getLoginTarget();
        } catch (BookstoreException ex) {
            //An account exist with this email
            MessageFactory.info("emailNotFree");
            return null;
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
