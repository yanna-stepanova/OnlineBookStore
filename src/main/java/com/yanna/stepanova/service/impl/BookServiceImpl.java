package com.yanna.stepanova.service.impl;

import com.yanna.stepanova.dto.BookDto;
import com.yanna.stepanova.dto.CreateBookRequestDto;
import com.yanna.stepanova.exception.EntityNotFoundException;
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
    private final Random random = new Random();

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        book.setIsbn(generateUniqueIsbn());
        return bookMapper.toDto(bookRepo.save(book));
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't get book by id = " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> getAllByAuthor(String author) {
        List<Book> books = bookRepo.findAllByAuthorContainsIgnoreCase(author).orElseThrow(
                () -> new EntityNotFoundException("Can't get all books by author: " + author));
        return books.stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> getAll() {
        List<Book> books = bookRepo.findAll();
        return books.stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        bookRepo.deleteById(id);
    }

    private String generateUniqueIsbn() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 13; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }
}
