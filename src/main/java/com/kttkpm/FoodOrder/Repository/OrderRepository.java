package com.kttkpm.FoodOrder.Repository;

import com.kttkpm.FoodOrder.Entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
    List<OrderEntity> findByUserId(int idUser);

    @Query("SELECT o FROM OrderEntity o WHERE o.user.username LIKE %:keyword%")
    List<OrderEntity> searchUsers(@Param("keyword") String keyword);

}