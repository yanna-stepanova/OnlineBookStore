package com.yanna.stepanova.repository;

import com.yanna.stepanova.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByAuthorContainsIgnoreCase(String author);
}
