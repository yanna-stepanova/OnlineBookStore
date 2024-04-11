package com.yanna.stepanova;

import com.yanna.stepanova.model.Book;
import com.yanna.stepanova.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OnlineBookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book bookOne = new Book();
            bookOne.setTitle("One book");
            bookOne.setAuthor("First Author");
            bookOne.setPrice(BigDecimal.valueOf(299));

            Book bookTwo = new Book();
            bookTwo.setTitle("Two book");
            bookTwo.setAuthor("First Author");
            bookTwo.setPrice(BigDecimal.valueOf(350));
            bookService.save(bookOne);
            bookService.save(bookTwo);
            System.out.println(bookService.findAll());
        };
    }
}
