package com.example.marketplace.repository;

import com.example.marketplace.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void Save_ValidCategory_ReturnSavedCategory() {
        Category category = Category.builder().name("electronics").build();

        Category savedCategory = categoryRepository.save(category);

        assertThat(savedCategory.getId()).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo("electronics");
        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void FindAll_ReturnCategoryList(){
        Category category1 = Category.builder().name("electronics").build();
        Category category2 = Category.builder().name("books").build();

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        List<Category> categoryList = categoryRepository.findAll();

        assertThat(categoryList).hasSize(2).contains(category1, category2);
    }

    @Test
    public void FindById_ValidId_ReturnCategory() {
        Category category = Category.builder().name("electronics").build();
        Category savedCategory = categoryRepository.save(category);

        Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getId());

        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get()).isEqualTo(category);
    }

    @Test
    public void FindById_InvalidId_ReturnEmpty() {
        Optional<Category> foundCategory = categoryRepository.findById(1L);

        assertThat(foundCategory).isEmpty();
    }

    @Test
    public void FindByNameIgnoreCase_ValidName_ReturnCategory() {
        Category category = Category.builder().name("electronics").build();
        categoryRepository.save(category);

        Optional<Category> foundCategory = categoryRepository.findByNameIgnoreCase("ElEcTrOnIcS");

        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo("electronics");
    }

    @Test
    public void FindByNameIgnoreCase_InvalidName_ReturnEmpty() {
        Category category = Category.builder().name("electronics").build();
        categoryRepository.save(category);

        Optional<Category> foundCategory = categoryRepository.findByNameIgnoreCase("books");

        assertThat(foundCategory).isEmpty();
    }

    @Test
    public void ExistsByName_ValidName_ReturnTrue() {
        Category category = Category.builder().name("electronics").build();
        categoryRepository.save(category);

        Boolean exists = categoryRepository.existsByName("electronics");

        assertThat(exists).isTrue();
    }

    @Test
    public void ExistsByName_InvalidName_ReturnFalse() {
        Category category = Category.builder().name("electronics").build();
        categoryRepository.save(category);

        Boolean exists = categoryRepository.existsByName("books");

        assertThat(exists).isFalse();
    }

    @Test
    public void UpdateCategory_ReturnUpdatedCategory(){
        Category category = Category.builder().name("electronics").build();
        Category savedCategory = categoryRepository.save(category);

        Category categoryToUpdate = categoryRepository.findById(savedCategory.getId()).get();

        categoryToUpdate.setName("books");

        Category updatedCategory = categoryRepository.save(categoryToUpdate);

        assertThat(updatedCategory.getId()).isNotNull();
        assertThat(updatedCategory.getId()).isEqualTo(savedCategory.getId());
        assertThat(updatedCategory.getName()).isEqualTo("books");
    }

    @Test
    public void DeleteById_ValidId_ReturnEmpty(){
        Category category = Category.builder().name("electronics").build();
        Category savedCategory = categoryRepository.save(category);

        categoryRepository.deleteById(savedCategory.getId());

        Optional<Category> deletedCategory = categoryRepository.findById(savedCategory.getId());

        assertThat(deletedCategory).isEmpty();
    }
}
