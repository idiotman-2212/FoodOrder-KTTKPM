package com.kttkpm.FoodOrder.Repository;

import com.kttkpm.FoodOrder.Entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {

    boolean existsByName(String name);
    @Query("SELECT c FROM CategoryEntity c WHERE c.name LIKE %:query%")
    List<CategoryEntity> searchCategories(@Param("query") String query);

    List<CategoryEntity> findTop5ByOrderByCreateDateDesc();

    CategoryEntity findByName(String categoryName);
}
