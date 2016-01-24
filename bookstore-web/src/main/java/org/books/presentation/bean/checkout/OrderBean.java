package org.books.presentation.bean.checkout;

import org.books.application.ShoppingCart;
import org.books.data.dto.OrderDTO;
import org.books.data.dto.OrderItemDTO;
import org.books.util.MessageFactory;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import org.books.application.OrderService;
import org.books.application.exception.BookNotFoundException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.PaymentFailedException;
import org.books.presentation.bean.account.CustomerBean;
import org.books.presentation.bean.catalog.CatalogBean;
import org.books.presentation.validator.CreditCardValidator;

@SessionScoped
@Named("orderBean")
public class OrderBean implements Serializable {

    public static final String CREDIT_CARD_LIMIT_EXCEEDED ="org.books.presentation.bean.checkout.OrderBean.CREDIT_CARD_LIMIT_EXCEEDED";
    
    private static final Logger LOGGER = Logger.getLogger(OrderBean.class.getName());
    
    @EJB
    private OrderService orderService;

    @Inject
    private ShoppingCart shoppingCart;

    private OrderDTO order;

    /**
     *
     * @param customerNr
     * @param orderItems
     * @return
     */
    public String doSubmitOrder(String customerNr, List<OrderItemDTO> orderItems) {
        try {
            this.order = orderService.placeOrder(customerNr, orderItems);
        } catch (CustomerNotFoundException e) {
            LOGGER.log(Level.WARNING, "Customer not found", e);
            MessageFactory.error(CustomerBean.CUSTOMER_NOT_FOUND);
            return null;
        } catch (BookNotFoundException ex) {
            LOGGER.log(Level.WARNING, "Book not found", ex);
            MessageFactory.error(CatalogBean.BOOK_NOT_FOUND);
            return null;
        } catch (PaymentFailedException ex) {
            LOGGER.log(Level.WARNING, "Payment failed (code:"+ex.getCode().name()+")", ex);
            switch (ex.getCode()) {
                case CREDIT_CARD_EXPIRED:
                    MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, CreditCardValidator.CREDIT_CARD_EXPIRED); 
                    break;
                case INVALID_CREDIT_CARD:
                    MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, CreditCardValidator.CREDIT_CARD_WRONG_FORMAT);   
                    break;
                case PAYMENT_LIMIT_EXCEEDED:
                    MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, CREDIT_CARD_LIMIT_EXCEEDED);   
                    break;
                default:
                    //Nothing       
            }
        }

        clearCart();

        return "/user/orderConfirmation?faces-redirect=true&menuId=2";
    }

    private void clearCart() {
        shoppingCart.getItems().clear();
    }

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }
}
