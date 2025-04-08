package com.kat.bookstore.service;

import com.kat.bookstore.entity.Book;
import java.util.List;

public interface BookService {

    Book save(Book book);

    List<Book> findAll();
}
