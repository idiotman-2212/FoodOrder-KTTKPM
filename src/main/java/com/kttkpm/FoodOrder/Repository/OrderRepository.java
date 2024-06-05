package com.kttkpm.FoodOrder.Repository;

import com.kttkpm.FoodOrder.Entity.OrderEntity;
import com.kttkpm.FoodOrder.Entity.ProductEntity;
import com.kttkpm.FoodOrder.Payload.Response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
    List<OrderEntity> findByUserId(int idUser);

    Page<OrderResponse> findByUserId(int idUser, Pageable pageable);

    @Query("SELECT o FROM OrderEntity o WHERE o.user.username LIKE %:keyword%")
    List<OrderEntity> searchUsers(@Param("keyword") String keyword);

    Optional<OrderEntity> findTopByUserIdOrderByOrderDateDesc(int userId);

}