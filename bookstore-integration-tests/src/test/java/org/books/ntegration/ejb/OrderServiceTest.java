package org.books.ntegration.ejb;

import org.books.BookstoreArquillianTest;
import org.books.application.CatalogService;
import org.books.application.CustomerService;
import org.books.application.OrderService;
import org.books.application.exception.*;
import org.books.data.dto.*;
import org.books.data.entity.Book;
import org.books.data.entity.Order;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ejb.EJB;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Test(groups = {"OrderServiceIT"}, dependsOnGroups = {"CustomerServiceTest", "CatalogServiceTest"})
public class OrderServiceTest extends BookstoreArquillianTest {

    private static final AddressDTO addressDTO = new AddressDTO("street 1", "city 1", "1111", "CH");
    private static final CreditCardDTO ccDTO = new CreditCardDTO(CreditCardType.MasterCard, "5111005111051128", 12, 2020);
    private static CustomerDTO customerDTO = new CustomerDTO("test@test.com", "firstname", "lastname", null, addressDTO, ccDTO);

    private BookDTO bookDTO1 = new BookDTO("Antonio Goncalves", Book.Binding.Paperback, "1234567890", 608, new BigDecimal("50.00"), 2013, "Apress", "Beginning Java EE 7");
    private BookDTO bookDTO2 = new BookDTO("U2", Book.Binding.Hardcover, "123456789012", 432, new BigDecimal("10.00"), 2010, "Rock", "Java EE 7 for the newbies");

    private final List<OrderItemDTO> items = new ArrayList<>();

    @EJB
    private OrderService orderService;

    @EJB
    private CatalogService catalogService;

    @EJB
    private CustomerService customerService;

    @Test
    public void insertData() throws Exception {

        customerDTO = customerService.registerCustomer(customerDTO, "1234");

        Assert.assertNotNull(customerDTO.getNumber());

        catalogService.addBook(bookDTO1);
        bookDTO1 = catalogService.findBook(bookDTO1.getIsbn());
        catalogService.addBook(bookDTO2);
        bookDTO2 = catalogService.findBook(bookDTO2.getIsbn());

        init();
    }

    @Test(expectedExceptions = ValidationException.class)
    public void findAnNullOrder() throws OrderNotFoundException {
        orderService.findOrder(null);
    }

    /**
     * PlaceOrder IT
     */
    /**
     * This IT test the processus of creating an order and his work flox
     * accepted --> processing --> shipped
     *
     * @throws CustomerNotFoundException
     * @throws BookNotFoundException
     * @throws PaymentFailedException
     */
    @Test(dependsOnMethods = "insertData")
    public void orderProcess() throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException {
        //Create order
        Assert.assertNotNull(customerDTO.getNumber());
        OrderDTO order = orderService.placeOrder(customerDTO.getNumber(), items);
        //check status accepted
        Assert.assertEquals(order.getStatus(), Order.Status.accepted);
        //Waiting 6 seconds
        try {
            Thread.sleep(6000);
        } catch (InterruptedException ex) {
            //Nothing
        }
        //check status progressing
        try {
            order = orderService.findOrder(order.getNumber());
            Assert.assertEquals(order.getStatus(), Order.Status.processing);
        } catch (OrderNotFoundException ex) {
            Assert.assertTrue(false, "Order not found");
        }

        //Waiting 6 seconds
        try {
            Thread.sleep(6000);
        } catch (InterruptedException ex) {
            //Nothing
        }

        try {
            //check status shipped
            order = orderService.findOrder(order.getNumber());
            Assert.assertEquals(order.getStatus(), Order.Status.shipped);
        } catch (OrderNotFoundException ex) {
            Assert.assertTrue(false, "Order not found");
        }
    }

    @Test(dependsOnMethods = "orderProcess", expectedExceptions = CustomerNotFoundException.class)
    public void placeOrderCustomerNotFound() throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException {
        orderService.placeOrder("no_number", items);
    }

    @Test(dependsOnMethods = "placeOrderCustomerNotFound", expectedExceptions = BookNotFoundException.class)
    public void placeOrderBookNotFound() throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException {
        items.add(new OrderItemDTO(new BookInfo("no_isbn", "no_title", new BigDecimal(15)), 2));
        orderService.placeOrder(customerDTO.getNumber(), items);
    }

    @Test(dependsOnMethods = "placeOrderBookNotFound", expectedExceptions = PaymentFailedException.class)
    public void placeOrderTotalTooBig() throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException {
        items.add(new OrderItemDTO(new BookInfo(bookDTO1.getIsbn(), bookDTO1.getTitle(), bookDTO1.getPrice()), 2000));
        orderService.placeOrder(customerDTO.getNumber(), items);
    }

    @Test(dependsOnMethods = "placeOrderTotalTooBig", expectedExceptions = PaymentFailedException.class)
    public void placeOrderCreditCardWrongSize() throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException, CustomerAlreadyExistsException {
        customerDTO.getCreditCard().setNumber("1234");
        customerService.updateCustomer(customerDTO);
        orderService.placeOrder(customerDTO.getNumber(), items);
    }

    @Test(dependsOnMethods = "placeOrderCreditCardWrongSize", expectedExceptions = PaymentFailedException.class)
    public void placeOrderCreditCardWrongFormat() throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException, CustomerAlreadyExistsException {
        customerDTO.getCreditCard().setNumber("1234123412341234");
        customerService.updateCustomer(customerDTO);
        orderService.placeOrder(customerDTO.getNumber(), items);
    }

    @Test(dependsOnMethods = "placeOrderCreditCardWrongFormat", expectedExceptions = PaymentFailedException.class)
    public void placeOrderCreditCardWrongType() throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException, CustomerAlreadyExistsException {
        customerDTO.getCreditCard().setType(CreditCardType.Visa);
        customerService.updateCustomer(customerDTO);
        orderService.placeOrder(customerDTO.getNumber(), items);
    }

    @Test(dependsOnMethods = "placeOrderCreditCardWrongType", expectedExceptions = PaymentFailedException.class)
    public void placeOrderCreditCardExpired() throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException, CustomerAlreadyExistsException {
        customerDTO.getCreditCard().setExpirationYear(2000);
        customerService.updateCustomer(customerDTO);
        orderService.placeOrder(customerDTO.getNumber(), items);
    }

    /**
     * Find order
     */
    @Test(expectedExceptions = OrderNotFoundException.class)
    public void findAnNotExistingOrder() throws OrderNotFoundException {
        orderService.findOrder("no_order_number");
    }

    @Test(dependsOnMethods = "orderProcess")
    public void findAnExistingOrder() throws OrderNotFoundException {
        Assert.assertNotNull(orderService.findOrder("O-1"));
    }

    /**
     * Cancel order
     */
    @Test(dependsOnMethods = "orderProcess")
    public void cancelAcceptedOrder() throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException, OrderNotFoundException, OrderAlreadyShippedException {
        //Create order
        OrderDTO order = orderService.placeOrder(customerDTO.getNumber(), items);
        orderService.cancelOrder(order.getNumber());
        //Waiting 12 seconds
        try {
            Thread.sleep(12000);
        } catch (InterruptedException ex) {
            //Nothing
        }
        order = orderService.findOrder(order.getNumber());
        Assert.assertEquals(order.getStatus(), Order.Status.canceled);
    }

    @Test(dependsOnMethods = "cancelAcceptedOrder")
    public void cancelProcessingOrder() throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException, OrderNotFoundException, OrderAlreadyShippedException {
        //Create order
        OrderDTO order = orderService.placeOrder(customerDTO.getNumber(), items);
        //Waiting 6 seconds
        try {
            Thread.sleep(6000);
        } catch (InterruptedException ex) {
            //Nothing
        }
        orderService.cancelOrder(order.getNumber());
        //Waiting 6 seconds
        try {
            Thread.sleep(6000);
        } catch (InterruptedException ex) {
            //Nothing
        }
        order = orderService.findOrder(order.getNumber());
        Assert.assertEquals(order.getStatus(), Order.Status.canceled);
    }

    @Test(dependsOnMethods = "cancelProcessingOrder", expectedExceptions = OrderAlreadyShippedException.class)
    public void cancelShippedOrder() throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException, OrderNotFoundException, OrderAlreadyShippedException {
        //Create order
        OrderDTO order = orderService.placeOrder(customerDTO.getNumber(), items);
        //Waiting 12 seconds
        try {
            Thread.sleep(12000);
        } catch (InterruptedException ex) {
            //Nothing
        }
        orderService.cancelOrder(order.getNumber());
    }

    /**
     * Search order
     */
    @Test(dependsOnMethods = "cancelShippedOrder")
    public void searchOrders() throws CustomerNotFoundException {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        List<OrderInfo> orders = orderService.searchOrders(customerDTO.getNumber(), year);
        Assert.assertEquals(orders.size(), 4);
    }

    @Test(dependsOnMethods = "searchOrders", expectedExceptions = CustomerNotFoundException.class)
    public void searchOrdersNoCustomerFound() throws CustomerNotFoundException {
        orderService.searchOrders("no_number", 2016);
    }

    @Test(dependsOnMethods = "searchOrders")
    public void searchOrderZeroResult() throws CustomerNotFoundException {
        List<OrderInfo> orders = orderService.searchOrders(customerDTO.getNumber(), 2000);
        Assert.assertEquals(orders.size(), 0);
    }

    private void init() {
        items.clear();
        items.add(new OrderItemDTO(new BookInfo(bookDTO1.getIsbn(), bookDTO1.getTitle(), bookDTO1.getPrice()), 2));
        items.add(new OrderItemDTO(new BookInfo(bookDTO2.getIsbn(), bookDTO2.getTitle(), bookDTO2.getPrice()), 1));
    }
}
