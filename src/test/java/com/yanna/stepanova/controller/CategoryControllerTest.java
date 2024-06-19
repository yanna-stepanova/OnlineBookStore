package com.yanna.stepanova.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yanna.stepanova.dto.book.BookDtoWithoutCategoryIds;
import com.yanna.stepanova.dto.category.CategoryDto;
import com.yanna.stepanova.dto.category.CreateCategoryRequestDto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
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
class CategoryControllerTest {
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
                    new ClassPathResource("database/category/controller/"
                            + "add-three-entities-in-categories.sql"));
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
                    new ClassPathResource("database/category/controller/"
                            + "remove-all-categories.sql"));
        }
    }

    @Test
    @DisplayName("Create a new category")
    @Sql(scripts = "classpath:database/category/controller/remove-category-by-name.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createCategory_ValidRequestDto_Success() throws Exception {
        //given
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "New category", "Description of category");
        CategoryDto expected = new CategoryDto(null, requestDto.name(), requestDto.description());
        //when
        MvcResult result = mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        //then
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.id());
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @Test
    @DisplayName("Get a category by valid id")
    @WithMockUser(username = "somebody", roles = {"ADMIN, ROLE"})
    void getCategoryById_ValidId_Success() throws Exception {
        //given
        Long categoryId = 1L;
        //when
        MvcResult result = mockMvc.perform(get("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        //then
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);
        CategoryDto expected = getAllCategoriesDto().get(0);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @DisplayName("Get all books by valid category id")
    @Sql(scripts = "classpath:"
            + "database/category/controller/add-three-entities-in-books-and-books_categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/controller/remove-books-and-books_categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getBooksByCategoryId_GivenValidId_ReturnBooksDtoWithoutCategoryIds() throws Exception {
        //given
        Long categoryId = 2L;
        List<BookDtoWithoutCategoryIds> expected = List.of(
                new BookDtoWithoutCategoryIds(7L, "First title", "Writer A",
                        BigDecimal.valueOf(111.03),"000-00-00000010", "Book 1",
                        "http://example.com/cover_1.jpg"),
                new BookDtoWithoutCategoryIds(8L, "Second title", "Writer B",
                        BigDecimal.valueOf(112.04), "000-00-00000020","Book 2",
                        "http://example.com/cover_2.jpg"));
        //when
        MvcResult result = mockMvc.perform(get("/categories/{id}/books", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        //then
        BookDtoWithoutCategoryIds[] actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDtoWithoutCategoryIds[].class);
        Assertions.assertEquals(expected.size(), actual.length);
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected.get(i), actual[i]));
        }
    }

    @Test
    @DisplayName("Get all categories")
    @WithMockUser(username = "somebody", roles = {"ADMIN, ROLE"})
    void getAll_GivenBooksInCatalog_ReturnAllBooks() throws Exception {
        //given
        List<CategoryDto> expected = getAllCategoriesDto();
        //when
        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        //then
        CategoryDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto[].class);
        Assertions.assertEquals(expected.size(), actual.length);
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected.get(i), actual[i]));
        }
    }

    @Test
    @DisplayName("Update a category by valid id and requestDto")
    @Sql(scripts = "classpath:database/category/controller/add-category-for-updating.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/controller/remove-updated-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateCategoryById_GivenValidAndRequestDto_Success() throws Exception {
        //given
        Long categoryId = 4L;
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Update category", "Description of updated category");
        CategoryDto expected = new CategoryDto(categoryId,
                requestDto.name(), requestDto.description());
        //when
        MvcResult result = mockMvc.perform(put("/categories/{id}", categoryId)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        //then
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    private List<CategoryDto> getAllCategoriesDto() {
        return List.of(
                new CategoryDto(1L, "First category", "Test category 1"),
                new CategoryDto(2L, "Second category", "Test category 2"),
                new CategoryDto(3L, "Third category", "Test category 3"));
    }
}
