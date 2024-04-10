package com.yanna.stepanova;

import com.yanna.stepanova.config.AppConfig;
import com.yanna.stepanova.model.Book;
import com.yanna.stepanova.service.BookService;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class OnlineBookStoreApplication {
	private static final AtomicLong idCounter = new AtomicLong();

	public static void main(String[] args) {
		Book bookOne = new Book("One book", "First Author",
				"isbn-" + idCounter.addAndGet(1), BigDecimal.valueOf(299));
		Book bookTwo = new Book("Two book", "First Author",
				"isbn-" + idCounter.addAndGet(1), BigDecimal.valueOf(350));

		AnnotationConfigApplicationContext context =
			new AnnotationConfigApplicationContext(AppConfig.class);
		BookService bookService = context.getBean(BookService.class);
		bookService.save(bookOne);
		bookService.save(bookTwo);
		System.out.println(bookService.findAll());
	}
}
