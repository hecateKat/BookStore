package com.kat.bookstore.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Set;

public record CreateBookRequestDto(@NotBlank String title,
                                   @NotBlank String author,
                                   String isbn,
                                   @NotNull @Positive BigDecimal price,
                                   Set<Long> categoryIds,
                                   String description, String coverImage)
{}
