package com.kat.bookstore.mapper.cartitem;

import com.kat.bookstore.config.MapperConfig;
import com.kat.bookstore.dto.cartitem.CartItemDto;
import com.kat.bookstore.dto.cartitem.CreateCartItemRequestDto;
import com.kat.bookstore.entity.book.Book;
import com.kat.bookstore.entity.cartitem.CartItem;
import com.kat.bookstore.mapper.book.BookMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {

    @Mapping(target = "book", source = "bookId")
    CartItem toEntity(CreateCartItemRequestDto createCartItemRequestDto);

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemDto toDto(CartItem cartItem);

    default Book map(Long bookId) {
        if (bookId == null) {
            return null;
        }
        Book book = new Book();
        book.setId(bookId);
        return book;
    }
}
