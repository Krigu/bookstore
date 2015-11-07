package org.books.presentation;

import org.books.application.ShoppingCart;
import org.books.data.dto.BookInfo;
import org.books.data.dto.OrderItemDTO;
import org.books.data.entity.Book;
import org.books.util.MessageFactory;
import org.primefaces.event.RowEditEvent;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

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

    public String removeFromShoppingCart(OrderItemDTO item) {
        shoppingCart.remove(item);
        MessageFactory.info("bookRemoved", item.getBook().getTitle());
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
        OrderItemDTO item = (OrderItemDTO)event.getObject();
        MessageFactory.info("shoppingCartUpdateBookQuantity", item.getBook().getTitle(),item.getQuantity());
    }
    /**
     *
     * @param bookInfo
     * @return true if the shopping cart contains the bookInfo else return false
     */
    public boolean shoppingCartContainsBookInfo(BookInfo bookInfo) {
        OrderItemDTO item = findOrderItem(bookInfo);
        return item!=null;
    }
    
        /**
     *
     * @param book
     * @return true if the shopping cart contains the book else return false
     */
    public boolean shoppingCartContainsBook(Book book) {
        OrderItemDTO item = findOrderItem(new BookInfo(book));
        return item!=null;
    }

    /**
     * Remove a bookInfo from the shopping cart
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
     * @param book
     * @return
     */
    public String removeBookFromShoppingCart(Book book) {
        return removeBookInfoFromShoppingCart(new BookInfo(book));
    }

    /**
     *
     * @param bookInfo
     * @return the shoppingCartItem of the bookInfo or null if the bookInfo isn't in the
     * shoppingCart
     */
    private OrderItemDTO findOrderItem(BookInfo bookInfo) {
        for (OrderItemDTO item : shoppingCart.getItems()) {
            if (item.getBook().equals(bookInfo)) {
                return item;
            }
        }
        return null;
    }
}
