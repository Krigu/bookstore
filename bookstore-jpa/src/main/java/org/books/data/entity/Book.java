package org.books.data.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@NamedQueries({
    @NamedQuery(name = Book.FIND_BY_ISBN, query = "from Book b where UPPER(b.isbn) = UPPER(:isbn)")
})
@Entity
public class Book extends BaseEntity {

    public static final String FIND_BY_ISBN = "Book.findByISBN";

    @XmlType(name = "BookBinding")
    @XmlEnum(String.class)
    public enum Binding {

        Hardcover, Paperback, Ebook, Unknown
    }

    @Column(nullable = false, unique = true)
    private String isbn;

    private String title;
    private String authors;
    private String publisher;
    private Integer publicationYear;

    @Enumerated(EnumType.STRING)
    private Binding binding;

    private Integer numberOfPages;
    private BigDecimal price;

    public Book() {
    }

    public Book(String isbn, String title, String authors, String publisher,
            Integer publicationYear, Binding binding, Integer numberOfPages, BigDecimal price) {
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.binding = binding;
        this.numberOfPages = numberOfPages;
        this.price = price;
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

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Binding getBinding() {
        return binding;
    }

    public void setBinding(Binding binding) {
        this.binding = binding;
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
}
