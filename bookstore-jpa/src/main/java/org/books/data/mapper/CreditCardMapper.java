package org.books.data.mapper;

import org.apache.commons.beanutils.BeanUtils;
import org.books.data.dto.CreditCardDTO;
import org.books.data.entity.CreditCard;

import java.lang.reflect.InvocationTargetException;

public class CreditCardMapper {

    public static CreditCardDTO toDTO(CreditCard creditCard) {

        CreditCardDTO creditCardDTO = new CreditCardDTO();
        try {
            BeanUtils.copyProperties(creditCardDTO, creditCard);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return creditCardDTO;
    }

    public static CreditCard toEntity(CreditCardDTO creditCardDTO) {

        CreditCard creditCard = new CreditCard();
        try {
            BeanUtils.copyProperties(creditCard, creditCardDTO);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return creditCard;
    }
}
