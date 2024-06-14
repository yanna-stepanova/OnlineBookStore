package com.yanna.stepanova.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yanna.stepanova.dto.book.BookDto;
import com.yanna.stepanova.dto.book.CreateBookRequestDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Set;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext appContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(appContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "admin@example.com", roles = {"ROLE_ADMIN"})
    @Test
    @DisplayName("Create a new book")
    public void createBook_ValidRequestDto_Success() throws Exception {
        //given
        CreateBookRequestDto requestDto = new CreateBookRequestDto("New title", "New author",
                "123-12-12345678", BigDecimal.valueOf(123.45), Set.of(1L, 2L, 3L),
                "New description", "new cover image");

        BookDto expected = new BookDto();
        expected.setTitle(requestDto.title());
        expected.setAuthor(requestDto.author());
        expected.setPrice(requestDto.price());
        expected.setIsbn(requestDto.isbn());
        expected.setDescription(requestDto.description());
        expected.setCoverImage(requestDto.coverImage());
        expected.setCategoryIds(requestDto.categoryIds());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //when
        MvcResult result = mockMvc.perform(post("/books")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        //then
    }
}
