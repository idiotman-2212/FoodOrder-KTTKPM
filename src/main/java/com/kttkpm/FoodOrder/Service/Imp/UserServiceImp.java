package com.kttkpm.FoodOrder.Service.Imp;

import com.kttkpm.FoodOrder.Entity.UserEntity;
import com.kttkpm.FoodOrder.Payload.Request.SignUpRequest;
import com.kttkpm.FoodOrder.Payload.Response.ProductResponse;
import com.kttkpm.FoodOrder.Payload.Response.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserServiceImp {
    boolean insertUser(SignUpRequest signUpRequest);
    List<UserResponse> getAllUser();
    void updateUser(UserEntity user);
    void deleteUserById(int id);
    Optional<UserEntity> getUserById(int id);
    Optional<UserEntity> getUserByEmail(String email);
    List<UserResponse> searchUsers(String keyword);
}