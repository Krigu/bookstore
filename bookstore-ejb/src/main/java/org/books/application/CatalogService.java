package org.books.application;

import java.util.List;
import javax.ejb.Remote;
import org.books.application.exception.BookAlreadyExistsException;
import org.books.application.exception.BookNotFoundException;
import org.books.data.dto.BookDTO;
import org.books.data.dto.BookInfo;

@Remote
public interface CatalogService {

	public void addBook(BookDTO book) throws BookAlreadyExistsException;

	public BookDTO findBook(String isbn) throws BookNotFoundException;
        
        public List<BookInfo> searchBooks(String keywords);
        
        public void updateBook(BookDTO book) throws BookNotFoundException;
}
