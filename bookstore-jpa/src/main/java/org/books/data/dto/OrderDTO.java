package org.books.data.dto;

import org.books.data.entity.Address;
import org.books.data.entity.CreditCard;
import org.books.data.entity.Order;
import org.books.data.entity.Order.Status;
import org.books.data.entity.OrderItem;
import org.books.data.mapper.AddressMapper;
import org.books.data.mapper.CreditCardMapper;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"number", "date","amount","status","customer","address","creditCard","items"})
public class OrderDTO extends OrderInfo implements Serializable {

    @NotNull
    @Valid
    private CustomerInfo customer;

    @NotNull
    @Valid
    private AddressDTO address;

    @NotNull
    @Valid
    private CreditCardDTO creditCard;

    @NotNull
    @Size(min = 1)
    @Valid
    private List<OrderItemDTO> items;

    public OrderDTO() {
    }

    public OrderDTO(String number, Date date, BigDecimal amount, Status status,
                    CustomerInfo customer, Address address, CreditCard creditCard, List<OrderItemDTO> items) {
        super(number, date, amount, status);
        this.customer = customer;
        this.address = AddressMapper.toDTO(address);
        this.creditCard = CreditCardMapper.toDTO(creditCard);
        this.items = items;
    }

    public OrderDTO(Order order) {
        super(order);
        this.customer = new CustomerInfo(order.getCustomer());
        this.address = AddressMapper.toDTO(order.getAddress());
        this.creditCard = CreditCardMapper.toDTO(order.getCreditCard());
        this.items = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            BookInfo book = new BookInfo(item.getBook().getIsbn(), item.getBook().getTitle(), item.getPrice());
            items.add(new OrderItemDTO(book, item.getQuantity()));
        }
    }

    public CustomerInfo getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerInfo customer) {
        this.customer = customer;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public CreditCardDTO getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCardDTO creditCard) {
        this.creditCard = creditCard;
    }

    public List<OrderItemDTO> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }
}
