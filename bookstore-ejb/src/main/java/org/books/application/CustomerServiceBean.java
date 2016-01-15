package org.books.application;


import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.InvalidPasswordException;
import org.books.application.interceptor.ValidationInterceptor;
import org.books.data.mapper.CustomerMapper;
import org.books.data.dao.CustomerDAOLocal;
import org.books.data.dao.LoginDAOLocal;
import org.books.data.dao.SequenceGeneratorDAO;
import org.books.data.dto.CustomerDTO;
import org.books.data.dto.CustomerInfo;
import org.books.data.entity.Customer;
import org.books.data.entity.Login;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import java.util.List;
import java.util.logging.Logger;

@Stateless(name = "CustomerService")
@Interceptors(ValidationInterceptor.class)
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

        if (login == null)
            throw new CustomerNotFoundException();

        login.setPassword(password);
    }

    @Override
    public CustomerDTO findCustomer(String customerNr) throws CustomerNotFoundException {

        Customer customer = customerDao.findByCustomerNumber(customerNr);
        if (customer == null)
            throw new CustomerNotFoundException();

        return CustomerMapper.toDTO(customer);
    }

    @Override
    public CustomerDTO findCustomerByEmail(String email) throws CustomerNotFoundException {

        Customer customer = customerDao.find(email);
        if (customer == null)
            throw new CustomerNotFoundException();

        return CustomerMapper.toDTO(customer);
    }


    @Override
    public CustomerDTO registerCustomer(CustomerDTO customer, String password) throws CustomerAlreadyExistsException {

        String email = customer.getEmail();

        Customer c = customerDao.find(email);
        if (c != null)
            throw new CustomerAlreadyExistsException();

        Long customerSequenceNumber = sequenceGenerator.getNextValue(CUSTOMER_SEQUENCE);
        String customerNumber = CUSTOMER_PREFIX + customerSequenceNumber;
        customer.setNumber(customerNumber);

        customerDao.create(CustomerMapper.toEntity(customer));

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

        Customer customerEntity = customerDao.findByCustomerNumber(customer.getNumber());
        if (customerEntity == null)
            throw new CustomerNotFoundException();

        // check for E-Mail update
        if (!customerEntity.getEmail().equals(customer.getEmail())) {
            if (customerDao.find(customer.getEmail()) != null) {
                throw new CustomerAlreadyExistsException();
            }
        }

        Login login = loginDao.find(customerEntity.getEmail());
        login.setUserName(customer.getEmail());
        loginDao.update(login);
        customerDao.update(CustomerMapper.toEntity(customerEntity, customer));

    }
}
