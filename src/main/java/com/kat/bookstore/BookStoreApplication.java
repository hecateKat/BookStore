package com.kat.bookstore;

import com.kat.bookstore.entity.Book;
import com.kat.bookstore.service.BookService;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(BookService bookService) {
        return args -> {
            bookService.save(new Book(
                    "The Hobbit",
                    "J.R.R. Tolkien",
                    "978-0261103344",
                    new BigDecimal("15.99"),
                    "A fantasy novel.",
                    "hobbit.jpg"));
            bookService.save(new Book(
                    "1984",
                    "George Orwell",
                    "978-0451524935",
                    new BigDecimal("12.99"),
                    "Dystopian novel.",
                    "1984.jpg"));
            bookService.save(new Book(
                    "To Kill a Mockingbird",
                    "Harper Lee",
                    "978-0060935467",
                    new BigDecimal("10.99"),
                    "Classic literature.",
                    "mockingbird.jpg"));
            System.out.println("Sample books initialized.");
        };
    }
}
