package org.books.application;


import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.InvalidPasswordException;
import org.books.data.converter.CustomerConverter;
import org.books.data.dao.CustomerDAOLocal;
import org.books.data.dao.LoginDAOLocal;
import org.books.data.dao.SequenceGeneratorDAO;
import org.books.data.dto.CustomerDTO;
import org.books.data.dto.CustomerInfo;
import org.books.data.entity.Customer;
import org.books.data.entity.Login;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.util.List;
import java.util.logging.Logger;

@Stateless(name = "CustomerService")
/**
 * TODO: password validation (complexity)
 */
public class CustomerServiceBean implements CustomerService {

    private static final Logger LOGGER = Logger.getLogger(CatalogServiceBean.class.getName());
    private static final String CUSTOMER_SEQUENCE = "CUSTOMER_SEQUENCE";
    private static final String CUSTOMER_PREFIX = "C-";

    @EJB
    private CustomerDAOLocal customerDao;

    @EJB
    private LoginDAOLocal loginDao;

    @EJB
    private SequenceGeneratorDAO sequenceGenerator;

    @Override
    public void authenticateCustomer(String email, String password) throws CustomerNotFoundException, InvalidPasswordException {

        if (email == null)
            throw new CustomerNotFoundException();

        Customer customer = customerDao.find(email);
        Login login = loginDao.find(email);

        if (customer == null || login == null)
            throw new CustomerNotFoundException();

        if (password == null)
            throw new InvalidPasswordException();

        if (!login.getPassword().equals(password))
            throw new InvalidPasswordException();

    }

    @Override
    public void changePassword(String email, String password) throws CustomerNotFoundException {

        if (email == null)
            throw new CustomerNotFoundException();

        Login login = loginDao.find(email);

        // TODO: check password

        if (login == null)
            throw new CustomerNotFoundException();

        login.setPassword(password);
    }

    @Override
    public CustomerDTO findCustomer(String customerNr) throws CustomerNotFoundException {
        if (customerNr == null)
            throw new CustomerNotFoundException();

        Customer customer = customerDao.findByCustomerNumber(customerNr);
        if (customer == null)
            throw new CustomerNotFoundException();

        return CustomerConverter.toDTO(customer);
    }

    @Override
    public CustomerDTO findCustomerByEmail(String email) throws CustomerNotFoundException {
        if (email == null)
            throw new CustomerNotFoundException();

        Customer customer = customerDao.find(email);
        if (customer == null)
            throw new CustomerNotFoundException();

        return CustomerConverter.toDTO(customer);
    }


    @Override
    public CustomerDTO registerCustomer(CustomerDTO customer, String password) throws CustomerAlreadyExistsException {

        // TODO
        if (password == null) {
            throw new EJBException("Password should not be null");
        }
        if (customer == null) {
            throw new EJBException("Customer should not be null");
        }

        String email = customer.getEmail();

        Customer c = customerDao.find(email);
        if (c != null)
            throw new CustomerAlreadyExistsException();

        Long customerSequenceNumber = sequenceGenerator.getNextValue(CUSTOMER_SEQUENCE);
        String customerNumber = CUSTOMER_PREFIX + customerSequenceNumber;
        customer.setNumber(customerNumber);

        customerDao.create(CustomerConverter.toEntity(customer));

        Login login = new Login();
        login.setUserName(customer.getEmail());
        login.setPassword(password);

        loginDao.create(login);

        return customer;
    }


    @Override
    public List<CustomerInfo> searchCustomers(String name) {

        return customerDao.search(name);

    }

    @Override
    public void updateCustomer(CustomerDTO customer) throws CustomerNotFoundException, CustomerAlreadyExistsException {

        if (customer == null || customer.getNumber() == null)
            throw new CustomerNotFoundException();

        Customer entity = customerDao.findByCustomerNumber(customer.getNumber());
        if (entity == null)
            throw new CustomerNotFoundException();

        // check for E-Mail update
        if (!entity.getEmail().equals(customer.getEmail())) {
            if (customerDao.find(customer.getEmail()) != null) {
                throw new CustomerAlreadyExistsException();
            }
        }

        Login login = loginDao.find(entity.getEmail());
        login.setUserName(customer.getEmail());
        loginDao.update(login);

        customerDao.update(CustomerConverter.updateEntity(entity, customer));

    }
}
