package com.kat.bookstore.service;

import com.kat.bookstore.dto.BookDto;
import com.kat.bookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto createBookRequestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);
}
