package org.books.presentation;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.books.application.Bookstore;
import org.books.application.BookstoreException;
import org.books.application.ShoppingCart;
import org.books.application.ShoppingCartItem;
import org.books.data.dto.BookInfo;
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

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }
    public String removeFromShoppingCart(ShoppingCartItem item) {
        shoppingCart.remove(item);
        return null;
    }
    
    public String addBookToShoppingCart(Book book) {
        shoppingCart.add(new BookInfo(book));
        return null;
    }

    public String addBookInfoToShoppingCart(BookInfo bookInfo) {
        shoppingCart.add(bookInfo);
        return null;
    }
}
