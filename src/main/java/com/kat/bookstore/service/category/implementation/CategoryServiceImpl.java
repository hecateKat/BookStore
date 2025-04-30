package com.kat.bookstore.service.category.implementation;

import com.kat.bookstore.dto.category.CategoryDto;
import com.kat.bookstore.dto.category.CreateCategoryRequestDto;
import com.kat.bookstore.entity.category.Category;
import com.kat.bookstore.exception.EntityNotFoundException;
import com.kat.bookstore.mapper.category.CategoryMapper;
import com.kat.bookstore.repository.category.CategoryRepository;
import com.kat.bookstore.service.category.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        Page<Category> page = categoryRepository.findAll(pageable);
        return page.stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto findById(Long id) {
        return categoryRepository.findById(id).map(categoryMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Category with that id not found" + id));
    }

    @Override
    public CategoryDto save(CreateCategoryRequestDto createCategoryRequestDto) {
        Category category = categoryMapper.toEntity(createCategoryRequestDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryDto update(
            Long id,
            CreateCategoryRequestDto createCategoryRequestDto
    ) {
        Category oldCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Category with that id not found" + id));
        Category updatedCategory = categoryMapper
                .updateCategoryFromDto(oldCategory, createCategoryRequestDto);
        updatedCategory.setId(oldCategory.getId());
        Category savedCategory = categoryRepository.save(updatedCategory);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public void deleteById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Category with that id not found" + id));
        category.setDeleted(true);
        categoryRepository.save(category);
    }
}
