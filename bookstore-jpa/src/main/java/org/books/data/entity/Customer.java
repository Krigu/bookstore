package org.books.data.entity;

import javax.persistence.*;


@NamedQueries({
        @NamedQuery(name = "Customer.findByLastNameOrFirstName",
                query = "from Customer c where UPPER(c.firstName) like UPPER(:firstName) or UPPER(c.lastName) like UPPER(:lastName)")
})
@Entity
public class Customer extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Embedded
    private Address address;

    @Embedded
    private CreditCard creditCard;

    public Customer() {
    }

    public Customer(String email, String firstName, String lastName, Address address, CreditCard creditCard) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.creditCard = creditCard;
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

    public Address getAddress() {
        if (address == null) {
            address = new Address();
        }
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public CreditCard getCreditCard() {
        if (creditCard == null) {
            creditCard = new CreditCard();
        }
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }
}
