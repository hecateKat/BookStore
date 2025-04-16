package com.kat.bookstore.repository.book;

import com.kat.bookstore.dto.BookSearchParametersDto;
import com.kat.bookstore.entity.Book;
import com.kat.bookstore.repository.SpecificationBuilder;
import com.kat.bookstore.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {

    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto parameters) {
        Specification<Book> spec = Specification.where(null);
        if (parameters != null && parameters.title().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider("title")
                    .getSpecification(parameters.title()));
        }
        if (parameters != null && parameters.author().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider("author")
                    .getSpecification(parameters.author()));
        }
        if (parameters != null && parameters.isbn().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider("isbn")
                    .getSpecification(parameters.isbn()));
        }
        return spec;
    }
}
