package org.books.data.dao;

import org.books.data.dao.generic.GenericDAOImpl;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;
import org.books.data.dto.CustomerInfo;
import org.books.data.entity.Customer;

/**
 * <h1>CustomerDAOBean</h1>
 * <p>
 * class DAO for the entity Customer
 * </p>
 *
 * @author Thomas Jeanmonod
 *
 *
 */
@Stateless
public class CustomerDAOBean extends GenericDAOImpl<Customer> implements CustomerDAOLocal {

    private static final Logger LOGGER = Logger.getLogger(CustomerDAOBean.class);

    public CustomerDAOBean() {
        super(Customer.class);
    }

    @Override
    public Customer find(String email) throws EntityNotFoundException {
        TypedQuery<Customer> query = entityManager.createNamedQuery(Customer.FIND_BY_EMAIL, Customer.class);
        query.setParameter("email", email);
        try {
            return query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public List search(String name) {
        TypedQuery<CustomerInfo> query = entityManager.createNamedQuery(Customer.FIND_BY_NAME, CustomerInfo.class);
        query.setParameter("firstName", name);
        query.setParameter("lastName", name);
        return query.getResultList();
    }

}
