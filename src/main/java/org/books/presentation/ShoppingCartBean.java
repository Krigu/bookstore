package org.books.presentation;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.books.application.ShoppingCart;
import org.books.application.ShoppingCartItem;
import org.books.data.dto.BookInfo;
import org.books.data.entity.Book;
import org.books.util.MessageFactory;
import org.primefaces.event.RowEditEvent;

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
        MessageFactory.info("bookRemoved", item.getBookInfo().getTitle());
        return null;
    }

    public String addBookToShoppingCart(Book book) {
        return addBookInfoToShoppingCart(new BookInfo(book));
    }

    public String addBookInfoToShoppingCart(BookInfo bookInfo) {
        shoppingCart.add(bookInfo);
        MessageFactory.info("bookAdded", bookInfo.getTitle());
        return null;
    }
    
    public void onRowEdit(RowEditEvent event) {
        ShoppingCartItem item = (ShoppingCartItem)event.getObject();
        MessageFactory.info("shoppingCartUpdateBookQuantity", item.getBookInfo().getTitle(),item.getQuantity());
    }
    /**
     *
     * @param bookInfo
     * @return true if the shopping cart contains the bookInfo else return false
     */
    public boolean shoppingCartContainsBookInfo(BookInfo bookInfo) {
        ShoppingCartItem item = findShoppingCartItem(bookInfo);
        return item!=null;
    }
    
        /**
     *
     * @param book
     * @return true if the shopping cart contains the book else return false
     */
    public boolean shoppingCartContainsBook(Book book) {
        ShoppingCartItem item = findShoppingCartItem(new BookInfo(book));
        return item!=null;
    }

    /**
     * Remove a bookInfo from the shopping cart
     * @param bookInfo
     * @return
     */
    public String removeBookInfoFromShoppingCart(BookInfo bookInfo) {
        ShoppingCartItem item = findShoppingCartItem(bookInfo);
        if (item != null) {
            return removeFromShoppingCart(item);
        }
        return null;
    }
    /**
     * Remove a book from the shopping cart
     * @param book
     * @return
     */
    public String removeBookFromShoppingCart(Book book) {
        return removeBookInfoFromShoppingCart(new BookInfo(book));
    }

    /**
     *
     * @param book
     * @return the shoppingCartItem of the bookInfo or null if the bookInfo isn't in the
     * shoppingCart
     */
    private ShoppingCartItem findShoppingCartItem(BookInfo bookInfo) {
        for (ShoppingCartItem item : shoppingCart.getItems()) {
            if (item.getBookInfo().equals(bookInfo)) {
                return item;
            }
        }
        return null;
    }
}
