package org.books.data.entity;


import org.testng.Assert;
import org.testng.annotations.Test;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class LoginTest extends AbstractJpaTest {

    @Test
    public void LoginFindByUsernamTest() {
        TypedQuery<Login> query = em.createNamedQuery("Login.findByUsername", Login.class);
        final String user = "uSeR";
        query.setParameter("username", user);

        Login l = query.getSingleResult();

        Assert.assertEquals("user",l.getUserName());
        Assert.assertEquals("password",l.getPassword());

    }

    @Test(expectedExceptions = NoResultException.class)
    public void LoginFindByUsernameTestNullValue() {
        TypedQuery<Login> query = em.createNamedQuery("Login.findByUsername", Login.class);
        query.setParameter("username", "abc");

        Login login = query.getSingleResult();

    }
}