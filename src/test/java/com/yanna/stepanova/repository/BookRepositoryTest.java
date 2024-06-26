package com.yanna.stepanova.repository;

import com.yanna.stepanova.model.Book;
import com.yanna.stepanova.repository.book.BookRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    private static final String AUTHOR = "marissa meyer";
    @Autowired
    private BookRepository bookRepo;

    @BeforeAll
    public static void beforeAll(@Autowired DataSource dataSource) throws SQLException {
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/book/repository/add-default-book.sql"));
        }
    }

    @AfterAll
    public static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    public static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(
                            "database/book/repository/remove-all-books-and-categories.sql"));
        }
    }

    @Test
    @DisplayName(" all five books")
    @Sql(scripts = "classpath:database/book/repository/add-four-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/repository/remove-four-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAll_FiveBooks_Ok() {
        Page<Book> actual = bookRepo.findAll(PageRequest.of(0, 10));
        Assertions.assertEquals(5, actual.getTotalElements());
    }

    @Test
    @DisplayName("Find a default book by id")
    public void findById_DefaultBook_Ok() {
        Assertions.assertTrue(bookRepo.findById(1L).isPresent());
    }

    @Test
    @DisplayName("Find a non-existing book by id")
    public void findById_NonExistingBook_notOk() {
        Assertions.assertFalse(bookRepo.findById(2L).isPresent());
    }

    @Test
    @DisplayName("Find books by existing author")
    @Sql(scripts = "classpath:database/book/repository/add-four-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/repository/remove-four-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByAuthorContainsIgnoreCase_ExistingAuthor_Ok() {
        List<Book> expected = bookRepo.findAllByAuthorContainsIgnoreCase(AUTHOR);
        Assertions.assertEquals(expected.size(), 4);
    }

    @Test
    @DisplayName("Find books by non-existing author")
    public void findAllByAuthorContainsIgnoreCase_NonExistingAuthor_Ok() {
        List<Book> expected = bookRepo.findAllByAuthorContainsIgnoreCase(AUTHOR);
        Assertions.assertTrue(expected.isEmpty());
    }

    @Test
    @DisplayName("Find all books by valid category id")
    @Sql(scripts = {"classpath:database/book/repository/add-four-books.sql",
            "classpath:database/book/repository/add-category-for-four-books.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:"
            + "database/book/repository/remove-all-entities-of-books_categories.sql",
            "classpath:database/book/repository/remove-four-books.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategorySet_Id_ValidCategoryId_Ok() {
        List<Book> expected = bookRepo.findAllByCategorySet_Id(2L, PageRequest.of(0, 10));
        Assertions.assertEquals(expected.size(), 4);
    }

    @Test
    @DisplayName("Get empty list of book by valid category id and empty table 'books_categories'")
    public void findAllByCategorySet_Id_ValidCategoryIdAndEmptyTable_Ok() {
        List<Book> expected = bookRepo.findAllByCategorySet_Id(2L, PageRequest.of(0, 10));
        Assertions.assertTrue(expected.isEmpty());
    }
}
