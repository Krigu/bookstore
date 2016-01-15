
package org.books.data.dto;

import org.books.data.entity.Book;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

public class BookDTO implements Serializable {

    @NotNull
    private String authors;
    @NotNull
    private Book.Binding binding;
    @NotNull
    @Size(min = 10, max = 14)
    private String isbn;
    @NotNull
    private Integer numberOfPages;
    @NotNull
    private BigDecimal price;
    @NotNull
    private Integer publicationYear;
    @NotNull
    private String publisher;
    @NotNull
    private String title;

    public BookDTO() {

    }

    public BookDTO(String authors, Book.Binding binding, String isbn, Integer numberOfPages, BigDecimal price, Integer publicationYear, String publisher, String title) {
        this.authors = authors;
        this.binding = binding;
        this.isbn = isbn;
        this.numberOfPages = numberOfPages;
        this.price = price;
        this.publicationYear = publicationYear;
        this.publisher = publisher;
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public Book.Binding getBinding() {
        return binding;
    }

    public void setBinding(Book.Binding binding) {
        this.binding = binding;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
