package org.books.presentation.bean.catalog;

import org.books.application.Bookstore;
import org.books.application.BookstoreException;
import org.books.data.dto.BookInfo;
import org.books.data.entity.Book;
import org.books.util.MessageFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SessionScoped
@Named("catalogBean")
public class CatalogBean implements Serializable {

    public static String NO_BOOKS_FOUND = "org.books.presentation.bean.catalog.catalogbean.NO_BOOKS_FOUND";

    @Inject
    private Bookstore bookstore;

    //ISBN of the selected book
    private String isbn;
    //Book to show on the detail page
    private Book selectedBook;
    //Results of the search
    private List<BookInfo> booksInfoList;

    private String searchString;

    @PostConstruct
    public void init(){
        booksInfoList = new ArrayList<>();
    }

    /**
     * Use this method to load a book (before rendering the details page)
     */
    public void loadSelectedBook() {
        try {
            this.selectedBook = bookstore.findBook(isbn);
        } catch (BookstoreException e) {
            this.selectedBook = null;
            MessageFactory.info(NO_BOOKS_FOUND);
        }
    }

    /**
     * Find the books for the searchString
     *
     * @return null (No navigation)
     */
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
            MessageFactory.info(NO_BOOKS_FOUND);
        }
        return null;
    }

    /**
     * Go on the detail page for the BookInfo b
     *
     * @param b
     * @return
     */
    public String goOnPageDetail(BookInfo b) {
        return "/bookDetails?faces-redirect=true&menuId=0&isbn=" + b.getIsbn();
    }

    /**
     * @return true if the selected book isn't the last element of the booklist
     */
    public boolean hasNext() {

        return indexOfBookInBookInfoList(selectedBook) < booksInfoList.size() - 1;
    }

    /**
     * @return true if the selected book isn't the first element of the booklist
     */
    public boolean hasPrevious() {
        return indexOfBookInBookInfoList(selectedBook) > 0;
    }

    /**
     * @param difference difference of position in the booksInfoList between the
     *                   selectedbook and the other book
     * @return go on the bookDetail if we found the other book
     */
    public String navigateOnBookList(int difference) {
        BookInfo bi = new BookInfo(selectedBook);
        int indexBook = booksInfoList.indexOf(bi);
        try {
            bi = booksInfoList.get(indexBook + difference);
            return goOnPageDetail(bi);
        } catch (IndexOutOfBoundsException e) {
            //Nothing
            return null;
        }
    }

    /**
     * Find the index of a book in the book list
     *
     * @param book
     * @return -1 if the book isn't in the list
     */
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

    public Book getSelectedBook() {
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
