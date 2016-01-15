package org.books.data.dto;


import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class AddressDTO implements Serializable {

    @NotNull
    private String street;

    @NotNull
    private String postalCode;

    @NotNull
    private String city;

    @NotNull
    private String country;

    public AddressDTO() {
    }

    public AddressDTO(AddressDTO other) {
        this.street = other.street;
        this.postalCode = other.postalCode;
        this.city = other.city;
        this.country = other.country;
    }

    public AddressDTO(String street, String postalCode, String city, String country) {
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
