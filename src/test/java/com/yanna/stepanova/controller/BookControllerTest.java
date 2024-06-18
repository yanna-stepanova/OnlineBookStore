package com.yanna.stepanova.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yanna.stepanova.dto.book.BookDto;
import com.yanna.stepanova.dto.book.CreateBookRequestDto;
import jakarta.servlet.ServletException;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource,
                          @Autowired WebApplicationContext appContext) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(appContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/controller/add-three-entities-in-books-and-books_categories.sql"));
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/controller/remove-all-from-books-and-books_categories.sql"));
        }
    }

    @Test
    @DisplayName("Create a new book")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createBook_ValidRequestDto_Success() throws Exception {
        //given
        CreateBookRequestDto requestDto = new CreateBookRequestDto("New title", "New author",
                "978-161-729-045-9", BigDecimal.valueOf(123.45), Set.of(1L, 2L),
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
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @Test
    @DisplayName("Exception: if get a book by non existing id")
    @WithMockUser(username = "somebody", roles = {"ADMIN, ROLE"})
    public void getBookById_NonExistingId_ThrowException() throws Exception {
        //given
        Long bookId = 100L;
        //when
        try {
            MvcResult result = mockMvc.perform(get("/books/{id}", bookId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();
            BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
            //then
            Assertions.assertNull(actual, String.format("Id=%s should be non-existing", bookId));
        } catch (ServletException exception) {
            //then
            Assertions.assertEquals(String.format("Book with id: %s not found", bookId),
                    exception.getRootCause().getMessage());
        }
    }
}
