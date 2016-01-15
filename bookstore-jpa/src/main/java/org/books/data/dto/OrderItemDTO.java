package org.books.data.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class OrderItemDTO implements Serializable{

	@NotNull
	private BookInfo book;
	@NotNull
	@DecimalMin(value = "1")
	private Integer quantity;

	public OrderItemDTO() {
	}

	public OrderItemDTO(BookInfo book, Integer quantity) {
		this.book = book;
		this.quantity = quantity;
	}

	public BookInfo getBook() {
		return book;
	}

	public void setBook(BookInfo book) {
		this.book = book;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}
