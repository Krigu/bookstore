package org.books.data.mapper;

import org.books.data.dto.AddressDTO;
import org.books.data.dto.CreditCardDTO;
import org.books.data.dto.CreditCardType;
import org.books.data.dto.CustomerDTO;
import org.books.data.entity.Address;
import org.books.data.entity.CreditCard;
import org.books.data.entity.Customer;
import org.testng.Assert;
import org.testng.annotations.Test;


public class CustomerMapperTest {

    @Test
    public void testToDTO() throws Exception {

        Customer customer = new Customer();
        customer.setFirstName("firstname");
        customer.setLastName("lastname");
        customer.setEmail("a@b.com");

        Address address = new Address();
        address.setStreet("street");
        address.setPostalCode("3000");
        address.setCity("Bern");
        address.setCountry("Switzerland");
        customer.setAddress(address);

        CreditCard creditCard = new CreditCard();
        creditCard.setType(CreditCardType.MasterCard);
        creditCard.setNumber("1223");
        creditCard.setExpirationMonth(12);
        creditCard.setExpirationYear(2020);
        creditCard.setExpirationYear(2020);
        customer.setCreditCard(creditCard);

        CustomerDTO customerDTO = CustomerMapper.toDTO(customer);

        Assert.assertNotNull(customerDTO.getAddress());
        Assert.assertEquals("3000", customerDTO.getAddress().getPostalCode());

        Assert.assertNotNull(customerDTO.getCreditCard());
        Assert.assertEquals("1223", customerDTO.getCreditCard().getNumber());

    }

    @Test
    public void testToEntity() throws Exception {

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("firstname");
        customerDTO.setLastName("lastname");
        customerDTO.setEmail("a@b.com");

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet("street");
        addressDTO.setPostalCode("3000");
        addressDTO.setCity("Bern");
        addressDTO.setCountry("Switzerland");
        customerDTO.setAddress(addressDTO);

        CreditCardDTO creditCardDTO = new CreditCardDTO();
        creditCardDTO.setType(CreditCardType.MasterCard);
        creditCardDTO.setNumber("1223");
        creditCardDTO.setExpirationMonth(12);
        creditCardDTO.setExpirationYear(2020);
        creditCardDTO.setExpirationYear(2020);
        customerDTO.setCreditCard(creditCardDTO);

        Customer customer = CustomerMapper.toEntity(customerDTO);

        Assert.assertNotNull(customer.getAddress());
        Assert.assertEquals("3000", customer.getAddress().getPostalCode());

        Assert.assertNotNull(customer.getCreditCard());
        Assert.assertEquals("1223", customer.getCreditCard().getNumber());

    }
}