package org.books.data.dto;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlRootElement(name = "customer")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"number", "email", "firstName", "lastName", "address", "creditCard"})
public class CustomerDTO implements Serializable {

    @NotNull
    private String email;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    private String number;
    @NotNull
    @Valid
    private AddressDTO address;

    @NotNull
    @Valid
    private CreditCardDTO creditCard;

    public CustomerDTO() {
        address = new AddressDTO();
        creditCard = new CreditCardDTO();
    }


    public CustomerDTO(String email, String firstName, String lastName, String number, AddressDTO addressDTO, CreditCardDTO creditCard) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
        this.address = addressDTO;
        this.creditCard = creditCard;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public CreditCardDTO getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCardDTO creditCard) {
        this.creditCard = creditCard;
    }
}
