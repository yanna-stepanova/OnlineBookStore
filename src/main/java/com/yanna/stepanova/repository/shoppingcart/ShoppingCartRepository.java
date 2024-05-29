package com.yanna.stepanova.repository.shoppingcart;

import com.yanna.stepanova.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
}
