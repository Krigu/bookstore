package org.books.application;

import org.books.data.dto.BookInfo;
import org.books.data.entity.Book;

import java.math.BigDecimal;

/**
 *
 * @author Tilmann Bück
 */
public class ShoppingCartItem {
    private BookInfo bookInfo;
    private int quantity;

    public ShoppingCartItem(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
        this.quantity = 1;
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getAmount() {
        return bookInfo.getPrice().multiply(new BigDecimal(quantity));
    }

    public void increaseQuantity() {
        quantity++;
    }

}
