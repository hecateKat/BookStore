package com.kat.bookstore.dto.book;

import java.math.BigDecimal;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private BigDecimal price;
    private String isbn;
    private Set<Long> categoryIds;
    private String description;
    private String coverImage;

}
