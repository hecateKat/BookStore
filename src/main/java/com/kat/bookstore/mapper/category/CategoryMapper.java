package com.kat.bookstore.mapper.category;

import com.kat.bookstore.config.MapperConfig;
import com.kat.bookstore.dto.category.CategoryDto;
import com.kat.bookstore.dto.category.CreateCategoryRequestDto;
import com.kat.bookstore.entity.category.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequestDto requestDto);

    Category updateCategoryFromDto(@MappingTarget Category category,
                                   CreateCategoryRequestDto requestDto);
}
