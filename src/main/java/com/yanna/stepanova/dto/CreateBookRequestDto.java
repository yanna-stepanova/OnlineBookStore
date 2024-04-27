package com.yanna.stepanova.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import org.hibernate.validator.constraints.ISBN;

public record CreateBookRequestDto(@NotBlank String title,
                                   @NotBlank String author,
                                   @ISBN String isbn,
                                   @NotNull @Positive BigDecimal price,
                                   String description, String coverImage) {}
