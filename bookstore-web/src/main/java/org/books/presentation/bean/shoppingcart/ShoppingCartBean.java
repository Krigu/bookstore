package org.books.presentation.bean.shoppingcart;

import org.books.application.ShoppingCart;
import org.books.data.dto.BookInfo;
import org.books.data.dto.OrderItemDTO;
import org.books.util.MessageFactory;
import org.primefaces.event.RowEditEvent;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import org.books.data.dto.BookDTO;

/**
 *
 * @author Tilmann Bück
 */
@SessionScoped
@Named("shoppingCartBean")
public class ShoppingCartBean implements Serializable {

    public static final String BOOK_REMOVED = "org.books.presentation.bean.shoppingcart.BOOK_REMOVED";
    public static final String BOOK_ADDED = "org.books.presentation.bean.shoppingcart.BOOK_ADDED";
    public static final String UPDATE_BOOK_QUANTITY = "org.books.presentation.bean.shoppingcart.UPDATE_BOOK_QUANTITY";

    @Inject
    private ShoppingCart shoppingCart;

    /**
     * Remove an item of the shopping cart
     *
     * @param item
     * @return null - No navigation
     */
    public String removeFromShoppingCart(OrderItemDTO item) {
        shoppingCart.remove(item);
        MessageFactory.info(BOOK_REMOVED, item.getBook().getTitle());
        return null;
    }

    /**
     * Add a book to the shopping cart
     *
     * @param book
     * @return @see addBookInfoToShoppingCart
     */
    public String addBookToShoppingCart(BookDTO book) {
        return addBookInfoToShoppingCart(new BookInfo(book));
    }

    /**
     * Add a bookInfo to the shopping cart
     *
     * @param bookInfo
     * @return null - No navigation
     */
    public String addBookInfoToShoppingCart(BookInfo bookInfo) {
        shoppingCart.add(bookInfo);
        MessageFactory.info(BOOK_ADDED, bookInfo.getTitle());
        return null;
    }

    /**
     * Event when we modify a row of the shopping cart
     * @param event 
     */
    public void onRowEdit(RowEditEvent event) {
        OrderItemDTO item = (OrderItemDTO) event.getObject();
        MessageFactory.info(UPDATE_BOOK_QUANTITY, item.getBook().getTitle(), item.getQuantity());
    }

    /**
     *
     * @param bookInfo
     * @return true if the shopping cart contains the bookInfo else return false
     */
    public boolean shoppingCartContainsBookInfo(BookInfo bookInfo) {
        OrderItemDTO item = findOrderItem(bookInfo);
        return item != null;
    }

    /**
     *
     * @param book
     * @return true if the shopping cart contains the book else return false
     */
    public boolean shoppingCartContainsBook(BookDTO book) {
        OrderItemDTO item = findOrderItem(new BookInfo(book));
        return item != null;
    }

    /**
     * Remove a bookInfo from the shopping cart
     *
     * @param bookInfo
     * @return
     */
    public String removeBookInfoFromShoppingCart(BookInfo bookInfo) {
        OrderItemDTO item = findOrderItem(bookInfo);
        if (item != null) {
            return removeFromShoppingCart(item);
        }
        return null;
    }

    /**
     * Remove a book from the shopping cart
     *
     * @param book
     * @return @see removeBookInfoFromShoppingCart
     */
    public String removeBookFromShoppingCart(BookDTO book) {
        return removeBookInfoFromShoppingCart(new BookInfo(book));
    }

    /**
     *
     * @param bookInfo
     * @return the shoppingCartItem of the bookInfo or null if the bookInfo
     * isn't in the shoppingCart
     */
    private OrderItemDTO findOrderItem(BookInfo bookInfo) {
        for (OrderItemDTO item : shoppingCart.getItems()) {
            if (item.getBook().equals(bookInfo)) {
                return item;
            }
        }
        return null;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }
}
