package com.yanna.stepanova.repository.shoppingcart;

import com.yanna.stepanova.model.CartItem;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    @Query(value = "SELECT * FROM cart_items ci WHERE ci.shopcart_id = :shopcartId",
             nativeQuery = true)
    Set<CartItem> findAllByShopCartId(@Param("shopcartId") Long id);
}
