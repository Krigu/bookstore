package org.books.presentation;

import org.books.application.BookNotFoundException;
import org.books.application.Bookstore;
import org.books.data.Book;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Created by krigu on 26.10.15.
 */
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
            if(!this.booksList.contains(b)){
               this.booksList.add(b); 
            }
            
        } catch (BookNotFoundException e) {
            //Nothing
        }
        if(booksList.isEmpty()){
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "No books found", "");
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, fm);
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

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
