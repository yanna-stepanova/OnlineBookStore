package com.yanna.stepanova.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@SQLDelete(sql = "UPDATE shopping_carts SET is_deleted = TRUE WHERE id = ?")
@SQLRestriction("is_deleted = FALSE")
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    private Long id;
    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private User user;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "shopcart")
    private Set<CartItem> cartItems = new HashSet<>();
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
}
