package com.yanna.stepanova.repository.book;

import com.yanna.stepanova.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @EntityGraph(value = "Book.categorySet")
    Page<Book> findAll(Pageable pageable);

    @EntityGraph(value = "Book.categorySet")
    List<Book> findAll(Specification<Book> specification);

    @EntityGraph(value = "Book.categorySet")
    Optional<Book> findById(Long id);

    @EntityGraph(value = "Book.categorySet")
    List<Book> findAllByAuthorContainsIgnoreCase(String author);

    @Query(value = "SELECT * FROM books b INNER JOIN books_categories bc ON "
            + "b.id = bc.book_id WHERE bc.category_id = :categoryId", nativeQuery = true)
    List<Book> findAllByCategorySet_Id(@Param("categoryId") Long categoryId, Pageable pageable);
}
