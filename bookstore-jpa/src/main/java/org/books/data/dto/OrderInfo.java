package org.books.data.dto;

import org.books.data.entity.Order;
import org.books.data.entity.Order.Status;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@XmlRootElement(name = "orderInfo")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"number", "date","amount","status"})
public class OrderInfo implements Serializable{

	private String number;
	@XmlElement(required = true)
	@XmlSchemaType(name = "dateTime")
	private Date date;
	private BigDecimal amount;
	@XmlElement(required = true)
	@XmlSchemaType(name = "string")
	private Status status;

	public OrderInfo() {
	}

	public OrderInfo(String number, Date date, BigDecimal amount, Status status) {
		this.number = number;
		this.date = date;
		this.amount = amount;
		this.status = status;
	}

	public OrderInfo(Order order) {
		this.number = order.getNumber();
		this.date = order.getDate();
		this.amount = order.getAmount();
		this.status = order.getStatus();
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
