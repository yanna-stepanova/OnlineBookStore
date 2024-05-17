package com.yanna.stepanova.mapper;

import com.yanna.stepanova.config.MapperConfig;
import com.yanna.stepanova.dto.category.CategoryDto;
import com.yanna.stepanova.dto.category.CreateCategoryRequestDto;
import com.yanna.stepanova.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toModel(CreateCategoryRequestDto requestDto);

    Category updateCategoryFromDto(@MappingTarget Category category,
                                   CreateCategoryRequestDto requestDto);
}
