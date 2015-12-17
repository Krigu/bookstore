package org.books.data.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NamedQueries({
    @NamedQuery(name = Order.FIND_BY_NUMBER, query = "from Order o where UPPER(o.number) = UPPER(:number)"),
    @NamedQuery(name = Order.FIND_BY_CUSTOMER_AND_YEAR, query = "SELECT NEW org.books.data.dto.OrderInfo(o) from Order o where o.customer = :customer AND EXTRACT(YEAR from o.date) = :year")
})
@Entity
@Table(name="command")
public class Order extends BaseEntity {

    public static final String FIND_BY_NUMBER = "Order.findByNumber";
    public static final String FIND_BY_CUSTOMER_AND_YEAR = "Order.findByCustomerAndYear";
    
    public enum Status {

        accepted, processing, shipped, canceled
    }

    @Column(nullable = false, name = "command_number")
    private String number;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Embedded
    private Address address;

    @Embedded
    private CreditCard creditCard;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items = new ArrayList<>();

    public Order() {
    }

    public Order(String number, Date date, BigDecimal amount, Status status,
            Customer customer, Address address, CreditCard creditCard, List<OrderItem> items) {
        this.number = number;
        this.date = date;
        this.amount = amount;
        this.status = status;
        this.customer = customer;
        this.address = address;
        this.creditCard = creditCard;
        this.items = items;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard card) {
        this.creditCard = card;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
