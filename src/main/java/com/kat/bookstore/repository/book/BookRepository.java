package com.kat.bookstore.repository.book;

import com.kat.bookstore.entity.book.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @EntityGraph(value = "Book.categorySet")
    Page<Book> findAll(Pageable pageable);

    @EntityGraph(value = "Book.categorySet")
    List<Book> findAll(Specification<Book> specification);

    @EntityGraph(value = "Book.categorySet")
    Optional<Book> findById(Long id);

    Page<Book> findAllByCategorySet_Id(@Param("categoryId") Long categoryId, Pageable pageable);
}
