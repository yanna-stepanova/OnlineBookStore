package com.yanna.stepanova.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yanna.stepanova.dto.cartitem.CartItemDto;
import com.yanna.stepanova.dto.cartitem.CartItemQuantityRequestDto;
import com.yanna.stepanova.dto.cartitem.CreateCartItemRequestDto;
import com.yanna.stepanova.dto.shoppingcart.ShoppingCartDto;
import com.yanna.stepanova.model.Book;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerTest {
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
                    new ClassPathResource("database/cartItem/controller/initial-info-for-db.sql"));
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
                    new ClassPathResource("database/cartItem/controller/clean-db.sql"));
        }
    }

    @Test
    @DisplayName("Add a new cart item entity with some book to the shopping cart")
    @Sql(scripts = "classpath:database/cartItem/controller/remove-added-cart-item.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("alice@example.com")
    public void createCartItem_GivenExistingBookIdAndValidRequestDto_ReturnCartItemDto()
            throws Exception {
        //given
        Long bookId = 1L;
        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto(bookId, 1);

        Book book = new Book(1L);
        book.setTitle("Title of first test book");

        CartItemDto expected = new CartItemDto(1L, book.getId(), book.getTitle(),
                requestDto.quantity());

        //when
        MvcResult result = mockMvc.perform(post("/cart")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        //then
        CartItemDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CartItemDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.id());
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @Test
    @DisplayName("Get user's shopping cart with all cart items")
    @Sql(scripts = "classpath:database/cartItem/controller/add-cart-items.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/cartItem/controller/remove-added-cart-item.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("alice@example.com")
    public void getShopCart_WithExistingUser_ReturnShoppingCartDto() throws Exception {
        //given
        Long userId = 1L;
        ShoppingCartDto expected = new ShoppingCartDto(userId, 
                Set.of(new CartItemDto(1L, 1L, "Title of first test book", 17),
                       new CartItemDto(2L, 2L, "Title of second test book", 14)));

        //when
        MvcResult result = mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        //then
        ShoppingCartDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.userId(), actual.userId());
        Assertions.assertEquals(expected.cartItems().size(), actual.cartItems().size());
        Set<CartItemDto> cartItemDtoSet = expected.cartItems();
        for (CartItemDto fromExpected: cartItemDtoSet) {
            Optional<CartItemDto> fromActual = actual.cartItems().stream()
                    .filter(item -> item.id().equals(fromExpected.id()))
                    .findFirst();
            Assertions.assertTrue(fromActual.isPresent());
            Assertions.assertTrue(EqualsBuilder.reflectionEquals(fromExpected, fromActual.get()));
        }
    }

    @Test
    @DisplayName("Update books quantity of existing cart item")
    @Sql(scripts = "classpath:database/cartItem/controller/add-cart-items.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/cartItem/controller/remove-added-cart-item.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("alice@example.com")
    public void updateBookQuantity_GivenExistingCartItemIdAndRequestDto_ReturnCartItemDto()
            throws Exception {
        //given
        Long cartItemId = 2L;
        CartItemQuantityRequestDto requestDto = new CartItemQuantityRequestDto(100);
        CartItemDto expected = new CartItemDto(cartItemId, 2L,
                "Title of second test book", requestDto.quantity());

        //when
        MvcResult result = mockMvc.perform(put("/cart/cart-items/{id}", cartItemId)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        //then
        CartItemDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CartItemDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }
}
