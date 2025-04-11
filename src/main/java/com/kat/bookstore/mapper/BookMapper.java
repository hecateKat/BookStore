package com.kat.bookstore.mapper;

import com.kat.bookstore.config.MapperConfig;
import com.kat.bookstore.dto.BookDto;
import com.kat.bookstore.dto.CreateBookRequestDto;
import com.kat.bookstore.entity.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto mapToDto(Book book);

    Book mapToEntity(CreateBookRequestDto createBookRequestDto);
}
