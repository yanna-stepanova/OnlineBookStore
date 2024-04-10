package com.yanna.stepanova.dao;

import com.yanna.stepanova.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
