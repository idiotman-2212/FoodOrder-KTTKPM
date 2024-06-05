package com.kttkpm.FoodOrder.Repository;

import com.kttkpm.FoodOrder.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    List<UserEntity> findByEmailAndPassword(String email, String password);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    @Query("SELECT u FROM UserEntity u WHERE u.username LIKE %:keyword% OR u.email LIKE %:keyword%")
    List<UserEntity> searchUsers(@Param("keyword") String keyword);
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
    UserEntity findByResetToken(String resetToken);

}