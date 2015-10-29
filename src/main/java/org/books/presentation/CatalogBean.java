package org.books.presentation;

import org.books.application.BookNotFoundException;
import org.books.application.Bookstore;
import org.books.data.Book;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by krigu on 26.10.15.
 */
@SessionScoped
@Named("catalogBean")
public class CatalogBean implements Serializable {

    @Inject
    private Bookstore bookstore;

    //private String isbn;

    private Book selectedBook;

    //private String message;

    private List<Book> booksList;

    private String searchString;

  /*  public String findBook() {
        selectedBook = null;
        message = null;

        try {
            selectedBook = bookstore.findBook(isbn);
            return "bookDetails";
        } catch (BookNotFoundException e) {
            message = "Book with id \"" + isbn + " \" not found";
        }

        return null;
    }*/

    public String findBooks() {
        this.booksList = bookstore.searchBooks(searchString);

        try {
            Book b = bookstore.findBook(searchString);
            if(!this.booksList.contains(b)){
               this.booksList.add(b); 
            }
            
        } catch (BookNotFoundException e) {
            //Nothing
        }
        return null;
    }

    public String setDetail(Book b) {
        this.selectedBook = b;
        return "bookDetails?faces-redirect=true";
    }

 /*   public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }*/

    public Book getselectedBook() {
        return selectedBook;
    }

    /*public String getMessage() {
        return message;
    }*/

    public List<Book> getBooksList() {
        return booksList;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
