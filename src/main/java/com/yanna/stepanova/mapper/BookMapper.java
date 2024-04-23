package com.yanna.stepanova.mapper;

import com.yanna.stepanova.config.MapperConfig;
import com.yanna.stepanova.dto.BookDto;
import com.yanna.stepanova.dto.CreateBookRequestDto;
import com.yanna.stepanova.model.Book;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    Book toModel(CreateBookRequestDto requestDto);

    Book updateBookFromDto(@MappingTarget Book book, CreateBookRequestDto requestDto);

    BookDto toDto(Book book);

    List<BookDto> toDto(List<Book> books);
}
