/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.data.entity;

import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import org.books.data.dao.CustomerDAOBean;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author tjd
 */
public class CustomerCRUDTest extends PopulateDBJpaTest {

    private static final String CITY_NAME_1 = "city 1";
    private static final String CARD_NUMBER_1 = "1234";

    @Test
    public void crudCustomer() {
        EntityTransaction tx = em.getTransaction();
        CustomerDAOBean bean = new CustomerDAOBean();
        bean.setEntityManager(em);

        //Create
        Address address = new Address("street 1", CITY_NAME_1, "1111", "CH");
        CreditCard cc = new CreditCard(CreditCard.Type.MasterCard, CARD_NUMBER_1, 12, 2020);
        Customer customer = new Customer("b@2.com", "first name 1", "last name 1", address, cc);
        tx.begin();
        Customer createCustomer = bean.create(customer);
        tx.commit();
        Assert.assertTrue(createCustomer.getId() != 0);
        Assert.assertTrue(createCustomer.getAddress().getCity().equals(CITY_NAME_1));
        Assert.assertTrue(createCustomer.getCreditCard().getNumber().equals(CARD_NUMBER_1));

        //Find
        Customer findCustomer = bean.find(createCustomer.getId());
        Assert.assertTrue(findCustomer.getId().equals(createCustomer.getId()));

        //Update
        String newFirstName = "new first name";
        findCustomer.setFirstName(newFirstName);
        tx.begin();
        Customer updateCustomer = bean.update(findCustomer);
        tx.commit();
        Assert.assertTrue(updateCustomer.getFirstName().equals(newFirstName));

        //Remove
        tx.begin();
        bean.remove(updateCustomer);
        tx.commit();

        try {
            bean.find(updateCustomer.getId());
            Assert.assertTrue(false);
        } catch (EntityNotFoundException e) {
            Assert.assertTrue(true);
        }

    }
}
