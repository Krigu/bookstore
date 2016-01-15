/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation.bean.account;

import org.books.application.Bookstore;
import org.books.application.BookstoreException;
import org.books.data.entity.Customer;
import org.books.util.MessageFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author tjd
 */
@Named("registerBean")
@RequestScoped
public class RegisterBean {

    public static final String EMAIL_NOT_FREE = "org.books.presentation.bean.account.registerbean.EMAIL_NOT_FREE";

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
     * Add a new customer in the bookstore
     *
     * @return redirect on the account page
     */
    public String register() {
        try {
            bookstore.registerCustomer(customer, password);
            customerBean.setCustomer(customer);
            customerBean.setAuthenticated(true);
            return customerBean.goOnPageOfNavigationTarget();
        } catch (BookstoreException ex) {
            //An account exist with this email
            MessageFactory.info(EMAIL_NOT_FREE);
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