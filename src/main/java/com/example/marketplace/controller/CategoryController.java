package com.example.marketplace.controller;

import com.example.marketplace.dto.request.CategoryRequest;
import com.example.marketplace.model.Category;
import com.example.marketplace.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    ResponseEntity<List<Category>> getAllCategories(){
        return new ResponseEntity<>(categoryService.getCategories(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/categories")
    ResponseEntity<Category> createCategory(@RequestBody @Valid CategoryRequest categoryRequest){
        return new ResponseEntity<>(categoryService.createCategory(categoryRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/categories/{categoryId}")
    ResponseEntity<Category> updateCategory(@PathVariable Long categoryId, @RequestBody @Valid CategoryRequest categoryRequest){
        return new ResponseEntity<>(categoryService.updateCategory(categoryId, categoryRequest), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/categories/{categoryId}")
    ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>("Category id: " + categoryId + " deleted successfully", HttpStatus.OK);
    }
}
