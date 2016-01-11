package org.books.data.converter;


import org.books.data.dto.AddressDTO;
import org.books.data.dto.CreditCardDTO;
import org.books.data.dto.CustomerDTO;
import org.books.data.entity.Address;
import org.books.data.entity.CreditCard;
import org.books.data.entity.Customer;

public class CustomerConverter {

    public static CustomerDTO toDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setEmail(customer.getEmail());
        dto.setNumber(customer.getNumber());

        if (customer.getAddress() != null) {
            AddressDTO address = new AddressDTO();
            address.setStreet(customer.getAddress().getStreet());
            address.setPostalCode(customer.getAddress().getPostalCode());
            address.setCity(customer.getAddress().getCity());
            address.setCountry(customer.getAddress().getCountry());
            dto.setAddress(address);
        }

        if (customer.getCreditCard() != null) {
            CreditCardDTO creditCardDTO = new CreditCardDTO();
            creditCardDTO.setNumber(customer.getCreditCard().getNumber());
            creditCardDTO.setExpirationMonth(customer.getCreditCard().getExpirationMonth());
            creditCardDTO.setExpirationYear(customer.getCreditCard().getExpirationYear());
            creditCardDTO.setType(customer.getCreditCard().getType());
            dto.setCreditCard(creditCardDTO);
        }
        return dto;
    }

    public static Customer toEntity(CustomerDTO customer) {
        Customer entity = new Customer();
        entity.setFirstName(customer.getFirstName());
        entity.setLastName(customer.getLastName());
        entity.setEmail(customer.getEmail());
        entity.setNumber(customer.getNumber());

        if (customer.getAddress() != null) {
            Address a = new Address();
            setAddressAttributes(customer, a);
            entity.setAddress(a);
        }

        if (customer.getCreditCard() != null) {
            CreditCard cc = new CreditCard();
            setCreditCardAttributes(customer, cc);
            entity.setCreditCard(cc);
        }

        return entity;
    }

    public static Customer updateEntity(Customer entity, CustomerDTO customer) {
        entity.setFirstName(customer.getFirstName());
        entity.setLastName(customer.getLastName());
        entity.setEmail(customer.getEmail());
        entity.setNumber(customer.getNumber());

        if (customer.getAddress() != null) {
            Address a = entity.getAddress() == null ? new Address() : entity.getAddress();
            setAddressAttributes(customer, a);
            entity.setAddress(a);
        }

        if (customer.getCreditCard() != null) {
            CreditCard cc = entity.getCreditCard() == null ? new CreditCard() : entity.getCreditCard();
            setCreditCardAttributes(customer, cc);
            entity.setCreditCard(cc);
        }

        return entity;
    }

    private static void setAddressAttributes(CustomerDTO customer, Address a) {
        a.setStreet(customer.getAddress().getStreet());
        a.setCity(customer.getAddress().getCity());
        a.setCountry(customer.getAddress().getCountry());
        a.setPostalCode(customer.getAddress().getPostalCode());
    }

    private static void setCreditCardAttributes(CustomerDTO customer, CreditCard cc) {
        cc.setType(customer.getCreditCard().getType());
        cc.setExpirationMonth(customer.getCreditCard().getExpirationMonth());
        cc.setExpirationYear(customer.getCreditCard().getExpirationYear());
        cc.setNumber(customer.getCreditCard().getNumber());
    }
}
