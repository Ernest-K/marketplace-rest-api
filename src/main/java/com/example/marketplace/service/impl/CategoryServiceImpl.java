package com.example.marketplace.service.impl;

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
}
