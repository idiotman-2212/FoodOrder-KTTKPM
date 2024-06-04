package com.kttkpm.FoodOrder.Service.Imp;

import com.kttkpm.FoodOrder.Entity.UserEntity;
import com.kttkpm.FoodOrder.Payload.Request.SignUpRequest;
import com.kttkpm.FoodOrder.Payload.Response.ProductResponse;
import com.kttkpm.FoodOrder.Payload.Response.UserResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserServiceImp {
    boolean insertUser(SignUpRequest signUpRequest);
    List<UserResponse> getAllUser();
    void updateUser(UserEntity user);
    void deleteUserById(int id);
    Optional<UserEntity> getUserById(int id);
    UserEntity findUserByEmail(String email);
    UserEntity findUserByUsername(String username);
    List<UserResponse> searchUsers(String keyword);

    Page<UserResponse> getAllUserPage(Integer pageNo);
}