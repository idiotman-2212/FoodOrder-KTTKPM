package com.kttkpm.FoodOrder.Repository;

import com.kttkpm.FoodOrder.Entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Integer> {
    List<CartEntity> findByUserId(int userId);
    Optional<CartEntity> findByUserIdAndProductId(@Param("user_id") int userId, @Param("product_id") int productId);
}
