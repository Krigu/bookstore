package org.books.data.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "orderRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"customerNr", "items"})
public class OrderRequest implements Serializable {

    @NotNull
    private String customerNr;

    @NotNull
    @Size(min = 1)
    @Valid
    private List<OrderItemDTO> items;

    public OrderRequest() {

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
