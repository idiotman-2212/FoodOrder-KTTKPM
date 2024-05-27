package com.kttkpm.FoodOrder.Repository;

import com.kttkpm.FoodOrder.Entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    List<ProductEntity> findByNameContainingIgnoreCase(String productName);

    @Query("SELECT p FROM ProductEntity p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "CAST(p.price AS string) LIKE CONCAT('%', :keyword, '%')")
    List<ProductEntity> searchProducts(@Param("keyword") String keyword);



    boolean existsByName(String name);

    List<ProductEntity> findAllByCategory_Id(int id);

    Page<ProductEntity> findByCategoryId(int categoryId, Pageable pageable);
}
