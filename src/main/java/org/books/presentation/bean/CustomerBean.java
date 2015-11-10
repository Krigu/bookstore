package org.books.presentation.bean;

import org.books.presentation.bean.LocaleBean;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
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
    
    public String logout() {
        authenticated = false;
        customer = null;
        email = null;
        password = null;
        return "/login?faces-redirect=true&menuId=3";
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
