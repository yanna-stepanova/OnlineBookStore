package com.yanna.stepanova.repository.book;

import com.yanna.stepanova.dto.book.BookSearchParams;
import com.yanna.stepanova.model.Book;
import com.yanna.stepanova.repository.SpecificationBuilder;
import com.yanna.stepanova.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> bookSpecPM;

    @Override
    public Specification<Book> build(BookSearchParams params) {
        Specification<Book> spec = Specification.where(null);
        if (params.titles() != null && params.titles().length > 0) {
            spec = spec.and(bookSpecPM.getSpecificationProvider("title")
                    .getSpecification(params.titles()));
        }
        if (params.authors() != null && params.authors().length > 0) {
            spec = spec.and(bookSpecPM.getSpecificationProvider("author")
                    .getSpecification(params.authors()));
        }
        return spec;
    }
}
