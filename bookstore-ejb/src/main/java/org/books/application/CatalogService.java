package org.books.application;

import org.books.application.exception.BookAlreadyExistsException;
import org.books.application.exception.BookNotFoundException;
import org.books.data.dto.BookDTO;
import org.books.data.dto.BookInfo;

import javax.ejb.Remote;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Remote
public interface CatalogService {

    void addBook(@NotNull @Valid BookDTO book) throws BookAlreadyExistsException;

    BookDTO findBook(@NotNull String isbn) throws BookNotFoundException;

    List<BookInfo> searchBooks(@NotNull String keywords);

    void updateBook(@NotNull @Valid BookDTO book) throws BookNotFoundException;
}
