package org.books.data.dto;

import org.books.data.entity.Order;
import org.books.data.entity.Order.Status;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"number", "date","amount","status"})
public class OrderInfo implements Serializable{

	private String number;
	private Date date;
	private BigDecimal amount;
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
