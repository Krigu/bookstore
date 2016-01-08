package org.books.integration;

import org.books.application.CustomerService;
import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.InvalidPasswordException;
import org.books.data.dto.AddressDTO;
import org.books.data.dto.CreditCardDTO;
import org.books.data.dto.CustomerDTO;
import org.books.data.dto.CustomerInfo;
import org.books.data.entity.CreditCard;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.List;


public class CustomerServiceIT {

    private static final String CUSTOMER_SERVICE_NAME = "java:global/bookstore-app/bookstore-ejb/CustomerService";
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "password";
    private static final String FIRST_NAME = "firstname";
    private static final String LAST_NAME = "lastname";
    private static final String CITY = "Bern";
    private static final String CREDIT_CARD_NUMBER = "1234";
    private static final String CUSTOMER_NR = "C-1";
    private static CustomerService customerService;


    @BeforeClass
    public void lookupService() throws Exception {
        Context jndiContext = new InitialContext();
        customerService = (CustomerService) jndiContext.lookup(CUSTOMER_SERVICE_NAME);
    }

    @Test
    public void testRegisterCustomer() throws Exception {
        CustomerDTO customerDTO = createCustomer();

        customerDTO = customerService.registerCustomer(customerDTO, PASSWORD);

        Assert.assertEquals(customerDTO.getCustomerNumber(),  "C-1");
    }

    @Test(expectedExceptions = EJBException.class)
    public void testRegisterNullCustomer() throws CustomerAlreadyExistsException {
        customerService.registerCustomer(null, "password");
    }

    @Test(expectedExceptions = EJBException.class)
    public void testRegisterCustomerNullPassword() throws CustomerAlreadyExistsException {
        CustomerDTO customerDTO = createCustomer();
        customerService.registerCustomer(customerDTO, null);
    }



    @Test(dependsOnMethods = "testRegisterCustomer", expectedExceptions = CustomerAlreadyExistsException.class)
    public void testRegisterCustomerTwice() throws Exception {
        CustomerDTO customerDTO = createCustomer();

        customerService.registerCustomer(customerDTO, PASSWORD);
    }

    private CustomerDTO createCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(FIRST_NAME);
        customerDTO.setLastName(LAST_NAME);
        customerDTO.setEmail(EMAIL);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet("street");
        addressDTO.setPostalCode("3000");
        addressDTO.setCity(CITY);
        addressDTO.setCountry("Switzerland");
        customerDTO.setAdress(addressDTO);

        CreditCardDTO creditCardDTO = new CreditCardDTO();
        creditCardDTO.setType(CreditCard.Type.MasterCard);
        creditCardDTO.setNumber(CREDIT_CARD_NUMBER);
        creditCardDTO.setExpirationMonth(12);
        creditCardDTO.setExpirationYear(2020);
        creditCardDTO.setExpirationYear(2020);
        customerDTO.setCreditCard(creditCardDTO);

        return customerDTO;
    }

    @Test(dependsOnMethods = "testRegisterCustomer")
    public void testAuthenticateCustomer() throws CustomerNotFoundException, InvalidPasswordException {
        customerService.authenticateCustomer(EMAIL, PASSWORD);
    }

    @Test(dependsOnMethods = "testRegisterCustomer", expectedExceptions = InvalidPasswordException.class)
    public void testAuthenticateCustomerInvalidPassword() throws CustomerNotFoundException, InvalidPasswordException {
        customerService.authenticateCustomer(EMAIL, PASSWORD + "invalid");
    }

    @Test(dependsOnMethods = "testRegisterCustomer", expectedExceptions = InvalidPasswordException.class)
    public void testAuthenticateCustomerNullPassword() throws CustomerNotFoundException, InvalidPasswordException {
        customerService.authenticateCustomer(EMAIL, null);
    }

    @Test(dependsOnMethods = "testRegisterCustomer", expectedExceptions = CustomerNotFoundException.class)
    public void testAuthenticateInvalidCustomer() throws CustomerNotFoundException, InvalidPasswordException {
        customerService.authenticateCustomer("invalid@invalid.com", PASSWORD);
    }

    @Test(dependsOnMethods = "testRegisterCustomer", expectedExceptions = CustomerNotFoundException.class)
    public void testAuthenticateNullCustomer() throws CustomerNotFoundException, InvalidPasswordException {
        customerService.authenticateCustomer(null, PASSWORD);
    }

    @Test(dependsOnMethods = "testRegisterCustomer")
    public void testChangePassword() throws Exception {
        String changedPassword = PASSWORD + "changed";
        customerService.changePassword(EMAIL, changedPassword);
        customerService.authenticateCustomer(EMAIL, changedPassword);
    }

    @Test(dependsOnMethods = "testRegisterCustomer", expectedExceptions = InvalidPasswordException.class)
    public void testChangePasswordAndTryWithOldone() throws Exception {
        String changedPassword = PASSWORD + "changed2";
        customerService.changePassword(EMAIL, changedPassword);
        customerService.authenticateCustomer(EMAIL, PASSWORD);
    }

    @Test(dependsOnMethods = "testRegisterCustomer", expectedExceptions = CustomerNotFoundException.class)
    public void testChangePasswordInvalidCustomre() throws Exception {
        customerService.changePassword("test@invalid.com", PASSWORD);
    }

    @Test(dependsOnMethods = "testRegisterCustomer")
    public void testFindCustomer() throws Exception {

    }

    @Test(dependsOnMethods = "testRegisterCustomer")
    public void testFindCustomerByEmail() throws Exception {
        CustomerDTO customer = customerService.findCustomerByEmail(EMAIL);
        Assert.assertEquals(EMAIL, customer.getEmail());
        Assert.assertEquals(FIRST_NAME, customer.getFirstName());
        Assert.assertEquals(LAST_NAME, customer.getLastName());
        Assert.assertEquals(CITY, customer.getAddress().getCity());
        Assert.assertEquals(CREDIT_CARD_NUMBER, customer.getCreditCard().getNumber());
    }

    @Test(dependsOnMethods = "testRegisterCustomer", expectedExceptions = CustomerNotFoundException.class)
    public void testFindCustomerByEmailInvalid() throws Exception {
        customerService.findCustomerByEmail("invalid@invalid.com");
    }

    @Test(dependsOnMethods = "testRegisterCustomer", expectedExceptions = CustomerNotFoundException.class)
    public void testFindCustomerByNullCustomerNumber() throws Exception {
        customerService.findCustomer(null);
    }

    @Test(dependsOnMethods = "testRegisterCustomer")
    public void testFindCustomerByCustomerNumber() throws Exception {
        CustomerDTO customer = customerService.findCustomer(CUSTOMER_NR);
        Assert.assertEquals(EMAIL, customer.getEmail());
        Assert.assertEquals(FIRST_NAME, customer.getFirstName());
        Assert.assertEquals(LAST_NAME, customer.getLastName());
        Assert.assertEquals(CITY, customer.getAddress().getCity());
        Assert.assertEquals(CREDIT_CARD_NUMBER, customer.getCreditCard().getNumber());
    }

    @Test(dependsOnMethods = "testRegisterCustomer", expectedExceptions = CustomerNotFoundException.class)
    public void testFindCustomerByCustomerNumberInvalid() throws Exception {
        customerService.findCustomer("C-123213");
    }

    @Test(dependsOnMethods = "testRegisterCustomer", expectedExceptions = CustomerNotFoundException.class)
    public void testFindCustomerByNullEmail() throws Exception {
        customerService.findCustomerByEmail(null);
    }


    @Test(dependsOnMethods = "testRegisterCustomer")
    public void testSearchCustomersByFirstname() throws Exception {
        List<CustomerInfo> list = customerService.searchCustomers(FIRST_NAME);
        Assert.assertFalse(list.isEmpty());
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(EMAIL, list.get(0).getEmail());
    }

    @Test(dependsOnMethods = "testRegisterCustomer")
    public void testSearchCustomersByLastname() throws Exception {
        List<CustomerInfo> list = customerService.searchCustomers(LAST_NAME);
        Assert.assertFalse(list.isEmpty());
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(EMAIL, list.get(0).getEmail());
    }

    @Test(dependsOnMethods = "testRegisterCustomer")
    public void testSearchCustomersEmtpyList() throws Exception {
        // Should return an empty list
        List<CustomerInfo> list = customerService.searchCustomers("xyz");
        Assert.assertTrue(list.isEmpty());
    }

    @Test(dependsOnMethods = "testRegisterCustomer")
    public void testUpdateCustomer() throws Exception {
        CustomerDTO customer = customerService.findCustomer(CUSTOMER_NR);

        String email = "new_adress@gmail.com";
        customer.setEmail(email);
        customerService.updateCustomer(customer);

        CustomerDTO updatedCustomer = customerService.findCustomerByEmail(email);

        Assert.assertEquals(email, updatedCustomer.getEmail());
    }

    @Test(dependsOnMethods = "testRegisterCustomer", expectedExceptions = CustomerAlreadyExistsException.class)
    public void testUpdateCustomerDuplicateEMail() throws Exception {
        CustomerDTO customer2 = createCustomer();
        String email = "email2@email.com";
        customer2.setEmail(email);
        customer2 = customerService.registerCustomer(customer2, PASSWORD);

        Assert.assertEquals("C-2", customer2.getCustomerNumber());

        // Find customer 1
        CustomerDTO customer = customerService.findCustomer(CUSTOMER_NR);
        customer.setEmail(email);
        customerService.updateCustomer(customer);

    }

    @Test(dependsOnMethods = "testRegisterCustomer", expectedExceptions = CustomerNotFoundException.class)
    public void testUpdateCustomerInvalidNumber() throws Exception {
        CustomerDTO customer3 = createCustomer();
        String email = "email555@email.com";
        customer3.setEmail(email);
        customerService.updateCustomer(customer3);

        // Find customer 1
        CustomerDTO customer = customerService.findCustomer(CUSTOMER_NR);
        customer.setEmail(email);
        customerService.updateCustomer(customer);

    }
}