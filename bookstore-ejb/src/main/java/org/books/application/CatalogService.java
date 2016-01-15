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

    public void addBook(@NotNull @Valid BookDTO book) throws BookAlreadyExistsException;

    public BookDTO findBook(@NotNull String isbn) throws BookNotFoundException;

    public List<BookInfo> searchBooks(@NotNull String keywords);

    public void updateBook(@NotNull @Valid BookDTO book) throws BookNotFoundException;
}
