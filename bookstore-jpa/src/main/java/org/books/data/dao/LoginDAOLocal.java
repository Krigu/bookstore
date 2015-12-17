/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.data.dao;

import javax.ejb.Local;
import javax.persistence.EntityNotFoundException;
import org.books.data.dao.generic.GenericDAO;
import org.books.data.entity.Login;

/**
 * <h1>LoginDAOLocal</h1>
 * <p>
 * Interface for the Login DAO layer
 * </p>
 *
 * @author Thomas Jeanmonod
 *
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
    public Login find(String username) throws EntityNotFoundException;
}
