package org.books.data.entity;


import org.books.data.PopulateDBJpaTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class LoginTest extends PopulateDBJpaTest {

    @Test
    public void loginFindByUsernamTest() {
        TypedQuery<Login> query = em.createNamedQuery(Login.FIND_BY_USERNAME, Login.class);
        final String user = "uSeR";
        query.setParameter("username", user);

        Login l = query.getSingleResult();

        Assert.assertEquals("user", l.getUserName());
        Assert.assertEquals("password", l.getPassword());

    }

    @Test(expectedExceptions = NoResultException.class)
    public void loginFindByUsernameTestNullValue() {
        TypedQuery<Login> query = em.createNamedQuery(Login.FIND_BY_USERNAME, Login.class);
        query.setParameter("username", "abc");

        Login login = query.getSingleResult();
        Assert.assertNull(login);
    }
}
