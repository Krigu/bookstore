package org.books.presentation;

import org.books.application.*;
import org.books.data.dto.BookInfo;
import org.books.data.entity.Book;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.books.util.MessageFactory;

@SessionScoped
@Named("catalogBean")
public class CatalogBean implements Serializable {

    @Inject
    private Bookstore bookstore;

    private Book selectedBook;

    private List<BookInfo> booksInfoList;

    private String searchString;

    public String findBooks() {
        this.booksInfoList = bookstore.searchBooks(searchString);

        try {
            Book book = bookstore.findBook(searchString);
            BookInfo bookInfo = new BookInfo(book);
            if (!this.booksInfoList.contains(bookInfo)) {
                this.booksInfoList.add(bookInfo);
            }

        } catch (BookstoreException e) {
            //Nothing
        }
        //Send a message if the list is empty
        if (booksInfoList.isEmpty()) {
            MessageFactory.info("noBooksFound");
        }
        return null;
    }

    public String setDetail(BookInfo b) {
        try {
            this.selectedBook = bookstore.findBook(b.getIsbn());
        } catch (BookstoreException e) {
            this.selectedBook = null;
        }
        return "bookDetails?faces-redirect=true";
    }

    /**
     *
     * @return true if the selected book isn't the last element of the booklist
     */
    public boolean hasNext() {
        BookInfo bookInfo = new BookInfo(selectedBook);
        int indexBook = booksInfoList.indexOf(bookInfo);
        return indexBook < booksInfoList.size() - 1;
    }

    /**
     *
     * @return true if the selected book isn't the first element of the booklist
     */
    public boolean hasPrevious() {
        BookInfo bookInfo = new BookInfo(selectedBook);
        int indexBook = booksInfoList.indexOf(bookInfo);
        return indexBook > 0;
    }

    /**
     *
     * @param difference difference of position in the booksInfoList between the
     * selectedbook and the other book
     * @return go on the bookDetail if we found the other book
     */
    public String navigateOnBookList(int difference) {
        BookInfo bi = new BookInfo(selectedBook);
        int indexBook = booksInfoList.indexOf(bi);
        try {
            bi = booksInfoList.get(indexBook + difference);
            return setDetail(bi);
        } catch (IndexOutOfBoundsException e) {
            //Nothing
            return null;
        }
    }

    public Book getselectedBook() {
        return selectedBook;
    }

    public List<BookInfo> getBooksInfoList() {
        return booksInfoList;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getSearchString() {
        return searchString;
    }

}
