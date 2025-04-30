package com.kat.bookstore.service.book;

import com.kat.bookstore.dto.book.BookDto;
import com.kat.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.kat.bookstore.dto.book.BookSearchParametersDto;
import com.kat.bookstore.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto createBookRequestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    List<BookDto> search(BookSearchParametersDto searchParams);

    List<BookDtoWithoutCategoryIds> getAllByCategoryId(Long categoryId, Pageable pageable);

    void delete(Long id);

    void softDelete(Long id);
}
