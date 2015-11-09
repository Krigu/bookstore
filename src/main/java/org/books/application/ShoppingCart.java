package org.books.application;

import org.books.data.dto.BookInfo;
import org.books.data.dto.OrderItemDTO;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tilmann Bück
 */
@SessionScoped
public class ShoppingCart implements Serializable {

   private List<OrderItemDTO> items;

   public ShoppingCart() {
       items = new ArrayList<>();
   }

    public List<OrderItemDTO> getItems() {
        return items;
    }

   public void add(BookInfo bookInfo)   {
       OrderItemDTO shoppingCartItem = getItem(bookInfo);

       if (shoppingCartItem == null) {
            items.add(new OrderItemDTO(bookInfo, 1));
       }
       else {
           shoppingCartItem.setQuantity(shoppingCartItem.getQuantity() + 1);
       }
   }

   public void remove(OrderItemDTO item) {
       items.remove(item);
   }

   public BigDecimal getTotalAmount() {
       BigDecimal result = new BigDecimal(0);

       for (OrderItemDTO item : items) {
           result = result.add( item.getBook().getPrice().multiply(new BigDecimal(item.getQuantity())) );
       }

       return result;
   }

   private OrderItemDTO getItem(BookInfo bookInfo) {
       for (OrderItemDTO item : items) {
           if (item.getBook().equals(bookInfo)) {
               return item;
           }
       }

       return null;
   }

}
