package org.books.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Created by krigu on 01.11.15.
 */
public class Messages {

    public static void show(String title, String message) {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage(title, message));

    }
}
