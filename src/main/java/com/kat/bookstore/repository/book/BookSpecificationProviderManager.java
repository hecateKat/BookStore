package com.kat.bookstore.repository.book;

import com.kat.bookstore.entity.book.Book;
import com.kat.bookstore.exception.SpecificationNotFoundException;
import com.kat.bookstore.repository.SpecificationProvider;
import com.kat.bookstore.repository.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {

    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        if (bookSpecificationProviders == null) {
            throw new RuntimeException("No specification providers available.");
        }
        return bookSpecificationProviders.stream()
                .filter(spec -> spec.getKey().equals(key))
                .findFirst()
                .orElseThrow(
                        () -> new SpecificationNotFoundException(
                                "No specification provider found for key: "
                                + key));
    }
}
