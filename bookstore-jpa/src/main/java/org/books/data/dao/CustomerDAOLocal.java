package org.books.data.dao;

import org.books.data.dao.generic.GenericDAO;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityNotFoundException;
import org.books.data.dto.CustomerInfo;
import org.books.data.entity.Customer;

/**
 * <h1>CustomerDAOLocal</h1>
 * <p>
 * Interface for the Customer DAO layer
 * </p>
 *
 * @author Thomas Jeanmonod
 *
 **/
@Local
public interface CustomerDAOLocal extends GenericDAO<Customer>{
    /**
     * <h1>T find(String number)</h1>
     *
     * <p>
     * a customer with a particular user name
     * </p>
     *
     * @param email email of the customer
     * @exception EntityNotFoundException happen if the customer isn't find
     *
     * @return the found entity instance
     */
    public Customer find(String email) throws EntityNotFoundException;
    /**
     * <h1>List<CustomerInfo> search(String name)</h1>
     *
     * <p>
     * information on all the customers whose first or last name contains a particular name
     * </p>
     *
     * @param name name of the customers
     *
     * @return A list of founded CustomerInfo
     */
    public List<CustomerInfo> search(String name);
}
