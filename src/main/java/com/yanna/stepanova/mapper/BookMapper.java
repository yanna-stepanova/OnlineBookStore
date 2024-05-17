package com.yanna.stepanova.mapper;

import com.yanna.stepanova.config.MapperConfig;
import com.yanna.stepanova.dto.book.BookDto;
import com.yanna.stepanova.dto.book.BookDtoWithoutCategoryIds;
import com.yanna.stepanova.dto.book.CreateBookRequestDto;
import com.yanna.stepanova.model.Book;
import com.yanna.stepanova.model.Category;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categorySet", ignore = true)
    Book toModel(CreateBookRequestDto requestDto);

    Book updateBookFromDto(@MappingTarget Book book, CreateBookRequestDto requestDto);

    @Mapping(target = "categoryIds", ignore = true)
    BookDto toDto(Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategories(@MappingTarget Book book, CreateBookRequestDto requestDto) {
        if (requestDto.categoryIds() == null) {
            book.setCategorySet(Set.of());
            return;
        }
        book.setCategorySet(requestDto.categoryIds().stream()
                .map(Category::new)
                .collect(Collectors.toSet()));
    }

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoryIds(book.getCategorySet().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
    }
}
