package com.kat.bookstore.repository.book.spec;

import com.kat.bookstore.entity.Book;
import com.kat.bookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "title";
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> {
            if (params != null && params.length > 0) {
                query.where(criteriaBuilder.equal(root.get("title"), params[0]));
            }
            return query.getRestriction();
        };
    }
}
