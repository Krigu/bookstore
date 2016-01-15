package org.books.data.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class OrderItem extends BaseEntity {

    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity;

    public OrderItem() {
    }

    public OrderItem(Book book, BigDecimal price, Integer quantity) {
        this.book = book;
        this.price = price;
        this.quantity = quantity;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
