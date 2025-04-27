package com.kat.bookstore.repository.book.spec;

import com.kat.bookstore.entity.book.Book;
import com.kat.bookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "isbn";
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> {
            if (params != null && params.length > 0) {
                query.where(criteriaBuilder.equal(root.get("isbn"), params[0]));
            }
            return query.getRestriction();
        };
    }
}
