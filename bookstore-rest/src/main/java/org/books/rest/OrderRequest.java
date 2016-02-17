package org.books.rest;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.books.data.dto.OrderItemDTO;

@XmlRootElement(name = "orderRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"customerNr", "items"})
public class OrderRequest implements Serializable{

    private String customerNr;
    private List<OrderItemDTO> items;

    public OrderRequest(){
        
    }
    
    public String getCustomerNr() {
        return customerNr;
    }

    public void setCustomerNr(String customerNr) {
        this.customerNr = customerNr;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }
    
    
}
