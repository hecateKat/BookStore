package com.kat.bookstore.repository;

import com.kat.bookstore.dto.BookSearchParametersDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParametersDto parameters);
}
