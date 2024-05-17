package com.yanna.stepanova.service.impl;

import com.yanna.stepanova.dto.category.CategoryDto;
import com.yanna.stepanova.dto.category.CreateCategoryRequestDto;
import com.yanna.stepanova.mapper.CategoryMapper;
import com.yanna.stepanova.model.Category;
import com.yanna.stepanova.repository.category.CategoryRepository;
import com.yanna.stepanova.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepo;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto save(CreateCategoryRequestDto requestDto) {
        return categoryMapper.toDto(categoryRepo.save(categoryMapper.toModel(requestDto)));
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        return categoryRepo.findById(id)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Category with id '%s' wasn't found", id)));
    }

    @Override
    public List<CategoryDto> getAll(Pageable pageable) {
        return categoryRepo.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        categoryRepo.deleteById(id);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long id, CreateCategoryRequestDto requestDto) {
        Category oldCategory = categoryRepo.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't get category by id = " + id));
        return categoryMapper.toDto(
                categoryMapper.updateCategoryFromDto(oldCategory, requestDto));
    }
}
