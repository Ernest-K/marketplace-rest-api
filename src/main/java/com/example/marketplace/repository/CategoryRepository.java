package com.example.marketplace.repository;

import com.example.marketplace.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById(Long id);

    Optional<Category> findByNameIgnoreCase(String name);

    Boolean existsByName(String name);
}
