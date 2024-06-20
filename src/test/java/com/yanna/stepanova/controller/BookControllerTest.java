package com.yanna.stepanova.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yanna.stepanova.dto.book.BookDto;
import com.yanna.stepanova.dto.book.CreateBookRequestDto;
import jakarta.servlet.ServletException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void beforeAll(@Autowired DataSource dataSource,
                          @Autowired WebApplicationContext appContext) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(appContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/book/controller/"
                            + "add-three-entities-in-books-and-books_categories.sql"));
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
                    new ClassPathResource("database/book/controller/"
                            + "remove-all-from-books-and-books_categories.sql"));
        }
    }

    @Test
    @DisplayName("Create a new book")
    @Sql(scripts = "classpath:database/book/controller/remove-book-by-isbn.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createBook_ValidRequestDto_Success() throws Exception {
        //given
        CreateBookRequestDto requestDto = new CreateBookRequestDto("New title", "New author",
                "978-161-729-045-9", BigDecimal.valueOf(123.45), Set.of(),
                "New description", "new cover image");
        BookDto expected = new BookDto();
        expected.setTitle(requestDto.title());
        expected.setAuthor(requestDto.author());
        expected.setPrice(requestDto.price());
        expected.setIsbn(requestDto.isbn());
        expected.setDescription(requestDto.description());
        expected.setCoverImage(requestDto.coverImage());
        expected.setCategoryIds(requestDto.categoryIds());
        //when
        MvcResult result = mockMvc.perform(post("/books")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        //then
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @Test
    @DisplayName("Get a book by valid id")
    @WithMockUser(username = "somebody", roles = {"ADMIN, ROLE"})
    public void getBookById_ValidId_Success() throws Exception {
        //given
        Long bookId = 1L;
        //when
        MvcResult result = mockMvc.perform(get("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        //then
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        BookDto expected = getAllBooksDto().get(0);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @DisplayName("Exception: if get a book by non existing id")
    @WithMockUser(username = "somebody", roles = {"ADMIN, ROLE"})
    public void getBookById_NonExistingId_ThrowException() throws Exception {
        //given
        Long bookId = 40L;
        //when
        try {
            MvcResult result = mockMvc.perform(get("/books/{id}", bookId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();
            BookDto actual = objectMapper.readValue(
                    result.getResponse().getContentAsString(), BookDto.class);
            //then
            Assertions.assertNull(actual, String.format("Id=%s should be non-existing", bookId));
        } catch (ServletException exception) {
            //then
            Assertions.assertEquals(String.format("Book with id: %s not found", bookId),
                    exception.getRootCause().getMessage());
        }
    }

    @Test
    @DisplayName("Get all books by an author")
    @WithMockUser(username = "somebody", roles = {"ADMIN, ROLE"})
    public void getAllByAuthor_GivenAuthor_Success() throws Exception {
        //given
        String author = "b";
        //when
        MvcResult result = mockMvc.perform(get("/books/by-author")
                        .param("author", author)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        BookDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto[].class);
        List<BookDto> expected = getAllBooksDto().stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .toList();
        //then
        Assertions.assertEquals(expected.size(), actual.length);
    }

    @Test
    @DisplayName("Get all books")
    @WithMockUser(username = "somebody", roles = {"ADMIN, ROLE"})
    public void getAll_GivenBooksInCatalog_ReturnAllBooks() throws Exception {
        //given
        List<BookDto> expected = getAllBooksDto();
        //when
        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        //then
        BookDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto[].class);
        Assertions.assertEquals(expected.size(), actual.length);
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected.get(i), actual[i]));
        }
    }

    @Test
    @DisplayName("Update a book by valid id and requestDto")
    @Sql(scripts = "classpath:database/book/controller/add-book-for-updating.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/controller/remove-updated-book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateBookById_GivenValidAndRequestDto_Success() throws Exception {
        //given
        Long bookId = 4L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto("Update title", "Update author",
                "654-123-456-789-0", BigDecimal.valueOf(99.99), Set.of(),
                "Update description", "update cover image");
        BookDto expected = new BookDto(bookId, requestDto.title(), requestDto.author(),
                requestDto.price(), requestDto.isbn(), requestDto.categoryIds(),
                requestDto.description(), requestDto.coverImage());
        //when
        MvcResult result = mockMvc.perform(put("/books/{id}", bookId)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        //then
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @DisplayName("Delete a book by valid id")
    @Sql(scripts = "classpath:database/book/controller/add-book-to-delete.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void delete_GivenValidId_Success() throws Exception {
        //given
        Long bookId = 4L;
        //when & then
        mockMvc.perform(delete("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(result -> Assertions.assertEquals(
                        "The book entity was deleted by id: " + bookId,
                        result.getResponse().getContentAsString()));
    }

    private List<BookDto> getAllBooksDto() {
        List<BookDto> bookDtoList = new ArrayList<>();
        bookDtoList.add(new BookDto(1L, "First book", "Writer A",
                BigDecimal.valueOf(1.23), "000-00-00000001", Set.of(1L),
                "Book 1", "http://example.com/cover_1.jpg"));
        bookDtoList.add(new BookDto(2L, "Second book", "Writer B",
                BigDecimal.valueOf(2.34), "000-00-00000002", Set.of(2L),
                "Book 2", "http://example.com/cover_2.jpg"));
        bookDtoList.add(new BookDto(3L, "Third book", "Writer C",
                BigDecimal.valueOf(3.45), "000-00-00000003", Set.of(3L),
                "Book 3", "http://example.com/cover_3.jpg"));
        return bookDtoList;
    }
}
