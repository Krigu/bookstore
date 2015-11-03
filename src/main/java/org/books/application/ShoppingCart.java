package org.books.application;

import org.books.data.entity.Book;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author Tilmann Bück
 */
@SessionScoped
public class ShoppingCart implements Serializable {
   private List<ShoppingCartItem> items;

   public ShoppingCart() {
       items = new ArrayList<>();
   }
   
    public List<ShoppingCartItem> getItems() {
        return items;
    }

   public void add(Book book)   {
       ShoppingCartItem shoppingCartItem = getItem(book);
       
       if (shoppingCartItem == null) {
            items.add(new ShoppingCartItem(book));    
       }
       else
           shoppingCartItem.increaseQuantity();
   }
   
   public void remove(ShoppingCartItem item) {
       items.remove(item);
   }
   
   public BigDecimal getTotalAmount() {
       BigDecimal result = new BigDecimal(0);
       
       for (ShoppingCartItem item : items) {
           result = result.add(item.getAmount());
       }
       
       return result;
   }
   
   private ShoppingCartItem getItem(Book book) {
       for (ShoppingCartItem item : items) {
           if (item.getBook().equals(book)) {
               return item;
           }
       }
       
       return null;
   }
           
}
