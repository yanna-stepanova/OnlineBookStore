package com.yanna.stepanova.repository;

import com.yanna.stepanova.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);

    Optional<Book> findBookById(Long id);

    Optional<List<Book>> findAllByAuthor(String author);

    Optional<List<Book>> findAll();
}
