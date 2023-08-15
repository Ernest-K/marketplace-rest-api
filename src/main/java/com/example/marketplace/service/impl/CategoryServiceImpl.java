package com.example.marketplace.service.impl;

import com.example.marketplace.dto.CategoryRequest;
import com.example.marketplace.exception.CategoryExistsException;
import com.example.marketplace.model.Category;
import com.example.marketplace.repository.CategoryRepository;
import com.example.marketplace.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category createCategory(CategoryRequest categoryRequest){
        if(categoryRepository.existsByName(categoryRequest.getName())){
            throw new CategoryExistsException("Category with name: " + categoryRequest.getName() + " already exists");
        }
        return categoryRepository.save(new Category(categoryRequest.getName()));
    }
}
