package com.yanna.stepanova.repository;

import com.yanna.stepanova.dto.BookSearchParams;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParams searchParametersDto);
}
