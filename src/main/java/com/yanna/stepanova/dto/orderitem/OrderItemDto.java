package com.yanna.stepanova.dto.orderitem;

import java.math.BigDecimal;

public record OrderItemDto(Long id,
                           Long bookId,
                           String bookTitle,
                           int quantity,
                           BigDecimal price) {
}
