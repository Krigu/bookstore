package org.books.data.dto;


import java.io.Serializable;

public class CustomerDTO implements Serializable {

    private String email;

    private String firstName;

    private String lastName;

    private String customerNumber;

    private AddressDTO adress;

    private CreditCardDTO creditCard;

    public CustomerDTO() {
    }

    public CustomerDTO(String email, String firstName, String lastName, String customerNumber, AddressDTO adress, CreditCardDTO creditCard) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.customerNumber = customerNumber;
        this.adress = adress;
        this.creditCard = creditCard;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public AddressDTO getAddress() {
        return adress;
    }

    public void setAdress(AddressDTO adress) {
        this.adress = adress;
    }

    public CreditCardDTO getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCardDTO creditCard) {
        this.creditCard = creditCard;
    }
}
