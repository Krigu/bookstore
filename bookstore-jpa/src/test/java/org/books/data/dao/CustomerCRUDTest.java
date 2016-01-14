package org.books.data.dao;

import org.books.data.BasisJpaTest;
import org.books.data.dto.CreditCardType;
import org.books.data.entity.Address;
import org.books.data.entity.CreditCard;
import org.books.data.entity.Customer;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;

@Test(groups = "CustomerCRUD")
public class CustomerCRUDTest extends BasisJpaTest {

    private static final String EMAIL = "b@2.com";
    private static final String CITY_NAME_1 = "city 1";
    private static final String CARD_NUMBER_1 = "1234";

    private final Address address = new Address("street 1", CITY_NAME_1, "1111", "CH");
    private final CreditCard cc = new CreditCard(CreditCardType.MasterCard, CARD_NUMBER_1, 12, 2020);
    private Customer customer = new Customer(EMAIL, "first name 1", "last name 1", "C1", address, cc);

    private CustomerDAOBean bean;

    @BeforeClass
    public void initDAO() throws Exception {
        bean = new CustomerDAOBean();
        bean.setEntityManager(em);
    }

    @Test
    public void createCustomer() {
        transaction.begin();
        customer = bean.create(customer);
        transaction.commit();
        Assert.assertNotNull(customer.getId());
        Assert.assertEquals(customer.getAddress().getCity(), CITY_NAME_1);
        Assert.assertEquals(customer.getCreditCard().getNumber(), CARD_NUMBER_1);
    }

    @Test(dependsOnMethods = "createCustomer")
    public void findCustomerById() {
        em.clear();
        customer = bean.find(customer.getId());
        em.clear();
        Assert.assertNotNull(customer);
    }

    @Test(dependsOnMethods = "createCustomer", expectedExceptions = PersistenceException.class)
    public void checkUniqueISBN() {
        Customer checkCustomer = new Customer();
        checkCustomer.setFirstName("first name");
        checkCustomer.setLastName("Last name");
        checkCustomer.setEmail(EMAIL);

        transaction.begin();
        bean.create(checkCustomer);
        transaction.commit();
    }

    @Test(dependsOnMethods = "findCustomerById")
    public void updateCustomer() {
        //Update
        String newFirstName = "new first name";
        customer.setFirstName(newFirstName);
        transaction.begin();
        customer = bean.update(customer);
        transaction.commit();
        em.clear();
        Assert.assertEquals(customer.getFirstName(), newFirstName);
    }

    @Test(dependsOnMethods = "updateCustomer", expectedExceptions = EntityNotFoundException.class)
    public void removeCustomer() {
        transaction.begin();
        bean.remove(customer);
        transaction.commit();
        em.clear();

        bean.find(customer.getId());
    }

}
