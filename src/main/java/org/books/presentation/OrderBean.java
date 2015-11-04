package org.books.presentation;


import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named("orderBean")
public class OrderBean implements Serializable {
    public String doSubmitOrder() {
        return "orderConfirmation";
    }
}
