package org.books.data.dao;

import javax.persistence.EntityNotFoundException;

import org.books.data.entity.Login;
import org.books.data.PopulateDBJpaTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoginQueryTest extends PopulateDBJpaTest {

    private static LoginDAOBean bean;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        PopulateDBJpaTest.setUpBeforeClass();
        bean = new LoginDAOBean();
        bean.setEntityManager(em);
    }

    @Test
    public void LoginFindByUsernamTest() {
        String username = "test";
        Login login = bean.find(username);
        Assert.assertEquals(username, login.getUserName());
    }
    
    @Test(expectedExceptions = EntityNotFoundException.class)
    public void LoginFindByUsernameNoResults() {
        String username = "foobar";
        bean.find(username);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void LoginFindByUsernameNoResultsWithUsernameSubString() {
        String username = "te";
        bean.find(username);
    }

}