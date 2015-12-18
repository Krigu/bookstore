package org.books.data.entity.dao;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import org.books.data.dao.LoginDAOBean;
import org.books.data.BasisJpaTest;
import org.books.data.entity.Login;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = "LoginCRUD", dependsOnGroups = "BookCRUD")
public class LoginCRUDTest extends BasisJpaTest {

    private LoginDAOBean bean;

    private static final String USERNAME = "username";
    private Login login = new Login(USERNAME, "password");

    @BeforeClass
    public void initDAO() throws Exception {
        bean = new LoginDAOBean();
        bean.setEntityManager(em);
    }

    @Test
    public void createLogin() {
        //Create
        transaction.begin();
        login = bean.create(login);
        transaction.commit();
        em.clear();
        Assert.assertNotNull(login.getId());
    }

    @Test(dependsOnMethods = "createLogin", expectedExceptions = PersistenceException.class)
    public void checkUniqueUsername() {
        Login checkLogin = new Login();
        checkLogin.setUserName(USERNAME);
        //Create
        transaction.begin();
        bean.create(checkLogin);
        transaction.commit();
    }

    @Test(dependsOnMethods = "checkUniqueUsername")
    public void findLoginById() {
        em.clear();
        login = bean.find(login.getId());
        em.clear();
        Assert.assertNotNull(login);
    }

    @Test(dependsOnMethods = "findLoginById")
    public void updateLogin() {
        String newPassword = "new password";
        login.setPassword(newPassword);
        transaction.begin();
        login = bean.update(login);
        transaction.commit();
        em.clear();
        Assert.assertTrue(login.getPassword().equals(newPassword));
    }

    @Test(dependsOnMethods = "updateLogin", expectedExceptions = EntityNotFoundException.class)
    public void removeBook() {
        transaction.begin();
        bean.remove(login);
        transaction.commit();
        em.clear();
        bean.find(login.getId());
    }
}
