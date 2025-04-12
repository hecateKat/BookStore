package com.kat.bookstore.service.implementation;

import com.kat.bookstore.dto.BookDto;
import com.kat.bookstore.dto.CreateBookRequestDto;
import com.kat.bookstore.entity.Book;
import com.kat.bookstore.exception.EntityNotFoundException;
import com.kat.bookstore.mapper.BookMapper;
import com.kat.bookstore.repository.BookRepository;
import com.kat.bookstore.service.BookService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto createBookRequestDto) {
        Book book = bookMapper.mapToEntity(createBookRequestDto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.mapToDto(savedBook);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::mapToDto).collect(Collectors
                        .toList());
    }

    @Override
    public BookDto findById(Long id) {
        return bookRepository.findById(id).map(bookMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Book with that id not found" + id));
    }
}
