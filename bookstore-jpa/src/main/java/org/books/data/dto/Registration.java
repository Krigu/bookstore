package org.books.data.dto;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "registration")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"customer", "password"})
public class Registration {

    @NotNull
    private CustomerDTO customer;

    @NotNull
    private String password;

    public Registration() {
    }

    public Registration(CustomerDTO customer, String password) {
        this.customer = customer;
        this.password = password;
    }

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

    @Override
    public String toString() {
        return "Registration{" +
                "customer=" + customer +
                ", password='" + password + '\'' +
                '}';
    }
}
