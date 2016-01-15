package org.books.application;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.Queue;
import org.books.application.exception.BookNotFoundException;
import org.books.application.exception.CreditCardValidationException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.OrderAlreadyShippedException;
import org.books.application.exception.OrderNotFoundException;
import org.books.application.exception.PaymentFailedException;
import org.books.data.dao.BookDAOLocal;
import org.books.data.dao.CustomerDAOLocal;
import org.books.data.dao.OrderDAOLocal;
import org.books.data.dto.CreditCardType;
import org.books.data.dto.OrderDTO;
import org.books.data.dto.OrderInfo;
import org.books.data.dto.OrderItemDTO;
import org.books.data.entity.Book;
import org.books.data.entity.CreditCard;
import org.books.data.entity.Customer;
import org.books.data.entity.Order;
import org.books.data.entity.OrderItem;

@Stateless(name = "OrderService")
public class OrderServiceBean implements OrderService {

    private static final Logger LOGGER = Logger.getLogger(OrderServiceBean.class.getName());

    @EJB
    private CustomerDAOLocal customerDAO;
    @EJB
    private BookDAOLocal bookDAO;
    @EJB
    private OrderDAOLocal orderDAO;
    @EJB
    private CreditCardValidatorLocal creditCardValidator;

    @Resource(name = "maxAmount")
    private float maxAmount;

    @Inject
    @JMSConnectionFactory("jms/connectionFactory")
    private JMSContext jMSContext;
    @Resource(lookup = "jms/orderQueue")
    private Queue queue;

    @Override
    public OrderDTO placeOrder(String customerNr, List<OrderItemDTO> items) throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException {
        //Find customer
        Customer customer = findCustomer(customerNr);
        //Create the list for the orderItem
        List<OrderItem> orderItems = initListOrderItems(items);
        BigDecimal amount = initTotalAmount(orderItems);
        //Check by credit card
        validateCreditCard(customer.getCreditCard());

        //Create order
        Order order = createOrder(customer, orderItems, amount);

        orderProcesing(order);

        return new OrderDTO(order);
    }

    @Override
    public OrderDTO findOrder(String orderNr) throws OrderNotFoundException {
        Order order = null;
        try {
            order = orderDAO.find(orderNr);
        } catch (Exception e) {
            throw new OrderNotFoundException();
        }
        return new OrderDTO(order);
    }

    @Override
    public List<OrderInfo> searchOrders(String customerNr, Integer year) throws CustomerNotFoundException {
        Customer customer = findCustomer(customerNr);
        return orderDAO.search(customer, year);
    }

    @Override
    public void cancelOrder(String orderNr) throws OrderNotFoundException, OrderAlreadyShippedException {
        Order order = null;
        try {
            order = orderDAO.find(orderNr);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING,"Order not found");
            throw new OrderNotFoundException();
        }
        if (order.getStatus() == Order.Status.shipped) {
            LOGGER.log(Level.WARNING,"Order already shipped");
            throw new OrderAlreadyShippedException();
        }
        order.setStatus(Order.Status.canceled);
        orderDAO.update(order);
    }

    /*@Override
    public void sendBook(Book book) {
        LOGGER.info("*************************send book to queue");
        //Send message after an accepted order
        JMSProducer producer = jMSContext.createProducer();
        ObjectMessage msg = jMSContext.createObjectMessage(book);
        producer.send(queue, msg);
        
        LOGGER.info("*******************book sended "+book.getAuthors());
    }*/

    /**
     * Find a customer by customerNr
     *
     * @param customerNr
     * @return
     * @throws CustomerNotFoundException
     */
    private Customer findCustomer(String customerNr) throws CustomerNotFoundException {
        LOGGER.info("Find customer number : " + customerNr);
        Customer customer;
        try {
            //TODO Remplace this methode by find by customerNr
            customer = customerDAO.find(customerNr);
        } catch (Exception e) {
            throw new CustomerNotFoundException();
        }
        return customer;
    }

    /**
     * Convert the orderItemDTO to orderItem
     *
     * @param items
     * @return
     * @throws BookNotFoundException if a book doesn't exist
     */
    private List<OrderItem> initListOrderItems(List<OrderItemDTO> items) throws BookNotFoundException {
        LOGGER.info("Convert OrderItemDTO to OrderItem");
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDTO item : items) {
            Book book;
            try {
                book = bookDAO.find(item.getBook().getIsbn());
            } catch (Exception e) {
                LOGGER.log(Level.WARNING,"This book isn't found : " + item.getBook().getIsbn());
                throw new BookNotFoundException();
            }

            OrderItem orderItem = new OrderItem(book, item.getBook().getPrice(), item.getQuantity());
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    /**
     * This method init the total amount and check if it's not greater as the
     * maximum (throw an PaymentFailedException in this fall)
     *
     * @param items
     * @return
     */
    private BigDecimal initTotalAmount(List<OrderItem> items) throws PaymentFailedException {
        LOGGER.info("Compute the total of the OrderItem's list");
        BigDecimal total = new BigDecimal(BigInteger.ZERO);
        for (OrderItem item : items) {

            float itemAmount = item.getBook().getPrice().floatValue() * item.getQuantity();
            total = total.add(new BigDecimal(itemAmount));
        }
        if (total.floatValue() > maxAmount) {
            LOGGER.log(Level.WARNING,"The total is too big : " + total);
            throw new PaymentFailedException(PaymentFailedException.Code.PAYMENT_LIMIT_EXCEEDED);
        }
        return total;
    }

    private void validateCreditCard(CreditCard creditCard) throws PaymentFailedException {
        try {
            creditCardValidator.checkCreditCard(creditCard.getNumber(), 
                                                creditCard.getType().name(), 
                                                creditCard.getExpirationMonth(), 
                                                creditCard.getExpirationYear());
        }
        catch (CreditCardValidationException e) {
            PaymentFailedException.Code code = PaymentFailedException.Code.INVALID_CREDIT_CARD;
            if (e.getCode() == CreditCardValidationException.Code.CREDIT_CARD_EXPIRED) {
                code = PaymentFailedException.Code.CREDIT_CARD_EXPIRED;
            }
            throw new PaymentFailedException(code);
        }
        
    }

    private Order createOrder(Customer customer, List<OrderItem> orderItems, BigDecimal amount) {
        //TODO generate the order number
        Order order = new Order("orderNummer", new Date(), amount, Order.Status.accepted, customer, customer.getAddress(), customer.getCreditCard(), orderItems);
        return orderDAO.create(order);
    }

    /**
     * Send the order to change the state from ACCEPTED to PROCESSING
     *
     * @param order
     */
    private void orderProcesing(Order order) {
        try {
            //Send message after an accepted order
            JMSProducer producer = jMSContext.createProducer();
            MapMessage msg = jMSContext.createMapMessage();
            msg.setLong("orderId", order.getId());
            producer.send(queue, msg);
        } catch (JMSException ex) {
            //Nothing
        }
    }

}
