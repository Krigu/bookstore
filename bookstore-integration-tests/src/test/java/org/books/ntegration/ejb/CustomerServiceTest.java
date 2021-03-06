package org.books.ntegration.ejb;

import org.books.BookstoreArquillianTest;
import org.books.application.CustomerService;
import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.InvalidPasswordException;
import org.books.application.exception.ValidationException;
import org.books.data.dto.*;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ejb.EJB;
import java.util.List;

@Test(groups = {"CustomerServiceTest"})
public class CustomerServiceTest  extends BookstoreArquillianTest {

    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "password";
    private static final String FIRST_NAME = "firstname";
    private static final String LAST_NAME = "lastname";
    private static final String CITY = "Bern";
    private static final String CREDIT_CARD_NUMBER = "1234";
    private static final String CUSTOMER_NR = "C-1";

    @EJB
    private CustomerService customerService;


    @Test
    public void testRegisterCustomer() throws Exception {
        CustomerDTO customerDTO = createCustomer();

        customerDTO = customerService.registerCustomer(customerDTO, PASSWORD);

        Assert.assertTrue(customerDTO.getNumber().startsWith("C-"));
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testRegisterNullCustomer() throws CustomerAlreadyExistsException {
        customerService.registerCustomer(null, "password");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testRegisterCustomerNullPassword() throws CustomerAlreadyExistsException {
        CustomerDTO customerDTO = createCustomer();
        customerService.registerCustomer(customerDTO, null);
    }


    @Test(dependsOnMethods = "testRegisterCustomer", expectedExceptions = CustomerAlreadyExistsException.class)
    public void testRegisterCustomerTwice() throws Exception {
        CustomerDTO customerDTO = createCustomer();

        customerService.registerCustomer(customerDTO, PASSWORD);
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testCreateCustomerInvalidAdress() throws Exception {
        CustomerDTO customerDTO = createCustomer();
        customerDTO.getAddress().setCountry(null);
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
        customerDTO.setAddress(addressDTO);

        CreditCardDTO creditCardDTO = new CreditCardDTO();
        creditCardDTO.setType(CreditCardType.MasterCard);
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

    @Test(dependsOnMethods = "testRegisterCustomer", expectedExceptions = ValidationException.class)
    public void testAuthenticateCustomerNullPassword() throws CustomerNotFoundException, InvalidPasswordException {
        customerService.authenticateCustomer(EMAIL, null);
    }

    @Test(dependsOnMethods = "testRegisterCustomer", expectedExceptions = CustomerNotFoundException.class)
    public void testAuthenticateInvalidCustomer() throws CustomerNotFoundException, InvalidPasswordException {
        customerService.authenticateCustomer("invalid@invalid.com", PASSWORD);
    }

    @Test(dependsOnMethods = "testRegisterCustomer", expectedExceptions = ValidationException.class)
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

    @Test(dependsOnMethods = "testRegisterCustomer", expectedExceptions = ValidationException.class)
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

    @Test(dependsOnMethods = "testRegisterCustomer", expectedExceptions = ValidationException.class)
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

        // reset password
        customerService.changePassword(EMAIL, PASSWORD);
        customerService.authenticateCustomer(EMAIL, PASSWORD);

        String email = "new_adress@gmail.com";
        customer.setEmail(email);
        customerService.updateCustomer(customer);

        CustomerDTO updatedCustomer = customerService.findCustomerByEmail(email);
        Assert.assertEquals(email, updatedCustomer.getEmail());

        customerService.authenticateCustomer(email, PASSWORD);
    }

    @Test(dependsOnMethods = "testRegisterCustomer", expectedExceptions = CustomerAlreadyExistsException.class)
    public void testUpdateCustomerDuplicateEMail() throws Exception {
        CustomerDTO customer2 = createCustomer();
        String email = "email2@email.com";
        customer2.setEmail(email);
        customer2 = customerService.registerCustomer(customer2, PASSWORD);

        Assert.assertNotNull(customer2.getNumber());

        // Find customer 1
        CustomerDTO customer = customerService.findCustomer(CUSTOMER_NR);
        customer.setEmail(email);
        customerService.updateCustomer(customer);

    }

    @Test(dependsOnMethods = "testRegisterCustomer", expectedExceptions = CustomerNotFoundException.class)
    public void testUpdateCustomerInvalidNumber() throws Exception {
        CustomerDTO customer3 = createCustomer();
        String email = "email5535@email.com";
        customer3.setNumber("123123");
        customerService.updateCustomer(customer3);

        // Find customer 1
        CustomerDTO customer = customerService.findCustomer(CUSTOMER_NR);
        customer.setEmail(email);
        customerService.updateCustomer(customer);

    }
}