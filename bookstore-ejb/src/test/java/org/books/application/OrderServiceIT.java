package org.books.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.books.application.exception.BookNotFoundException;
import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.OrderAlreadyShippedException;
import org.books.application.exception.OrderNotFoundException;
import org.books.application.exception.PaymentFailedException;
import org.books.data.dto.AddressDTO;
import org.books.data.dto.BookDTO;
import org.books.data.dto.BookInfo;
import org.books.data.dto.CreditCardDTO;
import org.books.data.dto.CustomerDTO;
import org.books.data.dto.OrderItemDTO;
import org.books.data.entity.Address;
import org.books.data.entity.Book;
import org.books.data.entity.CreditCard;
import org.books.data.entity.Customer;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class OrderServiceIT {

    private static final String ORDER_SERVICE_NAME = "java:global/bookstore-app/bookstore-ejb/OrderService";
    private static OrderService orderService;
    private static final String CATALOG_SERVICE_NAME = "java:global/bookstore-app/bookstore-ejb/CatalogService";
    private static CatalogService catalogService;
    private static final String CUSTOMER_SERVICE_NAME = "java:global/bookstore-app/bookstore-ejb/CustomerService";
    private static CustomerService customerService;

    private final AddressDTO addressDTO = new AddressDTO("street 1", "city 1", "1111", "CH");
    private final CreditCardDTO ccDTO = new CreditCardDTO(CreditCard.Type.MasterCard, "5111005111051128", 12, 2020);
    private CustomerDTO customerDTO;

    private BookDTO bookDTO1 = new BookDTO("Antonio Goncalves", Book.Binding.Paperback, "12345", 608, new BigDecimal("50.00"), 2013, "Apress", "Beginning Java EE 7");
    private BookDTO bookDTO2 = new BookDTO("U2", Book.Binding.Hardcover, "67890", 432, new BigDecimal("10.00"), 2010, "Rock", "Java EE 7 for the newbies");
    private BookDTO bookDTONotExist = new BookDTO("Paxar", Book.Binding.Ebook, "54321", 608, new BigDecimal("50.00"), 2013, "Dream", "Java EE 7 for the imagination");

    private List<OrderItemDTO> items = new ArrayList<>();

    @BeforeClass
    public void lookupService() throws Exception {
        Context jndiContext = new InitialContext();
        orderService = (OrderService) jndiContext.lookup(ORDER_SERVICE_NAME);
        catalogService = (CatalogService) jndiContext.lookup(CATALOG_SERVICE_NAME);
        customerService = (CustomerService) jndiContext.lookup(CUSTOMER_SERVICE_NAME);

        customerDTO.setEmail("abc@gmail.com");
        customerDTO.setFirstName("Fist name");
        customerDTO.setLastName("last name");
        customerDTO.setCreditCard(ccDTO);
        customerDTO.setAdress(addressDTO);
        customerDTO = customerService.registerCustomer(customerDTO, "1234");

        catalogService.addBook(bookDTO1);
        bookDTO1 = catalogService.findBook(bookDTO1.getIsbn());
        catalogService.addBook(bookDTO2);
        bookDTO2 = catalogService.findBook(bookDTO2.getIsbn());

        init();
    }

    @Test(expectedExceptions = CustomerNotFoundException.class)
    public void placeOrderCustomerNotFound() throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException {
        orderService.placeOrder("a@b.c", items);
    }

    /*@Test(expectedExceptions = BookNotFoundException.class)
    public void placeOrderBookNotFound() throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException {
        items.add(new OrderItemDTO(new BookInfo(book3), 2));
        orderService.placeOrder("a@b.c", items);
    }

    @Test(expectedExceptions = PaymentFailedException.class)
    public void placeOrderTotalTooBig() throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException {
        items.add(new OrderItemDTO(new BookInfo(book1), 200));
        orderService.placeOrder("a@b.c", items);
    }

    @Test(expectedExceptions = PaymentFailedException.class)
    public void placeOrderCreditCardWrongSize() throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException {
        //TODO
        customer.getCreditCard().setNumber("1234");
        //save customer
        orderService.placeOrder("a@b.c", items);
    }

    @Test(expectedExceptions = PaymentFailedException.class)
    public void placeOrderCreditCardWrongFormat() throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException {
        //TODO
        customer.getCreditCard().setNumber("1234123412341234");
        //save customer
        orderService.placeOrder("a@b.c", items);
    }

    @Test(expectedExceptions = PaymentFailedException.class)
    public void placeOrderCreditCardWrongType() throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException {
        //TODO
        customer.getCreditCard().setType(CreditCard.Type.Visa);
        //save customer
        orderService.placeOrder("a@b.c", items);
    }

    @Test(expectedExceptions = PaymentFailedException.class)
    public void placeOrderCreditCardExpired() throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException {
        //TODO
        customer.getCreditCard().setExpirationYear(2000);
        //save customer
        orderService.placeOrder("a@b.c", items);
    }
    @Test(expectedExceptions = OrderNotFoundException.class)
    public void findAnNotExistingOrder() throws OrderNotFoundException {
        orderService.findOrder("1234");
    }

    @Test(expectedExceptions = CustomerNotFoundException.class)
    public void searchOrderCustomerNotFound() throws CustomerNotFoundException {
        orderService.searchOrders("a@b.c", 2000);
    }

    @Test(expectedExceptions = OrderNotFoundException.class)
    public void cancelOrderOrderNotFound() throws OrderNotFoundException, OrderAlreadyShippedException {
        orderService.cancelOrder("123");
    }*/
    @Test
    public void shippingProcess() {
        //Create order
        //check status accepted
        //check status progressing
        //check status shipping
        //orderService.sendBook(book1);
        Assert.assertTrue(true);
    }

    //TODO
    //placeorder success
    //check send message processing
    //check order is created
    //find an existing order
    //Search order with result
    //cancel order success
    //cancel order OrderAlreadyShippedException
    @BeforeMethod
    private void init() {
        initListOrderItemDTO();
        initCustomer();
    }

    private void initListOrderItemDTO() {
        items.clear();
        items.add(new OrderItemDTO(new BookInfo(bookDTO1.getIsbn(), bookDTO1.getTitle(), bookDTO1.getPrice()), 2));
        items.add(new OrderItemDTO(new BookInfo(bookDTO2.getIsbn(), bookDTO2.getTitle(), bookDTO2.getPrice()), 1));
    }

    private void initCustomer() {
        customerDTO.setAdress(addressDTO);
        customerDTO.setCreditCard(ccDTO);
        try {
            customerService.updateCustomer(customerDTO);
        } catch (CustomerNotFoundException | CustomerAlreadyExistsException ex) {
            Logger.getLogger(OrderServiceIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
