package com.yanna.stepanova.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemQuantityRequestDto(
        @NotNull @Positive int quantity) {
}
