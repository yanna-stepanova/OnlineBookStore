package com.yanna.stepanova.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateCartItemRequestDto(@NotNull @Positive Long bookId,
                                       @NotNull @Positive int quantity) {
}
