package com.yanna.stepanova.controller;

import com.yanna.stepanova.dto.book.BookDtoWithoutCategoryIds;
import com.yanna.stepanova.dto.category.CategoryDto;
import com.yanna.stepanova.dto.category.CreateCategoryRequestDto;
import com.yanna.stepanova.service.BookService;
import com.yanna.stepanova.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category manager", description = "Endpoints for managing categories")
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
@Validated
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @Operation(summary = "Create a new category of book",
            description = "Create a new category entity in the database")
    public CategoryDto createCategory(@RequestBody @Valid CreateCategoryRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a category by id",
            description = "Retrieve a category entity by id from the database")
    public CategoryDto getCategoryById(@PathVariable @Positive Long id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/{id}/books")
    @Operation(summary = "Retrieve all books by a specific category",
            description = "Retrieve all entity books  by a specific category from the database")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable @Positive Long id) {
        return bookService.getAllByCategoryId(id);
    }

    @GetMapping()
    @Operation(summary = "Retrieve all categories",
            description = "Retrieve all entity categories from the database")
    public List<CategoryDto> getAll(@ParameterObject @PageableDefault Pageable pageable) {
        return categoryService.getAll(pageable);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update the details of a category by id",
            description = "Update a category entity with new information by id")
    public CategoryDto updateCategoryById(@PathVariable @Positive Long id,
                                      @RequestBody @Valid CreateCategoryRequestDto newRequestDto) {
        return categoryService.updateCategory(id, newRequestDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category by id",
            description = "Delete a category by id (using 'soft deleted')")
    public String deleteCategoryById(@PathVariable @Positive Long id) {
        categoryService.deleteById(id);
        return "The category entity was deleted by id: " + id;
    }
}
