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
    @PutMapping("/categories/{id}")
    ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryRequest categoryRequest){
        return new ResponseEntity<>(categoryService.updateCategory(id, categoryRequest), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/categories/{id}")
    ResponseEntity<String> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return new ResponseEntity<>("Category id: " + id + " deleted successfully", HttpStatus.OK);
    }
}
