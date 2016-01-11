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
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.OrderAlreadyShippedException;
import org.books.application.exception.OrderNotFoundException;
import org.books.application.exception.PaymentFailedException;
import org.books.data.dao.BookDAOLocal;
import org.books.data.dao.CustomerDAOLocal;
import org.books.data.dao.OrderDAOLocal;
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
    private static final String VISA_REGEX = "^4\\d{15}$";
    private static final String MASTERCARD_REGEX = "^5[1-5]\\d{14}$";

    @EJB
    private CustomerDAOLocal customerDAO;
    @EJB
    private BookDAOLocal bookDAO;
    @EJB
    private OrderDAOLocal orderDAO;

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
        checkFormat(customer.getCreditCard().getNumber());
        checkLuhndigit(customer.getCreditCard().getNumber());
        checkType(customer.getCreditCard().getType(), customer.getCreditCard().getNumber());
        checkExpiredDate(customer.getCreditCard().getExpirationMonth(), customer.getCreditCard().getExpirationYear());

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

    /**
     * Check the length and the content of a card number
     *
     * @param cardNumber
     * @throws PaymentFailedException
     */
    private void checkFormat(String cardNumber) throws PaymentFailedException {
        //Check the length
        if (cardNumber.length() != 16) {
            throw new PaymentFailedException(PaymentFailedException.Code.INVALID_CREDIT_CARD);
        }
        //Check the digit
        if (!cardNumber.matches("\\d{16}$")) {
            throw new PaymentFailedException(PaymentFailedException.Code.INVALID_CREDIT_CARD);
        }
    }

    /**
     * Check the card number (throw a PaymentFailedException if it's validate)
     *
     * @param cardNumber
     * @throws PaymentFailedException
     */
    private void checkLuhndigit(String cardNumber) throws PaymentFailedException {
        //Source : http://rosettacode.org/wiki/Luhn_test_of_credit_card_numbers
        int s1 = 0, s2 = 0;
        String reverse = new StringBuffer(cardNumber).reverse().toString();
        for (int i = 0; i < reverse.length(); i++) {
            int digit = Character.digit(reverse.charAt(i), 10);
            if (i % 2 == 0) {//this is for odd digits, they are 1-indexed in the algorithm
                s1 += digit;
            } else {//add 2 * digit for 0-4, add 2 * digit - 9 for 5-9
                s2 += 2 * digit;
                if (digit >= 5) {
                    s2 -= 9;
                }
            }
        }
        //return (s1 + s2) % 10 == 0;
        if ((s1 + s2) % 10 != 0) {
            throw new PaymentFailedException(PaymentFailedException.Code.INVALID_CREDIT_CARD);
        }
    }

    /**
     * Check the type of credit card with the card number
     *
     * @param type
     * @param cardNumber
     * @throws PaymentFailedException
     */
    private void checkType(CreditCard.Type type, String cardNumber) throws PaymentFailedException {
        String regEx;
        switch (type) {
            case MasterCard:
                regEx = MASTERCARD_REGEX;
                break;
            case Visa:
                regEx = VISA_REGEX;
                break;
            default:
                regEx = "";
        }

        if (regEx.isEmpty() || !cardNumber.matches(regEx)) {
            throw new PaymentFailedException(PaymentFailedException.Code.INVALID_CREDIT_CARD);
        }
    }

    /**
     * Throw an exception if the date is expired
     *
     * @param expirationMonth
     * @param expirationYear
     * @throws PaymentFailedException
     */
    private void checkExpiredDate(Integer expirationMonth, Integer expirationYear) throws PaymentFailedException {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, expirationMonth);
        calendar.set(Calendar.YEAR, expirationYear);
        Date exiprationDate = calendar.getTime();
        //Date is expried?
        if (exiprationDate.before(new Date())) {
            throw new PaymentFailedException(PaymentFailedException.Code.CREDIT_CARD_EXPIRED);
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
