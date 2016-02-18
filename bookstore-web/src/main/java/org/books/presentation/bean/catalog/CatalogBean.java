package org.books.presentation.bean.catalog;

import org.books.util.MessageFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Inject;
import org.books.application.CatalogService;
import org.books.application.exception.BookAlreadyExistsException;
import org.books.application.exception.BookNotFoundException;
import org.books.data.dto.BookDTO;
import org.books.data.dto.BookInfo;

@SessionScoped
@Named("catalogBean")
public class CatalogBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CatalogBean.class.getName());
    public static final String SEARCH_EMPTY ="org.books.presentation.bean.catalog.catalogbean.SEARCH_EMPTY";
    public static final String BOOK_NOT_FOUND = "org.books.presentation.bean.catalog.catalogbean.BOOK_NOT_FOUND";
    public static final String BOOK_ALREADY_EXISTS="org.books.presentation.bean.catalog.catalogbean.BOOK_ALREADY_EXISTS";

    @EJB
    private CatalogService catalogService;

    //ISBN of the selected book
    private String isbn;
    //Book to show on the detail page
    private BookDTO selectedBook;
    //Results of the search
    private List<BookInfo> booksInfoList;

    private String searchString;

    @PostConstruct
    public void init() {
        booksInfoList = new ArrayList<>();

    }

    /*
    * Use this method to add Book to BookStore
    */
    public void addBookDTO(BookDTO book) {
        try {
            catalogService.addBook(book);
        } catch (BookAlreadyExistsException ex) {
            LOGGER.log(Level.WARNING, "Book already exist", ex);
            MessageFactory.info(BOOK_ALREADY_EXISTS);
        }
    }

    /**
     * Use this method to load a book (before rendering the details page)
     */
    public void loadSelectedBook() {
        try {
            this.selectedBook = catalogService.findBook(isbn);
        } catch (BookNotFoundException ex) {
            LOGGER.log(Level.WARNING, "Book not found", ex);
            this.selectedBook = null;
            MessageFactory.info(BOOK_NOT_FOUND);
        }
    }

    /**
     * Find the books for the searchString
     *
     * @return null (No navigation)
     */
    public String findBooks() {
        this.booksInfoList = catalogService.searchBooks(searchString);

        //Send a message if the list is empty
        if (booksInfoList.isEmpty()) {
            MessageFactory.info(SEARCH_EMPTY);
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
     * selectedbook and the other book
     * @return go on the bookDetail if we found the other book
     */
    public String navigateOnBookList(int difference) {
        BookInfo bi = new BookInfo(selectedBook);
        int indexBook = booksInfoList.indexOf(bi);
        try {
            bi = booksInfoList.get(indexBook + difference);
            return goOnPageDetail(bi);
        } catch (IndexOutOfBoundsException e) {
            LOGGER.log(Level.WARNING, "No next book element", e);
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
    private int indexOfBookInBookInfoList(BookDTO book) {
        int indexBook = -1;
        try {
            BookInfo bookInfo = new BookInfo(book);
            indexBook = booksInfoList.indexOf(bookInfo);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Book not found in bookInfoList", e);
            //Nothing
        }
        return indexBook;
    }

    public BookDTO getSelectedBook() {
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
