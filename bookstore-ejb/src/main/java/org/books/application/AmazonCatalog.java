package org.books.application;

import java.util.List;
import javax.ejb.Remote;
import javax.validation.constraints.NotNull;
import org.books.application.exception.BookNotFoundException;
import org.books.data.dto.BookDTO;
import org.books.data.dto.BookInfo;

@Remote
public interface AmazonCatalog {
    
    BookDTO findBook(@NotNull String isbn) throws BookNotFoundException;

    List<BookInfo> searchBooks(@NotNull String keywords);
    
}
