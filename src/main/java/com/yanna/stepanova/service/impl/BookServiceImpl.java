package com.yanna.stepanova.service.impl;

import com.yanna.stepanova.dto.book.BookDto;
import com.yanna.stepanova.dto.book.BookDtoWithoutCategoryIds;
import com.yanna.stepanova.dto.book.BookSearchParams;
import com.yanna.stepanova.dto.book.CreateBookRequestDto;
import com.yanna.stepanova.exception.EntityNotFoundException;
import com.yanna.stepanova.mapper.BookMapper;
import com.yanna.stepanova.model.Book;
import com.yanna.stepanova.repository.book.BookRepository;
import com.yanna.stepanova.repository.book.BookSpecificationBuilder;
import com.yanna.stepanova.service.BookService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepo;
    private final BookMapper bookMapper;
    private final Random myRandom;
    private final BookSpecificationBuilder bookSpecBuilder;
    //private final CategoryRepository categoryRepo;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        if (book.getIsbn() == null || book.getIsbn().isBlank()) {
            book.setIsbn(generateUniqueIsbn());
        }
        bookMapper.setCategories(book,requestDto);
        return bookMapper.toDto(bookRepo.save(book));
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookRepo.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Book with id: %s not found", id)));
    }

    @Override
    public List<BookDto> getAllByAuthor(String author) {
        return bookRepo.findAllByAuthorContainsIgnoreCase(author).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> getAll(Pageable pageable) {
        return bookRepo.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        bookRepo.deleteById(id);
    }

    @Override
    @Transactional
    public BookDto updateBook(Long id, CreateBookRequestDto requestDto) {
        String isbn = null;
        Book oldBook = bookRepo.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't get book by id = " + id));
        if (requestDto.isbn() == null || requestDto.isbn().isBlank()) {
            isbn = oldBook.getIsbn();
        }
        Book updatedBook = bookMapper.updateBookFromDto(oldBook, requestDto);
        if (updatedBook.getIsbn() == null || updatedBook.getIsbn().isBlank()) {
            updatedBook.setIsbn(isbn);
        }
        return bookMapper.toDto(updatedBook);
    }

    @Override
    public List<BookDto> search(BookSearchParams params) {
        return bookRepo.findAll(bookSpecBuilder.build(params)).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getAllByCategoryId(Long categoryId, Pageable pageable) {
        return bookRepo.findAllByCategorySet_Id(categoryId, pageable).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }

    private String generateUniqueIsbn() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 13; i++) {
            result.append(myRandom.nextInt(10));
        }
        return result.toString();
    }
}
