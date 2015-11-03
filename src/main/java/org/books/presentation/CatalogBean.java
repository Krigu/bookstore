package org.books.presentation;

import org.books.application.*;
import org.books.data.dto.BookInfo;
import org.books.data.entity.Book;

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
