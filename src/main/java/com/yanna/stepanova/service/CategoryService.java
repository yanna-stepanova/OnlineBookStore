package com.yanna.stepanova.service;

import com.yanna.stepanova.dto.category.CategoryDto;
import com.yanna.stepanova.dto.category.CreateCategoryRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDto save(CreateCategoryRequestDto requestDto);

    CategoryDto getCategoryById(Long id);

    List<CategoryDto> getAll(Pageable pageable);

    void deleteById(Long id);

    CategoryDto updateCategory(Long id, CreateCategoryRequestDto requestDto);
}
