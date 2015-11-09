package org.books.presentation;

import org.books.application.*;
import org.books.data.dto.BookInfo;
import org.books.data.entity.Book;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import org.books.util.MessageFactory;

@SessionScoped
@Named("catalogBean")
public class CatalogBean implements Serializable {

    @Inject
    private Bookstore bookstore;

    private String isbn;

    private Book selectedBook;

    private List<BookInfo> booksInfoList;

    private String searchString;

    public void loadSelectedBook() {
        try {
            this.selectedBook = bookstore.findBook(isbn);
        } catch (BookstoreException e) {
            this.selectedBook = null;
            MessageFactory.info("noBooksFound");
        }
    }

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
        return "/bookDetails?faces-redirect=true&menuId=0&isbn=" + b.getIsbn();
    }

    /**
     *
     * @return true if the selected book isn't the last element of the booklist
     */
    public boolean hasNext() {
        return indexOfBookInBookInfoList(selectedBook) < booksInfoList.size()-1;
    }

    /**
     *
     * @return true if the selected book isn't the first element of the booklist
     */
    public boolean hasPrevious() {
        return indexOfBookInBookInfoList(selectedBook) > 0;
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

    private int indexOfBookInBookInfoList(Book book) {
        int indexBook = -1;
        try {
            BookInfo bookInfo = new BookInfo(book);
            indexBook = booksInfoList.indexOf(bookInfo);
        } catch (Exception e) {
            //Nothing
        }
        return indexBook;
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

}
