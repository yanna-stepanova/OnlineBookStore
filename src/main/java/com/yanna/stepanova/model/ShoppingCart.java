package com.yanna.stepanova.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@SQLDelete(sql = "UPDATE shopCarts SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Table(name = "shopCarts")
public class ShoppingCart {
    @Id
    private Long id;
    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToMany(mappedBy = "shopCart")
    /*@JoinTable(name = "shopCarts_cartItems",
                joinColumns = @JoinColumn(name = "shopCart_id"),
                inverseJoinColumns = @JoinColumn(name = "cartItem_id"))*/
    private Set<CartItem> cartItems = new HashSet<>();
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
}
