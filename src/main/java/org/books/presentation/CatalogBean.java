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

    /**
     * Use this methode to find books if it doesn't find book, it send a message
     * to the user
     *
     * @return null - Stay on the same page<s
     */
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
        //Send a message if the list is empty
        if (booksList.isEmpty()) {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "No books found", "");
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, fm);
        }
        return null;
    }

    /**
     * Use this methode to go on the details page
     *
     * @param b selectedBook
     * @return the details page
     */
    public String setDetail(Book b) {
        this.selectedBook = b;
        return "bookDetails?faces-redirect=true";
    }

    /**
     * Use this methode to show the next book
     *
     * @return go on the details page
     */
    public String nextBook() {
        int indexBook = booksList.indexOf(selectedBook);
        try {
            return setDetail(booksList.get(indexBook + 1));
        } catch (IndexOutOfBoundsException e) {
            //Nothing
            return null;
        }
    }

    /**
     * Use this methode to show the previous book
     *
     * @return go on the details page
     */
    public String previousBook() {
        int indexBook = booksList.indexOf(selectedBook);
        try {
            return setDetail(booksList.get(indexBook - 1));
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     *
     * @return true if the selected book isn't the last element of the booklist
     */
    public boolean hasNext() {
        int indexBook = booksList.indexOf(selectedBook);
        return indexBook < booksList.size() - 1;
    }

    /**
     *
     * @return true if the selected book isn't the first element of the booklist
     */
    public boolean hasPrevious() {
        int indexBook = booksList.indexOf(selectedBook);
        return indexBook > 0;
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
