package com.kttkpm.FoodOrder.Service;

import com.kttkpm.FoodOrder.Entity.CategoryEntity;
import com.kttkpm.FoodOrder.Helper.CategoryConverter;
import com.kttkpm.FoodOrder.Payload.Response.CategoryResponse;
import com.kttkpm.FoodOrder.Payload.Response.ProductResponse;
import com.kttkpm.FoodOrder.Repository.CategoryRepository;
import com.kttkpm.FoodOrder.Service.Imp.CategoryServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService implements CategoryServiceImp {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryConverter categoryConverter;
    @Override
    public boolean insertCategory(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new IllegalStateException("Category with this name already exists");
        }
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(name);
        categoryEntity.setCreateDate(new Date());
        categoryRepository.save(categoryEntity);
        return true;
    }

    @Override
    public List<CategoryResponse> getAllCategory() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryConverter::toCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getCategoryById(int id) {
        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(id);
        if(categoryEntityOptional.isPresent()){
            return CategoryConverter.toCategoryResponse(categoryEntityOptional.get());
        }else {
            return null;
        }
    }

    @Override
    public boolean deleteCategoryById(int id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateCategoryById(int id, String name) {
        Optional<CategoryEntity> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isPresent()) {
            CategoryEntity categoryEntity = optionalCategory.get();
            categoryEntity.setName(name);
            categoryEntity.setCreateDate(new Date());
            categoryRepository.save(categoryEntity);
            return true;
        }
        return false;
    }
    @Override
    public List<CategoryResponse> searchCategories(String query) {
        List<CategoryEntity> list = categoryRepository.searchCategories(query);
        List<CategoryResponse> responseList = new ArrayList<>();

        for (CategoryEntity c: list) {
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setId(c.getId());
            categoryResponse.setName(c.getName());
            categoryResponse.setCreateDate(c.getCreateDate());

            responseList.add(categoryResponse);
        }
        return responseList;
    }

    public Page<CategoryResponse> getAllCategoryPage(Integer pageNo) {
        int pageSize = 5; // Số sản phẩm trên mỗi trang
        List<CategoryResponse> allCategory = getAllCategory();
        // Phân trang dữ liệu
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allCategory.size());

        List<CategoryResponse> sublist = allCategory.subList(start, end);
        return new PageImpl<>(sublist, pageable, allCategory.size());
    }
}
