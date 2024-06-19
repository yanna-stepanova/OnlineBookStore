package com.yanna.stepanova.repository.shoppingcart;

import com.yanna.stepanova.model.Book;
import com.yanna.stepanova.model.CartItem;
import com.yanna.stepanova.model.ShoppingCart;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CartItemRepositoryTest {
    @Autowired
    private CartItemRepository cartItemRepo;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource) throws SQLException {
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/cartItem/repository/add-required-data-to-tables.sql"));
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
                    new ClassPathResource("database/cartItem/repository/clear-all-dependent-tables.sql"));
        }
    }

    @Test
    @DisplayName("""
            Find all four cart_items by shopping_cart""")
    void findAllByShopCartId_FourCartItems_Ok() {
        Set<CartItem> actual = cartItemRepo.findAllByShopCartId(2L);
        Assertions.assertEquals(4, actual.size());
    }

    @Test
    @DisplayName("""
            Find all two cart_items by shopping_cart""")
    void findAllByShopCartId_TwoCartItems_Ok() {
        Set<CartItem> actual = cartItemRepo.findAllByShopCartId(1L);
        Assertions.assertEquals(2, actual.size());
    }

    @Test
    @DisplayName("""
            Find nothing by non-existing shopping_cart""")
    void findAllByShopCartId_ZeroCartItems_Ok() {
        Set<CartItem> actual = cartItemRepo.findAllByShopCartId(53L);
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    @DisplayName("""
            Find cart_item by its existing id and by existing shopping_cart id""")
    void findByIdAndShoppingCartId_BothValidId_Ok() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);

        CartItem expected = new CartItem();
        expected.setId(1L);
        expected.setShopcart(shoppingCart);
        expected.setBook(new Book(1L));
        expected.setQuantity(1);

        Optional<CartItem> actual = cartItemRepo.findByIdAndShoppingCartId(1L, 1L);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertTrue(
                EqualsBuilder.reflectionEquals(expected, actual.get(), List.of("shopcart", "book")));
        Assertions.assertEquals(expected.getShopcart().getId(), actual.get().getShopcart().getId());
        Assertions.assertEquals(expected.getBook().getId(), actual.get().getBook().getId());
    }

    @Test
    @DisplayName("""
            Find nothing by non-existing cart_item id and by non-existing shopping_cart id""")
    void findByIdAndShoppingCartId_NonExistingIds_NotOk() {
        Optional<CartItem> actual = cartItemRepo.findByIdAndShoppingCartId(100L, 100L);
        Assertions.assertFalse(actual.isPresent());
    }
}
