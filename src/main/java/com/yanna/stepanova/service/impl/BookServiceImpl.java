package com.yanna.stepanova.service.impl;

import com.yanna.stepanova.dto.BookDto;
import com.yanna.stepanova.dto.CreateBookRequestDto;
import com.yanna.stepanova.exception.EntityNotFoundException;
import com.yanna.stepanova.mapper.BookMapper;
import com.yanna.stepanova.model.Book;
import com.yanna.stepanova.model.MyRandom;
import com.yanna.stepanova.repository.BookRepository;
import com.yanna.stepanova.service.BookService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepo;
    private final BookMapper bookMapper;
    private final MyRandom myRandom;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.generateUniqueIsbn(bookMapper.toModel(requestDto),
                myRandom.getRandom());
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
        return bookMapper.toDto(bookRepo.findAllByAuthorContainsIgnoreCase(author));
    }

    @Override
    public List<BookDto> getAll() {
        return bookMapper.toDto(bookRepo.findAll());
    }

    @Override
    public void deleteById(Long id) {
        bookRepo.deleteById(id);
    }

    @Override
    @Transactional
    public BookDto updateBookById(Long id, CreateBookRequestDto requestDto) {
        Book updatedBook = bookMapper.updateBookFromRequestDto(
                bookRepo.findById(id).orElseThrow(() ->
                        new EntityNotFoundException("Can't get book by id = " + id)), requestDto);
        return bookMapper.toDto(updatedBook);
    }

    /*private String generateUniqueIsbn() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 13; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }*/
}
