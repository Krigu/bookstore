package org.books.data.entity.dao;

import java.util.List;
import org.books.data.dao.CustomerDAOBean;
import org.books.data.dto.CustomerInfo;
import org.books.data.entity.PopulateDBJpaTest;
import org.testng.annotations.BeforeClass;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author tjd
 */
public class CustomerQueryTest extends PopulateDBJpaTest {

    private static CustomerDAOBean bean;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        PopulateDBJpaTest.setUpBeforeClass();
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
