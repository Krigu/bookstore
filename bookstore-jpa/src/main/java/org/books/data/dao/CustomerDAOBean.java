package org.books.data.dao;

import org.apache.log4j.Logger;
import org.books.data.dao.generic.GenericDAOImpl;
import org.books.data.dto.CustomerInfo;
import org.books.data.entity.Customer;

import javax.ejb.Stateless;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * <h1>CustomerDAOBean</h1>
 * <p>
 * class DAO for the entity Customer
 * </p>
 */
@Stateless
public class CustomerDAOBean extends GenericDAOImpl<Customer> implements CustomerDAOLocal {

    private static final Logger LOGGER = Logger.getLogger(CustomerDAOBean.class);

    public CustomerDAOBean() {
        super(Customer.class);
    }

    @Override
    public Customer find(String email) throws EntityNotFoundException {
        LOGGER.info("Find customer by email : " + email);
        TypedQuery<Customer> query = entityManager.createNamedQuery(Customer.FIND_BY_EMAIL, Customer.class);
        query.setParameter("email", email);
        try {
            return query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
    }

    @Override
    public Customer findByCustomerNumber(String customerNumber) {
        LOGGER.info("Find customer by customerNumber : " + customerNumber);
        TypedQuery<Customer> query = entityManager.createNamedQuery(Customer.FIND_BY_CUSTOMER_NUMBER, Customer.class);
        query.setParameter("number", customerNumber);
        try {
            return query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
    }

    @Override
    public List search(String name) {
        LOGGER.info("Search customers by name : " + name);
        TypedQuery<CustomerInfo> query = entityManager.createNamedQuery(Customer.FIND_BY_NAME, CustomerInfo.class);
        query.setParameter("firstName", name);
        query.setParameter("lastName", name);
        return query.getResultList();
    }

}
