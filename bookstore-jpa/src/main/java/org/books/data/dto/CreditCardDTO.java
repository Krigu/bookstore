package org.books.data.dto;


import java.io.Serializable;

public class CreditCardDTO implements Serializable {


    private CreditCardType type;

    private String number;

    private Integer expirationMonth;

    private Integer expirationYear;

    public CreditCardDTO() {
    }

    public CreditCardDTO(CreditCardDTO other) {
        this.type = other.type;
        this.number = other.number;
        this.expirationMonth = other.expirationMonth;
        this.expirationYear = other.expirationYear;
    }

    public CreditCardDTO(CreditCardType type, String number, Integer expirationMonth, Integer expirationYear) {
        this.type = type;
        this.number = number;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
    }

    public CreditCardType getType() {
        return type;
    }

    public void setType(CreditCardType type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String cardNumber) {
        this.number = cardNumber;
    }

    public Integer getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(Integer expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public Integer getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(Integer expirationYear) {
        this.expirationYear = expirationYear;
    }
}
