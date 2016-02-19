package org.books.data.dto;

import org.books.data.entity.Customer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlRootElement(name = "customerInfo")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"number", "email", "firstName", "lastName"})
public class CustomerInfo implements Serializable {

    private String email;
    private String firstName;
    private String lastName;
    private String number;

    public CustomerInfo() {
    }

    public CustomerInfo(String email, String firstName, String lastName, String number) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
    }

    public CustomerInfo(Customer customer) {
        this.email = customer.getEmail();
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.number = customer.getNumber();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
