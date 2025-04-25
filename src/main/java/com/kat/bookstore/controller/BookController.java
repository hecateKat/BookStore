package com.kat.bookstore.controller;

import com.kat.bookstore.dto.book.BookDto;
import com.kat.bookstore.dto.book.BookSearchParametersDto;
import com.kat.bookstore.dto.book.CreateBookRequestDto;
import com.kat.bookstore.service.book.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book Store", description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Find all books",
            description = "Find all book entities in the database")
    public List<BookDto> getBooks(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a book by id",
            description = "Find a book entity by id from the database")
    public BookDto findById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @Operation(summary = "Save a book by id",
            description = "Save a book entity in the database")
    public BookDto save(@RequestBody @Valid CreateBookRequestDto createBookRequestDto) {
        return bookService.save(createBookRequestDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book by id",
            description = "Update a book with new data by id in the database")
    public BookDto updateBookById(@PathVariable Long id,
                                  @RequestBody @Valid CreateBookRequestDto createBookRequestDto) {
        return bookService.update(id, createBookRequestDto);
    }

    @GetMapping("/search")
    @Operation(summary = "Find a book by parameter (title, author, isbn)",
            description = "Find a book entity by parameter (title, author, isbn) from the database")
    public List<BookDto> searchBooks(BookSearchParametersDto searchParams) {
        return bookService.search(searchParams);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book by id",
            description = "Delete a book by id (not physically - just mark it as deleted)")
    public void deleteBookById(@PathVariable Long id) {
        bookService.softDelete(id);
    }
}
