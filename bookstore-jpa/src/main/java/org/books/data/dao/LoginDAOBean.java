package org.books.data.dao;

import org.apache.log4j.Logger;
import org.books.data.dao.generic.GenericDAOImpl;
import org.books.data.entity.Login;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;

/**
 * <h1>LoginDAOBean</h1>
 * <p>
 * class DAO for the entity Login
 * </p>
 */
@Stateless
public class LoginDAOBean extends GenericDAOImpl<Login> implements LoginDAOLocal {

    private static final Logger LOGGER = Logger.getLogger(LoginDAOBean.class);

    public LoginDAOBean() {
        super(Login.class);
    }

    @Override
    public Login find(String username) {
        LOGGER.info("Find login by username : " + username);
        TypedQuery<Login> query = entityManager.createNamedQuery(Login.FIND_BY_USERNAME, Login.class);
        query.setParameter("username", username);
        try {
            return query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
    }

}
