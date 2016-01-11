package org.books.data.entity;

import org.books.data.dto.CreditCardType;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class CreditCard implements Serializable {



    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CreditCardType type;
    @Column(nullable = false)
    private String number;
    @Column(nullable = false)
    private Integer expirationMonth;
    @Column(nullable = false)
    private Integer expirationYear;

    public CreditCard() {
    }

    public CreditCard(CreditCardType type, String number, Integer expirationMonth, Integer expirationYear) {
        this.type = type;
        this.number = number;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
    }

    public CreditCard(CreditCard other) {
        this.type = other.type;
        this.number = other.number;
        this.expirationMonth = other.expirationMonth;
        this.expirationYear = other.expirationYear;
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

    public void setNumber(String number) {
        this.number = number;
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
