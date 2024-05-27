package com.kttkpm.FoodOrder.Helper;

import com.kttkpm.FoodOrder.Entity.CategoryEntity;
import com.kttkpm.FoodOrder.Payload.Response.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter {
    public static CategoryResponse toCategoryResponse(CategoryEntity categoryEntity) {
        if (categoryEntity == null) {
            return null;
        }
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(categoryEntity.getId());
        categoryResponse.setName(categoryEntity.getName());
        categoryResponse.setCreateDate(categoryEntity.getCreateDate());
        return categoryResponse;
    }

    public static CategoryEntity toCategoryEntity(CategoryResponse categoryResponse) {
        if (categoryResponse == null) {
            return null;
        }
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(categoryResponse.getId());
        categoryEntity.setName(categoryResponse.getName());
        categoryEntity.setCreateDate(categoryResponse.getCreateDate());
        return categoryEntity;
    }
}
