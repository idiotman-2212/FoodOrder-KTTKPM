package com.kttkpm.FoodOrder.Service;

import com.kttkpm.FoodOrder.Entity.RoleEntity;
import com.kttkpm.FoodOrder.Entity.UserEntity;
import com.kttkpm.FoodOrder.Payload.Request.SignUpRequest;
import com.kttkpm.FoodOrder.Payload.Response.UserResponse;
import com.kttkpm.FoodOrder.Repository.RoleRepository;
import com.kttkpm.FoodOrder.Repository.UserRepository;
import com.kttkpm.FoodOrder.Service.Imp.UserServiceImp;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserServiceImp {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;


    @Override
    public boolean insertUser(SignUpRequest signUpRequest) {
        if (userRepository.findByEmail(signUpRequest.getEmail()) != null) {
            return false;
        }

        UserEntity user = new UserEntity();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setPhone(signUpRequest.getPhone());
        user.setAddress(signUpRequest.getAddress());

        RoleEntity role = roleRepository.findById(signUpRequest.getIdRole()).orElse(null);
        if (role == null) {
            return false; // or throw an exception
        }
        user.setRole(role);

        userRepository.save(user);
        return true;
    }


    @Override
    public List<UserEntity> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(UserEntity user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<UserEntity> getUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserEntity> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

}