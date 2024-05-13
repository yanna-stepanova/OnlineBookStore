package com.yanna.stepanova.service;

import com.yanna.stepanova.dto.book.BookDto;
import com.yanna.stepanova.dto.book.BookSearchParams;
import com.yanna.stepanova.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto getBookById(Long id);

    List<BookDto> getAllByAuthor(String author);

    List<BookDto> getAll(Pageable pageable);

    void deleteById(Long id);

    BookDto updateBook(Long id, CreateBookRequestDto requestDto);

    List<BookDto> search(BookSearchParams params);
}
