package com.kttkpm.FoodOrder.Service.Imp;

import com.kttkpm.FoodOrder.Payload.Response.CategoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryServiceImp {
    boolean insertCategory(String name);
    List<CategoryResponse> getAllCategory();
    CategoryResponse getCategoryById(int id);
    boolean deleteCategoryById(int id);
    boolean updateCategoryById(int id, String name);

    List<CategoryResponse> searchCategories(String query);

    Page<CategoryResponse> getAllCategoryPage(Integer pageNo);
}
