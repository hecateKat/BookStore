package com.kat.bookstore.service.book;

import com.kat.bookstore.dto.book.BookDto;
import com.kat.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.kat.bookstore.dto.book.CreateBookRequestDto;
import com.kat.bookstore.entity.book.Book;
import com.kat.bookstore.entity.category.Category;
import com.kat.bookstore.exception.EntityNotFoundException;
import com.kat.bookstore.mapper.book.BookMapper;
import com.kat.bookstore.repository.book.BookRepository;
import com.kat.bookstore.service.book.implementation.BookServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepo;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void test_should_return_true_when_find_Book_by_Id_with_valid_Id_and_return_BookDto() {
        //given
        Long bookId = 1L;
        Book book = new Book(bookId);
        book.setTitle("test book");
        book.setAuthor("test author");
        book.setPrice(BigDecimal.valueOf(1.99));
        book.setIsbn("000-00-00000000");
        book.setDescription("test description");
        book.setCoverImage("test image");
        book.setCategorySet(Set.of(new Category(1L), new Category(11L)));
        Mockito.when(bookRepo.findById(bookId)).thenReturn(Optional.of(book));

        BookDto expected = new BookDto();
        expected.setId(book.getId());
        expected.setTitle(book.getTitle());
        expected.setAuthor(book.getAuthor());
        expected.setPrice(book.getPrice());
        expected.setIsbn(book.getIsbn());
        expected.setDescription(book.getDescription());
        expected.setCoverImage(book.getCoverImage());
        expected.setCategoryIds(book.getCategorySet().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
        Mockito.when(bookMapper.mapToDto(book)).thenReturn(expected);

        //when
        BookDto actual = bookService.findById(bookId);

        //then
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    public void test_should_throw_exception_when_findById_gets_invalid_Id() {
        //given
        Long bookId = -1L;
        Mockito.when(bookRepo.findById(bookId)).thenReturn(Optional.empty());

        //when
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(bookId));

        //then
        String expected = String.format("Book with that id not found" + bookId);
        String actual = exception.getMessage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_should_return_true_when_findAll_BookDto_with_valid_Pageable() {
        //given
        Book book1 = new Book(1L);
        book1.setTitle("test book");
        book1.setAuthor("test author");
        book1.setPrice(BigDecimal.valueOf(1.11));
        book1.setIsbn("111-00-00000000");
        book1.setDescription("test description");
        book1.setCoverImage("test image");
        book1.setCategorySet(Set.of(new Category(1L)));

        Book book2 = new Book(2L);
        book2.setTitle("test book 2");
        book2.setAuthor("test author 2");
        book2.setPrice(BigDecimal.valueOf(2.22));
        book2.setIsbn("222-00-00000000");
        book2.setCategorySet(Set.of());

        BookDto bookDto1 = new BookDto();
        bookDto1.setId(book1.getId());
        bookDto1.setTitle(book1.getTitle());
        bookDto1.setAuthor(book1.getAuthor());
        bookDto1.setPrice(book1.getPrice());
        bookDto1.setIsbn(book1.getIsbn());
        bookDto1.setDescription(book1.getDescription());
        bookDto1.setCoverImage(book1.getCoverImage());
        bookDto1.setCategoryIds(book1.getCategorySet().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
        BookDto bookDto2 = new BookDto();
        bookDto2.setId(book2.getId());
        bookDto2.setTitle(book2.getTitle());
        bookDto2.setAuthor(book2.getAuthor());
        bookDto2.setPrice(book2.getPrice());
        bookDto2.setIsbn(book2.getIsbn());
        bookDto2.setDescription(book2.getDescription());
        bookDto2.setCoverImage(book2.getCoverImage());
        bookDto2.setCategoryIds(book2.getCategorySet().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));

        List<Book> bookList = List.of(book1, book2);
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(bookList, pageable, bookList.size());
        Mockito.when(bookRepo.findAll(pageable)).thenReturn(bookPage);
        Mockito.when(bookMapper.mapToDto(book1)).thenReturn(bookDto1);
        Mockito.when(bookMapper.mapToDto(book2)).thenReturn(bookDto2);

        //when
        List<BookDto> expected = List.of(bookDto1, bookDto2);
        List<BookDto> actual = bookService.findAll(pageable);

        //then
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertTrue(
                    EqualsBuilder.reflectionEquals(expected.get(i), actual.get(i)));
        }
    }

    @Test
    public void test_should_return_true_when_getAllByCategoryId_with_valid_CategoryId() {
        //given
        Long categoryId = 1L;
        Book book1 = new Book(1L);
        book1.setTitle("test title");
        book1.setAuthor("test author");
        book1.setPrice(BigDecimal.valueOf(1.11));
        book1.setIsbn("111-00-00000000");
        book1.setDescription("test description");
        book1.setCoverImage("test image");
        book1.setCategorySet(Set.of(new Category(categoryId)));

        Book book2 = new Book(2L);
        book2.setTitle("test title 2");
        book2.setAuthor("test author 2");
        book2.setPrice(BigDecimal.valueOf(2.22));
        book2.setIsbn("222-00-00000000");
        book2.setCategorySet(Set.of(new Category(categoryId), new Category(2L)));

        BookDtoWithoutCategoryIds bookDtoWithout1 = new BookDtoWithoutCategoryIds(book1.getId(),
                book1.getTitle(), book1.getAuthor(), book1.getPrice(), book1.getIsbn(),
                book1.getDescription(), book1.getCoverImage());
        BookDtoWithoutCategoryIds bookDtoWithout2 = new BookDtoWithoutCategoryIds(book2.getId(),
                book2.getTitle(), book2.getAuthor(), book2.getPrice(), book2.getIsbn(),
                book2.getDescription(), book2.getCoverImage());

        PageRequest pageable = PageRequest.of(0, 10);
        Page<Book> bookList = new PageImpl<>(List.of(book1, book2), pageable, 2);
        Mockito.when(bookRepo.findAllByCategorySet_Id(categoryId, pageable)).thenReturn(bookList);
        Mockito.when(bookMapper.toDtoWithoutCategories(book1)).thenReturn(bookDtoWithout1);
        Mockito.when(bookMapper.toDtoWithoutCategories(book2)).thenReturn(bookDtoWithout2);

        //when
        List<BookDtoWithoutCategoryIds> expected = List.of(bookDtoWithout1, bookDtoWithout2);
        List<BookDtoWithoutCategoryIds> actual = bookService.getAllByCategoryId(categoryId,
                pageable);

        //then
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertTrue(
                    EqualsBuilder.reflectionEquals(expected.get(i), actual.get(i)));
        }
    }

    @Test
    public void test_should_return_true_when_save_with_valid_RequestDto_and_return_BookDto() {
        //given
        CreateBookRequestDto requestDto = new CreateBookRequestDto("test title 1", "author 1",
                "000-00-00000000", BigDecimal.valueOf(1.11), Set.of(1L, 2L, 3L),
                "description", "cover image");
        Book book = new Book();
        book.setTitle(requestDto.title());
        book.setAuthor(requestDto.author());
        book.setIsbn(requestDto.isbn());
        book.setPrice(requestDto.price());
        book.setDescription(requestDto.description());
        book.setCoverImage(requestDto.coverImage());
        book.setCategorySet(requestDto.categoryIds().stream()
                .map(Category::new)
                .collect(Collectors.toSet()));
        BookDto expected = new BookDto();
        expected.setId(2L);
        expected.setTitle(book.getTitle());
        expected.setAuthor(book.getAuthor());
        expected.setPrice(book.getPrice());
        expected.setIsbn(book.getIsbn());
        expected.setDescription(book.getDescription());
        expected.setCoverImage(book.getCoverImage());
        expected.setCategoryIds(book.getCategorySet().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));

        Mockito.when(bookMapper.mapToEntity(requestDto)).thenReturn(book);
        Mockito.when(bookRepo.save(book)).thenReturn(book);
        Mockito.when(bookMapper.mapToDto(book)).thenReturn(expected);

        //when
        BookDto actual = bookService.save(requestDto);

        //then
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    public void test_should_not_update_Book_with_invalid_Id_and_throw_Exception() {
        //given
        Long bookId = 10L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto("Update title", "Update author",
                "000-00-12345678", BigDecimal.valueOf(9.99), Set.of(1L, 2L),
                "Update description", "Update cover image");
        Mockito.when(bookRepo.findById(bookId)).thenReturn(Optional.empty());

        //when
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookService.update(bookId, requestDto));

        //then
        String expected = "Book with that id not found" + bookId;
        String actual = exception.getMessage();
        Assertions.assertEquals(expected, actual);
    }
}
