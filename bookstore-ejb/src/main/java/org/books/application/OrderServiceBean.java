package org.books.application;

import org.books.application.exception.*;
import org.books.application.interceptor.ValidationInterceptor;
import org.books.data.dao.BookDAOLocal;
import org.books.data.dao.CustomerDAOLocal;
import org.books.data.dao.OrderDAOLocal;
import org.books.data.dao.SequenceGeneratorDAO;
import org.books.data.dto.BookDTO;
import org.books.data.dto.OrderDTO;
import org.books.data.dto.OrderInfo;
import org.books.data.dto.OrderItemDTO;
import org.books.data.entity.*;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jms.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "OrderService")
@Interceptors(ValidationInterceptor.class)
public class OrderServiceBean implements OrderService {

    private static final Logger LOGGER = Logger.getLogger(OrderServiceBean.class.getName());
    private static final String ORDER_SEQUENCE = "ORDER_SEQUENCE";
    private static final String ORDER_PREFIX = "O-";

    @EJB
    private CustomerDAOLocal customerDAO;
    @EJB
    private BookDAOLocal bookDAO;
    @EJB
    private OrderDAOLocal orderDAO;
    @EJB
    private SequenceGeneratorDAO sequenceGenerator;
    @EJB
    private CreditCardValidatorLocal creditCardValidator;
    @EJB
    private AmazonCatalog amazonCatalog;

    @EJB
    private CatalogService catalogService;

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
        Order order = findAnOrder(orderNr);
        return new OrderDTO(order);
    }

    @Override
    public List<OrderInfo> searchOrders(String customerNr, Integer year) throws CustomerNotFoundException {
        Customer customer = findCustomer(customerNr);
        return orderDAO.search(customer, year);
    }

    @Override
    public void cancelOrder(String orderNr) throws OrderNotFoundException, OrderAlreadyShippedException {
        Order order = findAnOrder(orderNr);

        if (order.getStatus() == Order.Status.shipped) {
            LOGGER.log(Level.WARNING, "Order already shipped");
            throw new OrderAlreadyShippedException();
        }
        order.setStatus(Order.Status.canceled);
        orderDAO.update(order);
    }

    /**
     * Find a customer by customerNr
     *
     * @param customerNr
     * @return
     * @throws CustomerNotFoundException
     */
    private Customer findCustomer(String customerNr) throws CustomerNotFoundException {
        LOGGER.log(Level.INFO, "Find customer number : {0}", customerNr);
        Customer customer = customerDAO.findByCustomerNumber(customerNr);
        if (customer == null) {
            LOGGER.log(Level.WARNING, "customer number {0} not found", customerNr);
            throw new CustomerNotFoundException();
        }
        return customer;
    }

    /**
     * Find a order by orderNr if an order isn't found, an exception is sended
     *
     * @param orderNr
     * @return
     * @throws OrderNotFoundException
     */
    private Order findAnOrder(String orderNr) throws OrderNotFoundException {
        LOGGER.log(Level.INFO, "Find order number : {0}", orderNr);
        Order order = orderDAO.find(orderNr);
        if (order == null) {
            LOGGER.log(Level.WARNING, "order number {0} not found", orderNr);
            throw new OrderNotFoundException();
        }
        return order;
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
            String isbn = item.getBook().getIsbn();
            Book book = bookDAO.find(isbn);
            if (book == null) {
                LOGGER.log(Level.WARNING, "Can't find book with  {0}. Looking up amazon", isbn);
                BookDTO bookDTO = amazonCatalog.findBook(isbn);
                try {
                    catalogService.addBook(bookDTO);
                    book = bookDAO.find(isbn);
                    LOGGER.log(Level.WARNING, "Added book {0} to database", isbn);
                } catch (BookAlreadyExistsException e) {
                    throw new RuntimeException();
                }
            }
            if (book == null) {
                LOGGER.log(Level.WARNING, "This book isn''t found : {0}", isbn);
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
            LOGGER.log(Level.WARNING, "The total is too big : {0}", total);
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
        } catch (CreditCardValidationException e) {
            PaymentFailedException.Code code = PaymentFailedException.Code.INVALID_CREDIT_CARD;
            if (e.getCode() == CreditCardValidationException.Code.CREDIT_CARD_EXPIRED) {
                code = PaymentFailedException.Code.CREDIT_CARD_EXPIRED;
            }
            throw new PaymentFailedException(code);
        }

    }

    /**
     * Create and init new order
     *
     * @param customer
     * @param orderItems
     * @param amount
     * @return
     */
    private Order createOrder(Customer customer, List<OrderItem> orderItems, BigDecimal amount) {
        Long orderSequenceNumber = sequenceGenerator.getNextValue(ORDER_SEQUENCE);
        String orderNumber = ORDER_PREFIX + orderSequenceNumber;
        Order order = new Order(orderNumber, new Date(), amount, Order.Status.accepted, customer, customer.getAddress(), customer.getCreditCard(), orderItems);
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
            LOGGER.log(Level.SEVERE, "Fail to send message", ex);
        }
    }
}
