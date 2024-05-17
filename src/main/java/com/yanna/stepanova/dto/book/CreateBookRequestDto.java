package com.yanna.stepanova.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Set;
import org.hibernate.validator.constraints.ISBN;

public record CreateBookRequestDto(@NotBlank String title,
                                   @NotBlank String author,
                                   @ISBN String isbn,
                                   @NotNull @Positive BigDecimal price,
                                   Set<Long> categoryIds,
                                   String description, String coverImage) {}
