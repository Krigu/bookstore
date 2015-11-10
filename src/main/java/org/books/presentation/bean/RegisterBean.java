/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation.bean;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.books.application.Bookstore;
import org.books.application.BookstoreException;
import org.books.data.entity.Customer;
import org.books.presentation.CustomerBean;
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

    private Customer customer;
    private String password;

    @PostConstruct
    public void init() {
        customer = new Customer();
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
