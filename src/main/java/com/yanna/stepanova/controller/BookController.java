package com.yanna.stepanova.controller;

import com.yanna.stepanova.dto.book.BookDto;
import com.yanna.stepanova.dto.book.BookSearchParams;
import com.yanna.stepanova.dto.book.CreateBookRequestDto;
import com.yanna.stepanova.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book manager", description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
@Validated
public class BookController {
    private final BookService bookService;

    @PostMapping
    @Operation(summary = "Create a new book",
            description = "Create a new book entity in the database")
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by id",
            description = "Get a book entity by id from the database")
    public BookDto getBookById(@PathVariable @Positive Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/by-author")
    @Operation(summary = "Get all books by an author",
            description = "Get all books filtered by an author")
    public List<BookDto> getAllByAuthor(@RequestParam String author) {
        return bookService.getAllByAuthor(author);
    }

    @GetMapping
    @Operation(summary = "Get all books in parts",
            description = "Get all the books in parts + using sorting")
    public List<BookDto> getAll(@ParameterObject @PageableDefault Pageable pageable) {
        return bookService.getAll(pageable);
    }

    @GetMapping("/search")
    public List<BookDto> searchBooks(BookSearchParams searchParams) {
        return bookService.search(searchParams);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book by id",
            description = "Update a book with new data by id in the database")
    public BookDto updateBookById(@PathVariable @Positive Long id,
                                  @RequestBody @Valid CreateBookRequestDto newRequestDto) {
        return bookService.updateBook(id, newRequestDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book by id",
            description = "Delete a book by id (not physically - just mark it as deleted)")
    public String delete(@PathVariable @Positive Long id) {
        bookService.deleteById(id);
        return "The book entity was deleted by id: " + id;
    }
}
