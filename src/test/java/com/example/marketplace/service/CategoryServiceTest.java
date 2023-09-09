package com.example.marketplace.service;

import com.example.marketplace.dto.request.CategoryRequest;
import com.example.marketplace.exception.CategoryNotFoundException;
import com.example.marketplace.model.Category;
import com.example.marketplace.repository.CategoryRepository;
import com.example.marketplace.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    public void GetCategories_ReturnCategoryList() {
        Category category1 = Category.builder()
                .id(1L)
                .name("electronics")
                .build();
        Category category2 = Category.builder()
                .id(2L)
                .name("books")
                .build();

        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

        List<Category> categoryList = categoryService.getCategories();

        assertThat(categoryList).isNotNull();
        assertThat(categoryList).hasSize(2);
    }

    @Test
    public void CreateCategory_CategoryRequest_ReturnCategory() {
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("electronics");

        Category category = Category.builder()
                .id(1L)
                .name("electronics")
                .build();

        when(categoryRepository.existsByName(anyString())).thenReturn(Boolean.FALSE);
        when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(category);

        Category returnCategory = categoryService.createCategory(categoryRequest);

        verify(categoryRepository, times(1)).save(any(Category.class));
        assertThat(returnCategory).isNotNull();
    }

    @Test
    public void UpdateCategory_CategoryRequest_ReturnCategory() {
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("electronics");

        Category category = Category.builder()
                .id(1L)
                .name("electronics")
                .build();

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.existsByName(anyString())).thenReturn(Boolean.FALSE);
        when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(category);

        Category returnCategory = categoryService.updateCategory(category.getId(), categoryRequest);

        verify(categoryRepository, times(1)).save(any(Category.class));
        assertThat(returnCategory).isNotNull();
    }

    @Test
    public void DeleteCategory_ValidCategoryId_ReturnVoid() {
        Category category = Category.builder()
                .id(1L)
                .name("electronics")
                .build();

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        assertAll(() -> categoryService.deleteCategory(category.getId()));
    }

    @Test
    public void DeleteCategory_InvalidCategoryId_ThrowCategoryNotFoundException() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(2L));
    }
}
