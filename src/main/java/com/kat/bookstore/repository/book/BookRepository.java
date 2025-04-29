package com.kat.bookstore.repository.book;

import com.kat.bookstore.entity.book.Book;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query(value = "SELECT * FROM books b INNER JOIN books_categories bc ON "
            + "b.id = bc.book_id WHERE bc.category_id = :categoryId", nativeQuery = true)
    List<Book> findAllByCategorySet_Id(@Param("categoryId") Long categoryId, Pageable pageable);
}
