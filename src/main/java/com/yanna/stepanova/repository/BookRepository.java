package com.yanna.stepanova.repository;

import com.yanna.stepanova.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<List<Book>> findAllByAuthorContainsIgnoreCase(String author);
}
