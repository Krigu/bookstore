package org.books.application;


import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.InvalidPasswordException;
import org.books.data.dto.CustomerDTO;
import org.books.data.dto.CustomerInfo;

import javax.ejb.Stateless;
import java.util.List;

@Stateless(name = "CustomerService")
public class CustomerServiceBean implements CustomerService {
    @Override
    public void authenticateCustomer(String email, String password) throws CustomerNotFoundException, InvalidPasswordException {

    }

    @Override
    public void changePassword(String email, String password) throws CustomerNotFoundException {

    }

    @Override
    public CustomerDTO findCustomer(String customerNr) throws CustomerNotFoundException {
        return null;
    }

    @Override
    public CustomerDTO findCustomerByEmail(String email) throws CustomerNotFoundException {
        return null;
    }

    @Override
    public String registerCustomer(CustomerDTO customer, String password) throws CustomerAlreadyExistsException {
        return null;
    }

    @Override
    public List<CustomerInfo> searchCustomers(String name) {
        return null;
    }

    @Override
    public void updateCustomer(CustomerDTO customer) throws CustomerNotFoundException, CustomerAlreadyExistsException {

    }
}
