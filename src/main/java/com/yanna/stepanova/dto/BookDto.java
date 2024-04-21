package com.yanna.stepanova.dto;

import java.math.BigDecimal;

public record BookDto(Long id, String title, String author, BigDecimal price,
                     String description, String coverImage) {}
