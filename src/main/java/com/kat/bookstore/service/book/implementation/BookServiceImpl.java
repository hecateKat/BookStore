package com.kat.bookstore.service.book.implementation;

import com.kat.bookstore.dto.book.BookDto;
import com.kat.bookstore.dto.book.BookSearchParametersDto;
import com.kat.bookstore.dto.book.CreateBookRequestDto;
import com.kat.bookstore.entity.Book;
import com.kat.bookstore.exception.EntityNotFoundException;
import com.kat.bookstore.mapper.BookMapper;
import com.kat.bookstore.repository.book.BookRepository;
import com.kat.bookstore.repository.book.BookSpecificationBuilder;
import com.kat.bookstore.service.book.BookService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto createBookRequestDto) {
        Book book = bookMapper.mapToEntity(createBookRequestDto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.mapToDto(savedBook);
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::mapToDto).collect(Collectors
                        .toList());
    }

    @Override
    public BookDto findById(Long id) {
        return bookRepository.findById(id).map(bookMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Book with that id not found" + id));
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto createBookRequestDto) {
        Book oldBook = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with that id not found" + id));
        Book updatedBook = bookMapper.mapToEntity(createBookRequestDto);
        updatedBook.setId(oldBook.getId());
        Book savedBook = bookRepository.save(updatedBook);
        return bookMapper.mapToDto(savedBook);
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto searchParams) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParams);
        return bookRepository.findAll(bookSpecification).stream()
                .map(bookMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public void softDelete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with that id not found" + id));
        book.setDeleted(true);
        bookRepository.save(book);
    }
}
