package com.yanna.stepanova.dto.book;

import java.math.BigDecimal;

public record BookDtoWithoutCategoryIds(Long id, String title, String author, BigDecimal price,
                                        String isbn, String description, String coverImage) {
}
