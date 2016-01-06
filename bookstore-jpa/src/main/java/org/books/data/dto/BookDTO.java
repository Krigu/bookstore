
package org.books.data.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import org.books.data.entity.Book;

public class BookDTO implements Serializable {
    private String authors; 
    private Book.Binding binding;
    private String isbn; 
    private Integer numberOfPages; 
    private BigDecimal price; 
    private Integer publicationYear; 
    private String publisher; 
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
