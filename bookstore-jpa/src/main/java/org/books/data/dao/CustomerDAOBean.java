package org.books.data.dao;

import org.books.data.dao.generic.GenericDAOImpl;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityNotFoundException;
import org.apache.log4j.Logger;
import org.books.data.entity.Customer;

/**
 * <h1>CustomerDAOBean</h1>
 * <p>
 * class DAO for the entity Customer
 * </p>
 *
 * @author Thomas Jeanmonod
 *
 **/
@Stateless
public class CustomerDAOBean extends GenericDAOImpl<Customer> implements CustomerDAOLocal {

    private static final Logger LOGGER = Logger.getLogger(CustomerDAOBean.class);
    
    public CustomerDAOBean(){
        super(Customer.class);
    }
   

    @Override
    public Customer find(String email) throws EntityNotFoundException {
        //TODO
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List search(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
