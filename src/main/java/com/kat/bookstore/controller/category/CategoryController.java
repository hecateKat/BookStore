package com.kat.bookstore.controller.category;

import com.kat.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.kat.bookstore.dto.category.CategoryDto;
import com.kat.bookstore.dto.category.CreateCategoryRequestDto;
import com.kat.bookstore.service.book.BookService;
import com.kat.bookstore.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category management", description = "Endpoints for managing categories")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/categories")
@Validated
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Find all categories",
            description = "Find all category entities in the database")
    public List<CategoryDto> getCategories(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a category by id",
            description = "Find a category entity by id from the database")
    public CategoryDto findById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @Operation(summary = "Save a category by id",
            description = "Save a category entity in the database")
    public CategoryDto save(
            @RequestBody @Valid CreateCategoryRequestDto createCategoryRequestDto) {
        return categoryService.save(createCategoryRequestDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update a category by id",
            description = "Update a category with new data by id in the database")
    public CategoryDto updateBookById(
            @PathVariable Long id,
            @RequestBody @Valid CreateCategoryRequestDto createCategoryRequestDto
    ) {
        return categoryService.update(id, createCategoryRequestDto);
    }

    @GetMapping("/{id}/books")
    @Operation(summary = "Retrieve all books by a specific category",
            description = "Retrieve all entity books  by a specific category from the database")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(
            @PathVariable @Positive Long id,
            @ParameterObject Pageable pageable) {
        return bookService.getAllByCategoryId(id, pageable);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category by id",
            description = "Delete a category by id (not physically - just mark it as deleted)")
    public void deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteById(id);
    }
}
