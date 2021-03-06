package org.books.data.dto;

import org.books.data.entity.Book;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@XmlRootElement(name = "bookInfo")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"isbn", "title", "price"})
public class BookInfo implements Serializable {

	@NotNull
	private String isbn;
	@NotNull
	private String title;
	@NotNull
	private BigDecimal price;

	public BookInfo() {
	}

	public BookInfo(String isbn, String title, BigDecimal price) {
		this.isbn = isbn;
		this.title = title;
		this.price = price;
	}

	public BookInfo(Book book) {
		this.isbn = book.getIsbn();
		this.title = book.getTitle();
		this.price = book.getPrice();
	}
        
        public BookInfo(BookDTO book) {
		this.isbn = book.getIsbn();
		this.title = book.getTitle();
		this.price = book.getPrice();
	}

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.isbn);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BookInfo other = (BookInfo) obj;
        if (!Objects.equals(this.isbn, other.isbn)) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        /*if (!Objects.equals(this.price, other.price)) {
            return false;
        }*/
        return true;
    }
        

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
