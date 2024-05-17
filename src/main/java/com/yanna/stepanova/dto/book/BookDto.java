package com.yanna.stepanova.dto.book;

import java.math.BigDecimal;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private BigDecimal price;
    private String isbn;
    private Set<Long> categoryIds;
    private String description;
    private String coverImage;
}
