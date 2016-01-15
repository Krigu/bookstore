package org.books.data.dao;

import org.books.data.PopulateDBJpaTest;
import org.books.data.entity.Login;
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
    
    @Test
    public void LoginFindByUsernameNoResults() {
        String username = "foobar";
        Assert.assertNull(bean.find(username));
    }

    @Test
    public void LoginFindByUsernameNoResultsWithUsernameSubString() {
        String username = "te";
        Assert.assertNull(bean.find(username));
    }

}
