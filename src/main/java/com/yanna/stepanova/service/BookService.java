package com.yanna.stepanova.service;

import com.yanna.stepanova.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
