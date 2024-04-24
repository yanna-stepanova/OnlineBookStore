package com.yanna.stepanova.repository.book.spec;

import com.yanna.stepanova.model.Book;
import com.yanna.stepanova.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "author";
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return ((root, query, criteriaBuilder) ->
                root.get(getKey()).in(Arrays.stream(params).toArray()));
    }
}
