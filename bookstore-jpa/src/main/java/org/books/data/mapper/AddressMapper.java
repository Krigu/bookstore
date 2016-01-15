package org.books.data.mapper;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.Converter;
import org.books.data.dto.AddressDTO;
import org.books.data.entity.Address;

import java.lang.reflect.InvocationTargetException;

public class AddressMapper {

    class DtoToEntityConverter<AddressDTO> implements Converter {

        @Override
        public <T> T convert(Class<T> aClass, Object o) {
            return (T) toDTO((Address) o);
        }
    }


    public static AddressDTO toDTO(Address address) {

        AddressDTO addressDTO = new AddressDTO();
        try {
            BeanUtils.copyProperties(addressDTO, address);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return addressDTO;
    }

    public static Address toEntity(AddressDTO addressDTO) {

        Address address = new Address();
        try {
            BeanUtils.copyProperties(address, addressDTO);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return address;
    }


}
