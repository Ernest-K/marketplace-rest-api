package com.example.marketplace.service;

import com.example.marketplace.dto.request.CategoryRequest;
import com.example.marketplace.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getCategories();

    Category createCategory(CategoryRequest categoryRequest);

    Category updateCategory(Long categoryId, CategoryRequest categoryRequest);

    void deleteCategory(Long categoryId);
}
