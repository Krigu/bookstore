package org.books.data.dao;

import org.books.data.dao.generic.GenericDAO;
import org.books.data.entity.Login;

import javax.ejb.Local;
import javax.persistence.EntityNotFoundException;

/**
 * <h1>LoginDAOLocal</h1>
 * <p>
 * Interface for the Login DAO layer
 * </p>
 **/
@Local
public interface LoginDAOLocal extends GenericDAO<Login>{
        /**
     * <h1>Login find(String username)</h1>
     *
     * <p>
     * a login with a particular user name
     * </p>
     *
     * @param username
     * @exception EntityNotFoundException happen if the login isn't find
     *
     * @return the found entity instance
     */
    public Login find(String username);
}
