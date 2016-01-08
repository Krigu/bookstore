package org.books.data.dto;


import org.books.data.entity.CreditCard;

import java.io.Serializable;

public class CreditCardDTO implements Serializable {

    private CreditCard.Type type;

    private String number;

    private Integer expirationMonth;

    private Integer expirationYear;

    public CreditCardDTO() {
    }

    public CreditCardDTO(CreditCard.Type type, String cardNumber, Integer expirationMonth, Integer expirationYear) {
        this.type = type;
        this.number = cardNumber;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
    }

    public CreditCard.Type getType() {
        return type;
    }

    public void setType(CreditCard.Type type) {
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
