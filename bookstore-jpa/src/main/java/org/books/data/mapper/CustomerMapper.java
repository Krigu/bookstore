package org.books.data.mapper;


import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.books.data.dto.AddressDTO;
import org.books.data.dto.CreditCardDTO;
import org.books.data.dto.CustomerDTO;
import org.books.data.entity.Address;
import org.books.data.entity.CreditCard;
import org.books.data.entity.Customer;

import java.lang.reflect.InvocationTargetException;

public class CustomerMapper {


    public static CustomerDTO toDTO(Customer customer) {

        ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
        convertUtilsBean.register(new Converter() {
            @Override
            public <T> T convert(Class<T> aClass, Object o) {
                return (T) AddressMapper.toDTO((Address) o);
            }
        }, AddressDTO.class);
        convertUtilsBean.register(new Converter() {
            @Override
            public <T> T convert(Class<T> aClass, Object o) {
                return (T) CreditCardMapper.toDTO((CreditCard) o);
            }
        }, CreditCardDTO.class);
        BeanUtilsBean beanUtilsBean =
                new BeanUtilsBean(convertUtilsBean, new PropertyUtilsBean());


        CustomerDTO customerDTO = new CustomerDTO();
        try {
            beanUtilsBean.copyProperties(customerDTO, customer);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return customerDTO;
    }

    public static Customer toEntity(CustomerDTO customerDTO) {
        return toEntity(new Customer(), customerDTO);
    }

    public static Customer toEntity(Customer customer, CustomerDTO customerDTO) {

        ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
        convertUtilsBean.register(new Converter() {
            @Override
            public <T> T convert(Class<T> aClass, Object o) {
                return (T) AddressMapper.toEntity((AddressDTO) o);
            }
        }, Address.class);
        convertUtilsBean.register(new Converter() {
            @Override
            public <T> T convert(Class<T> aClass, Object o) {
                return (T) CreditCardMapper.toEntity((CreditCardDTO) o);
            }
        }, CreditCard.class);
        BeanUtilsBean beanUtilsBean =
                new BeanUtilsBean(convertUtilsBean, new PropertyUtilsBean());

        try {
            beanUtilsBean.copyProperties(customer, customerDTO);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return customer;
    }

}
