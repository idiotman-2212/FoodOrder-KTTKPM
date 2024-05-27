package com.kttkpm.FoodOrder.Repository;

import com.kttkpm.FoodOrder.Entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Integer> {
}
