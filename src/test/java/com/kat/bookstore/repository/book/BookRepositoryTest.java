package com.kat.bookstore.repository.book;

import com.kat.bookstore.entity.book.Book;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @SneakyThrows
    public static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(
                            "book/repository/remove-all-books-and-categories.sql"));
        }
    }

    @BeforeAll
    public static void beforeAll(@Autowired DataSource dataSource) throws SQLException {
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("book/repository/add-default-book.sql"));
        }
    }

    @AfterAll
    public static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @Test
    @Sql(scripts = "classpath:book/repository/add-four-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:book/repository/remove-four-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void test_should_return_true_when_findAll() {
        //given
        Page<Book> actual = bookRepository.findAll(PageRequest.of(0, 10));

        //then
        Assertions.assertEquals(5, actual.getTotalElements());
    }

    @Test
    public void test_should_return_true_when_findById() {
        Assertions.assertTrue(bookRepository.findById(1L).isPresent());
    }

    @Test
    public void test_should_return_false_when_findById_nonexisting_Book() {
        Assertions.assertFalse(bookRepository.findById(2L).isPresent());
    }


    @Test
    @Sql(scripts = {"classpath:book/repository/add-four-books.sql",
            "classpath:book/repository/add-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:"
            + "book/repository/remove-all-categories.sql",
            "classpath:book/repository/remove-four-books.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void test_should_return_true_when_findAllByCategorySet_Id() {
        //given
        Page<Book> expected = bookRepository.findAllByCategorySet_Id(2L, PageRequest.of(0, 10));

        //then
        Assertions.assertEquals(4, expected.toSet().size());
    }

    @Test
    public void test_should_return_true_when_findAllByCategorySet_Id_expects_empty() {
        //given
        Page<Book> expected = bookRepository.findAllByCategorySet_Id(999L, PageRequest.of(0, 10));

        //then
        Assertions.assertTrue(expected.isEmpty());
    }

}
