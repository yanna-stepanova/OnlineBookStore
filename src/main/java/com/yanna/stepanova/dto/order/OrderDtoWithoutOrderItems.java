package com.yanna.stepanova.dto.order;

import com.yanna.stepanova.model.Status;
import java.math.BigDecimal;

public record OrderDtoWithoutOrderItems(Long id,
                                        Long userId,
                                        String orderDate,
                                        BigDecimal total,
                                        Status status) {
}
