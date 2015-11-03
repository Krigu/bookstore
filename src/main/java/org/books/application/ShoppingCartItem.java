package org.books.application;

import org.books.data.entity.Book;

import java.math.BigDecimal;

/**
 *
 * @author Tilmann Bück
 */
public class ShoppingCartItem {
    private Book book;
    private int quantity;

    public ShoppingCartItem(Book book) {
        this.book = book;
        this.quantity = 1;        
    }
    
    public void setBook(Book book) {
        this.book = book;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Book getBook() {
        return book;
    }

    public int getQuantity() {
        return quantity;
    }
    
    public BigDecimal getAmount() {
        return book.getPrice().multiply(new BigDecimal(quantity));
    }
    
    public void increaseQuantity() {
        quantity++;
    }
    
}
