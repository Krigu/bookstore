/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Tilmann Bück
 */
@FacesConverter(CreditCardConverter.CONVERTER_ID)
public class CreditCardConverter implements Converter {

    public static final String CONVERTER_ID = "org.books.presentation.converter.creditcardconverter";

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return value.replaceAll(" ", "");
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String str = value.toString();
        return str.substring(0, 4) + " " + str.substring(4, 8) + " " + str.substring(8, 12) + " " + str.substring(12, 16);
    }

}
