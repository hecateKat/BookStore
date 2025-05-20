package com.kat.bookstore.service.category;

import com.kat.bookstore.dto.category.CategoryDto;
import com.kat.bookstore.dto.category.CreateCategoryRequestDto;
import com.kat.bookstore.entity.category.Category;
import com.kat.bookstore.mapper.category.CategoryMapper;
import com.kat.bookstore.repository.category.CategoryRepository;
import com.kat.bookstore.service.category.implementation.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void test_should_return_true_when_findAll_CategoryDto_with_valid_Pageable() {
        //given
        Category category1 = new Category(1L);
        category1.setName("One");
        category1.setDescription("Description 1");

        Category category2 = new Category(2L);
        category2.setName("Two");
        category2.setDescription("Description 2");

        CategoryDto categoryDto1 = new CategoryDto(
                category1.getId(), category1.getName(), category1.getDescription());
        CategoryDto categoryDto2 = new CategoryDto(
                category2.getId(), category2.getName(), category2.getDescription());
        List<Category> categoryList = List.of(category1, category2);
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(categoryList, pageable, categoryList.size());
        Mockito.when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        Mockito.when(categoryMapper.toDto(category1)).thenReturn(categoryDto1);
        Mockito.when(categoryMapper.toDto(category2)).thenReturn(categoryDto2);

        //when
        List<CategoryDto> expected = List.of(categoryDto1, categoryDto2);
        List<CategoryDto> actual = categoryService.findAll(pageable);

        //then
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertTrue(
                    EqualsBuilder.reflectionEquals(expected.get(i), actual.get(i)));
        }
    }

    @Test
    void test_should_return_true_when_findById_CategoryDto_with_valid_Id() {
        //given
        Long categoryId = 1L;
        Category category = new Category(categoryId);
        category.setName("Category");
        category.setDescription("Description");

        CategoryDto expected = new CategoryDto(
                category.getId(), category.getName(), category.getDescription());
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Mockito.when(categoryMapper.toDto(category)).thenReturn(expected);
        //when
        CategoryDto actual = categoryService.findById(categoryId);
        //then
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    public void test_should_throw_exception_when_findById_gets_invalid_Id() {
        //given
        Long categoryId = 1L;
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        //then
        Assertions.assertThrows(
                RuntimeException.class,
                () -> categoryService.findById(categoryId),
                "Category with that id not found" + categoryId);
    }

    @Test
    void test_should_return_true_when_save_with_valid_RequestDto() {
        //given
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "New category", "New description");
        Category category = new Category();
        category.setName(requestDto.name());
        category.setDescription(requestDto.description());

        CategoryDto expected = new CategoryDto(4L, category.getName(),
                category.getDescription());
        Mockito.when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        Mockito.when(categoryRepository.save(category)).thenReturn(category);
        Mockito.when(categoryMapper.toDto(category)).thenReturn(expected);
        //when
        CategoryDto actual = categoryService.save(requestDto);
        //then
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    public void test_should_return_true_when_update_with_valid_Id() {
        // given
        Long categoryId = 4L;
        Category oldCategory = new Category(categoryId);
        oldCategory.setName("Category");
        oldCategory.setDescription("Description");

        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Updated category", "Updated description");
        Category updatedCategory = new Category(oldCategory.getId());
        updatedCategory.setName(requestDto.name());
        updatedCategory.setDescription(requestDto.description());

        CategoryDto expected = new CategoryDto(updatedCategory.getId(),
                updatedCategory.getName(), updatedCategory.getDescription());

        Mockito.lenient().when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(oldCategory));
        Mockito.lenient().when(categoryMapper.updateCategoryFromDto(oldCategory, requestDto))
                .thenReturn(updatedCategory);
        Mockito.lenient().when(categoryRepository.save(updatedCategory)).thenReturn(updatedCategory);
        Mockito.lenient().when(categoryMapper.toDto(updatedCategory)).thenReturn(expected);

        // when
        CategoryDto actual = categoryService.update(categoryId, requestDto);

        // then
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    public void test_should_not_update_category_with_invalid_Id_and_throw_Exception() {
        //given
        Long categoryId = 4L;
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Updated category", "Updated description");
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        //then
        Assertions.assertThrows(
                RuntimeException.class,
                () -> categoryService.update(categoryId, requestDto),
                "Category with that id not found" + categoryId);
    }

    @Test
    public void test_should_return_true_when_delete_with_valid_Id() {
        //given
        Long categoryId = 4L;
        Category category = new Category(categoryId);
        category.setName("Category");
        category.setDescription("Description");

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        //when
        categoryService.deleteById(categoryId);
        //then
        Mockito.verify(categoryRepository, Mockito.times(1)).save(category);
    }
}
