package com.kat.bookstore.repository;

import com.kat.bookstore.entity.Book;
import java.util.List;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();
}
