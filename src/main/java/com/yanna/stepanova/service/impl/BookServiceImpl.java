package com.yanna.stepanova.service.impl;

import com.yanna.stepanova.dto.BookDto;
import com.yanna.stepanova.dto.CreateBookRequestDto;
import com.yanna.stepanova.mapper.BookMapper;
import com.yanna.stepanova.model.Book;
import com.yanna.stepanova.repository.BookRepository;
import com.yanna.stepanova.service.BookService;
import java.util.List;
import java.util.Random;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepo;
    private final BookMapper bookMapper;
    private final Random randomIsbn = new Random(10_000_000_000_000L);

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        book.setIsbn(String.valueOf(randomIsbn.longs()));
        return bookMapper.toDto(bookRepo.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepo.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
