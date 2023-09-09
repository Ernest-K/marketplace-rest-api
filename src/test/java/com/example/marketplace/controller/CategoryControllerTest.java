package com.example.marketplace.controller;

import com.example.marketplace.dto.request.CategoryRequest;
import com.example.marketplace.model.Category;
import com.example.marketplace.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(controllers = CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void GetAllCategories_ReturnOk() throws Exception {
        Category category = Category.builder().id(1L).name("electronics").build();
        List<Category> categoryList = List.of(category);

        when(categoryService.getCategories()).thenReturn(categoryList);

        ResultActions response = mockMvc.perform(get("/api/categories")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void CreateCategory_CategoryRequest_ReturnCreated() throws Exception {
        CategoryRequest categoryRequest = CategoryRequest.builder().name("electronics").build();
        Category createdCategory = Category.builder().id(1L).name(categoryRequest.getName()).build();
        when(categoryService.createCategory(Mockito.any(CategoryRequest.class))).thenReturn(createdCategory);

        ResultActions response = mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest))
                .accept(MediaType.APPLICATION_JSON));


        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void UpdateCategory_CategoryIdAndCategoryRequest_ReturnOk() throws Exception {
        CategoryRequest categoryRequest = CategoryRequest.builder().name("electronics").build();
        Category createdCategory = Category.builder().id(1L).name(categoryRequest.getName()).build();
        when(categoryService.updateCategory(anyLong(), Mockito.any(CategoryRequest.class))).thenReturn(createdCategory);

        ResultActions response = mockMvc.perform(put("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest)));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void DeleteCategory_CategoryId_ReturnOk() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        ResultActions response = mockMvc.perform(delete("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
