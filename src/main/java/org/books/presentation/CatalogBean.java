package org.books.presentation;

import org.books.application.ShoppingCartItem;
import org.books.application.ShoppingCart;
import org.books.application.BookNotFoundException;
import org.books.application.Bookstore;
import org.books.data.Book;
import org.books.util.Messages;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@SessionScoped
@Named("catalogBean")
public class CatalogBean implements Serializable {

    @Inject
    private Bookstore bookstore;

    private Book selectedBook;

    private List<Book> booksList;

    private String searchString;

    public String findBooks() {
        this.booksList = bookstore.searchBooks(searchString);

        try {
            Book b = bookstore.findBook(searchString);
            if (!this.booksList.contains(b)) {
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

    public Book getselectedBook() {
        return selectedBook;
    }

    public List<Book> getBooksList() {
        return booksList;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getSearchString() {
        return searchString;
    }

}
