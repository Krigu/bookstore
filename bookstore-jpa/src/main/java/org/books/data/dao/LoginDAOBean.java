/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.data.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import org.books.data.dao.generic.GenericDAOImpl;
import org.books.data.entity.Login;

/**
 * <h1>LoginDAOBean</h1>
 * <p>
 * class DAO for the entity Login
 * </p>
 *
 * @author Thomas Jeanmonod
 *
 *
 */
@Stateless
public class LoginDAOBean extends GenericDAOImpl<Login> implements LoginDAOLocal {

    public LoginDAOBean() {
        super(Login.class);
    }

    @Override
    public Login find(String username) throws EntityNotFoundException {
        TypedQuery<Login> query = entityManager.createNamedQuery(Login.FIND_BY_USERNAME, Login.class);
        query.setParameter("username", username);
        try {
            return query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

}
