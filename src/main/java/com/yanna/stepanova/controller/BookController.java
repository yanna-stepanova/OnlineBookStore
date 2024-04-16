package com.yanna.stepanova.controller;

import com.yanna.stepanova.dto.BookDto;
import com.yanna.stepanova.dto.CreateBookRequestDto;
import com.yanna.stepanova.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;

    @PostMapping
    public BookDto createBook(@RequestBody CreateBookRequestDto requestDto) {
        //System.out.println(requestDto);
        return bookService.save(requestDto);
    }

    /*@GetMapping
    public BookDto getBookById(Long id) {
        return null;
    }*/

    @GetMapping
    public List<BookDto> getAll() {
        return bookService.findAll();
    }
}
