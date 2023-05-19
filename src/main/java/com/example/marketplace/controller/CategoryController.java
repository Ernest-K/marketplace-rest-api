package com.example.marketplace.controller;

import com.example.marketplace.model.Category;
import com.example.marketplace.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    ResponseEntity<List<Category>> getAllCategories(){
        return new ResponseEntity<>(categoryService.getCategories(), HttpStatus.OK);
    }
}
