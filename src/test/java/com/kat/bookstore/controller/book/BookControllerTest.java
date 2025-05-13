package com.kat.bookstore.controller.book;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kat.bookstore.dto.book.BookDto;
import com.kat.bookstore.dto.book.CreateBookRequestDto;
import jakarta.servlet.ServletException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Arrays;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {

    static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    public static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("book/controller/"
                            + "remove-all.sql"));
        }
    }

    @BeforeAll
    public static void beforeAll(@Autowired DataSource dataSource,
                                 @Autowired WebApplicationContext appContext) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(appContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("book/controller/"
                            + "add-three-books-and-categories.sql"));
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN, ROLE"})
    public void test_should_return_true_when_getAll() throws Exception {
        //given
        List<BookDto> expected = getAllBooksDto();

        //when
        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        //then
        BookDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto[].class);
        Assertions.assertEquals(expected.size(), actual.length);
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected.get(i), actual[i]));
        }
    }

    @AfterAll
    public static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN, ROLE"})
    public void test_should_return_true_when_use_getBookById_with_valid_Id() throws Exception {
        //given
        Long bookId = 1L;

        //when
        MvcResult result = mockMvc.perform(get("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //then
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        BookDto expected = getAllBooksDto().get(0);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN, ROLE"})
    public void test_should_throw_Exception_when_using_getBookById_with_nonexisting_Id() throws Exception {
        //given
        Long bookId = 400L;

        //when
        try {
            MvcResult result = mockMvc.perform(get("/books/{id}", bookId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();
            BookDto actual = objectMapper.readValue(
                    result.getResponse().getContentAsString(), BookDto.class);

            //then
            Assertions.assertNull(actual, String.format("Id=%s should be non-existing", bookId));
        } catch (ServletException exception) {
            //then
            Assertions.assertEquals(String.format("Book with that id not found" + bookId),
                    exception.getRootCause().getMessage());
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN, ROLE"})
    public void test_should_return_true_when_saveBook_with_valid_requestDto() throws Exception {
        //given
        CreateBookRequestDto requestDto = new CreateBookRequestDto("New Book", "New Author",
                "123-456-789-0", BigDecimal.valueOf(99.99), Set.of(),
                "New description", "new cover image");
        BookDto expected = new BookDto(null, requestDto.title(), requestDto.author(),
                requestDto.price(), requestDto.isbn(), requestDto.categoryIds(),
                requestDto.description(), requestDto.coverImage());

        //when
        MvcResult result = mockMvc.perform(put("/books")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        //then
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN, ROLE"})
    public void test_should_return_true_when_search_with_valid_parameters() throws Exception {
        // given
        String title = "First";
        String author = "Author 1";
        String isbn = "000-00-00000001";

        List<BookDto> expected = getAllBooksDto().stream()
                .filter(book -> (title == null || book.getTitle().contains(title)) &&
                        (author == null || book.getAuthor().contains(author)) &&
                        (isbn == null || book.getIsbn().equals(isbn)))
                .toList();

        // when
        MvcResult result = mockMvc.perform(get("/books/search")
                        .param("title", title)
                        .param("author", author)
                        .param("isbn", isbn)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // then
        BookDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto[].class);

        System.out.println("Expected: " + expected);
        System.out.println("Actual: " + Arrays.toString(actual));

        Assertions.assertEquals(expected.size(), actual.length);
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertEquals(expected.get(i).getTitle(), actual[i].getTitle());
            Assertions.assertEquals(expected.get(i).getAuthor(), actual[i].getAuthor());
            Assertions.assertEquals(expected.get(i).getIsbn(), actual[i].getIsbn());
        }
    }

    @Test
    @Sql(scripts = "classpath:book/controller/add-book-for-update.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:book/controller/remove-updated-book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void test_should_return_true_when_updateBookById_with_valid_id_and_requestDto() throws Exception {
        //given
        Long bookId = 4L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto("Update title", "Update author",
                "654-123-456-789-0", BigDecimal.valueOf(99.99), Set.of(),
                "Update description", "update cover image");
        BookDto expected = new BookDto(bookId, requestDto.title(), requestDto.author(),
                requestDto.price(), requestDto.isbn(), requestDto.categoryIds(),
                requestDto.description(), requestDto.coverImage());

        //when
        MvcResult result = mockMvc.perform(put("/books/{id}", bookId)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //then
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @Sql(scripts = "classpath:book/controller/add-book-to-delete.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void test_should_return_true_when_delete() throws Exception {
        //given
        Long bookId = 4L;

        //when & then
        mockMvc.perform(delete("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(result -> Assertions.assertEquals(
                        "The book entity was deleted by id: " + bookId,
                        result.getResponse().getContentAsString()));
    }

    private List<BookDto> getAllBooksDto() {
        List<BookDto> bookDtoList = new ArrayList<>();
        bookDtoList.add(new BookDto(1L, "First", "Author 1",
                BigDecimal.valueOf(1.23), "000-00-00000001", Set.of(1L),
                "Book 1", "cover_1.jpg"));
        bookDtoList.add(new BookDto(2L, "Second", "Author 2",
                BigDecimal.valueOf(2.34), "000-00-00000002", Set.of(2L),
                "Book 2", "cover_2.jpg"));
        bookDtoList.add(new BookDto(3L, "Third", "Author 3",
                BigDecimal.valueOf(3.45), "000-00-00000003", Set.of(3L),
                "Book 3", "cover_3.jpg"));
        return bookDtoList;
    }
}