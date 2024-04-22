package com.yanna.stepanova.mapper;

import com.yanna.stepanova.config.MapperConfig;
import com.yanna.stepanova.dto.BookDto;
import com.yanna.stepanova.dto.CreateBookRequestDto;
import com.yanna.stepanova.model.Book;
import java.util.List;
import java.util.Random;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    default Book generateUniqueIsbn(Book book, Random random) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 13; i++) {
            result.append(random.nextInt(10));
        }
        book.setIsbn(result.toString());
        return book;
    }

    Book toModel(CreateBookRequestDto requestDto);

    Book updateBookFromRequestDto(@MappingTarget Book book, CreateBookRequestDto requestDto);

    BookDto toDto(Book book);

    List<BookDto> toDto(List<Book> books);
}
