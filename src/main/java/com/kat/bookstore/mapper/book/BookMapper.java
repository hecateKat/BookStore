package com.kat.bookstore.mapper.book;

import com.kat.bookstore.config.MapperConfig;
import com.kat.bookstore.dto.book.BookDto;
import com.kat.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.kat.bookstore.dto.book.CreateBookRequestDto;
import com.kat.bookstore.entity.book.Book;
import com.kat.bookstore.entity.category.Category;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    Book mapToEntity(CreateBookRequestDto requestDto);

    BookDto mapToDto(Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    default Set<Category> mapCategoryIdsToCategorySet(Set<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return Set.of();
        }
        return categoryIds.stream()
                .map(Category::new)
                .collect(Collectors.toSet());
    }

    @AfterMapping
    default void setCategorySet(@MappingTarget Book book, CreateBookRequestDto requestDto) {
        book.setCategorySet(mapCategoryIdsToCategorySet(requestDto.categoryIds()));
    }

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoryIds(book.getCategorySet().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
    }
}
