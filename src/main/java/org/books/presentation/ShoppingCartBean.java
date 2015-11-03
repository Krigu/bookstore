package org.books.presentation;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.books.application.Bookstore;
import org.books.application.BookstoreException;
import org.books.application.ShoppingCart;
import org.books.application.ShoppingCartItem;
import org.books.data.entity.Book;

/**
 *
 * @author Tilmann Bück
 */

@SessionScoped
@Named("shoppingCartBean")
public class ShoppingCartBean implements Serializable {
    
    @Inject 
    private ShoppingCart shoppingCart;

    @Inject
    private Bookstore bookstore;
    
    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }
    public String removeFromShoopingCart(ShoppingCartItem item) {
        shoppingCart.remove(item);
        return null;
    }
    
    public String addToShoopingCart(Book book) {
        shoppingCart.add(book);
        return null;
    }

    public String addToShoopingCart(String isbn) {
        try {
            Book book = bookstore.findBook(isbn);
            shoppingCart.add(book);
        } catch (BookstoreException e) {
            return null;
        }


        return null;
    }
}
