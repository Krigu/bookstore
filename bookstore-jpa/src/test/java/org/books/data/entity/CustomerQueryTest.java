/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.data.entity;

import java.util.List;
import org.books.data.dao.CustomerDAOBean;
import org.books.data.dto.CustomerInfo;
import static org.books.data.entity.AbstractJpaTest.em;
import org.testng.annotations.BeforeClass;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author tjd
 */
public class CustomerQueryTest extends AbstractJpaTest {

    private static CustomerDAOBean bean;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        AbstractJpaTest.setUpBeforeClass();
        bean = new CustomerDAOBean();
        bean.setEntityManager(em);
    }

    @Test
    public void findByName() {
        List<CustomerInfo> customers = bean.search("Alice");
        Assert.assertEquals(customers.size(), 1);
    }

    @Test
    public void findByName2Results() {
        List<CustomerInfo> customers = bean.search("smith");
        Assert.assertEquals(customers.size(), 2);
    }

    @Test
    public void finbByNameNoResult() {
        List<CustomerInfo> customers = bean.search("dupond");
        Assert.assertEquals(customers.size(), 0);
    }

}
