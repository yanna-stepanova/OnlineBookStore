package com.yanna.stepanova.service;

import com.yanna.stepanova.dto.BookDto;
import com.yanna.stepanova.dto.BookSearchParams;
import com.yanna.stepanova.dto.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto getBookById(Long id);

    List<BookDto> getAllByAuthor(String author);

    List<BookDto> getAll(Pageable pageable);

    void deleteById(Long id);

    BookDto updateBook(Long id, CreateBookRequestDto requestDto);

    List<BookDto> search(BookSearchParams params);
}
