package com.yanna.stepanova.service;

import com.yanna.stepanova.dto.category.CategoryDto;
import com.yanna.stepanova.dto.category.CreateCategoryRequestDto;
import com.yanna.stepanova.mapper.CategoryMapper;
import com.yanna.stepanova.model.Category;
import com.yanna.stepanova.repository.category.CategoryRepository;
import com.yanna.stepanova.service.impl.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
    private CategoryRepository categoryRepo;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("""
            Get correct CategoryDto for valid requestDto""")
    void save_WithValidRequestDto_ReturnCategoryDto() {
        //given
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "New category", "Description of category");
        Category category = new Category();
        category.setName(requestDto.name());
        category.setDescription(requestDto.description());

        CategoryDto expected = new CategoryDto(4L, category.getName(),
                category.getDescription());
        Mockito.when(categoryMapper.toModel(requestDto)).thenReturn(category);
        Mockito.when(categoryRepo.save(category)).thenReturn(category);
        Mockito.when(categoryMapper.toDto(category)).thenReturn(expected);
        //when
        CategoryDto actual = categoryService.save(requestDto);
        //then
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @DisplayName("""
            Get correct CategoryDto for existing category""")
    void getCategoryById_WithValidId_ReturnCategoryDto() {
        //given
        Long categoryId = 1L;
        Category category = new Category(categoryId);
        category.setName("Some category");
        category.setDescription("Some description");

        CategoryDto expected = new CategoryDto(category.getId(),
                category.getName(), category.getDescription());
        Mockito.when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));
        Mockito.when(categoryMapper.toDto(category)).thenReturn(expected);
        //when
        CategoryDto actual = categoryService.getCategoryById(categoryId);
        //then
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @DisplayName("""
            Get a list of all CategoryDto""")
    void getAllWithValidPageable_ReturnAllCategoryDto() {
        //given
        Category category1 = new Category(1L);
        category1.setName("Category one");
        category1.setDescription("Description 1");

        Category category2 = new Category(2L);
        category2.setName("Category two");
        category2.setDescription("Description 2");

        CategoryDto categoryDto1 = new CategoryDto(
                category1.getId(), category1.getName(), category1.getDescription());
        CategoryDto categoryDto2 = new CategoryDto(
                category2.getId(), category2.getName(), category2.getDescription());
        List<Category> categoryList = List.of(category1, category2);
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(categoryList, pageable, categoryList.size());
        Mockito.when(categoryRepo.findAll(pageable)).thenReturn(categoryPage);
        Mockito.when(categoryMapper.toDto(category1)).thenReturn(categoryDto1);
        Mockito.when(categoryMapper.toDto(category2)).thenReturn(categoryDto2);
        //when
        List<CategoryDto> expected = List.of(categoryDto1, categoryDto2);
        List<CategoryDto> actual = categoryService.getAll(pageable);
        //then
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertTrue(
                    EqualsBuilder.reflectionEquals(expected.get(i), actual.get(i)));
        }
    }

    @Test
    @DisplayName("""
            Get updated CategoryDto by valid id""")
    void updateCategory_WithValidIdAndRequestDto_ReturnCategoryDto() {
        //given
        Long categoryId = 4L;
        Category oldCategory = new Category(categoryId);
        oldCategory.setName("Some category");
        oldCategory.setDescription("Some description");

        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Updated category", "Description of updated category");
        Category updatedCategory = new Category(oldCategory.getId());
        updatedCategory.setName(requestDto.name());
        updatedCategory.setDescription(requestDto.description());

        CategoryDto expected = new CategoryDto(updatedCategory.getId(),
                updatedCategory.getName(), updatedCategory.getDescription());

        Mockito.when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(oldCategory));
        Mockito.when(categoryMapper.updateCategoryFromDto(oldCategory, requestDto))
                .thenReturn(updatedCategory);
        Mockito.when(categoryMapper.toDto(updatedCategory)).thenReturn(expected);
        //when
        CategoryDto actual = categoryService.updateCategory(categoryId, requestDto);
        //then
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }
}
