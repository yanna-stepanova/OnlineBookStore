package com.yanna.stepanova.controller;

import com.yanna.stepanova.dto.BookDto;
import com.yanna.stepanova.dto.CreateBookRequestDto;
import com.yanna.stepanova.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @PostMapping
    public BookDto createBook(@RequestBody CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/by-author")
    public List<BookDto> getAllByAuthor(@RequestParam String author) {
        return bookService.getAllByAuthor(author);
    }

    @GetMapping
    public List<BookDto> getAll() {
        return bookService.getAll();
    }
}
