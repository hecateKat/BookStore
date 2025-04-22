package com.kat.bookstore.repository.book.spec;

import com.kat.bookstore.entity.Book;
import com.kat.bookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "author";
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> {
            if (params != null && params.length > 0 && params[0] != null) {
                String pattern = "%" + params[0] + "%";
                query.where(criteriaBuilder.like(root.get("author"), pattern));
            }
            return query.getRestriction();
        };
    }
}
