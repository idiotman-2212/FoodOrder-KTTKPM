package com.kttkpm.FoodOrder.Service;

import com.kttkpm.FoodOrder.Entity.RoleEntity;
import com.kttkpm.FoodOrder.Entity.UserEntity;
import com.kttkpm.FoodOrder.Payload.Request.SignUpRequest;
import com.kttkpm.FoodOrder.Payload.Response.UserResponse;
import com.kttkpm.FoodOrder.Repository.RoleRepository;
import com.kttkpm.FoodOrder.Repository.UserRepository;
import com.kttkpm.FoodOrder.Service.Imp.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserServiceImp {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
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
            return false;
        }

        user.setRole(role);
        userRepository.save(user);
        return true;
    }

    public boolean checkPassword(String email, String password) {
        UserEntity user = userRepository.findByEmail(email);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

    public boolean checkLogin(String email, String password){
        List<UserEntity> list = userRepository.findByEmailAndPassword(email,password);
        return list.size() > 0;
    }

    @Override
    public List<UserResponse> getAllUser() {
        List<UserEntity> list = userRepository.findAll();
        List<UserResponse> responseList = new ArrayList<>();

        for (UserEntity u : list) {
            UserResponse userResponse = new UserResponse();
            userResponse.setId(u.getId());
            userResponse.setUsername(u.getUsername());
            userResponse.setEmail(u.getEmail());
            userResponse.setCreateDate(u.getCreateDate());
            userResponse.setPassword(u.getPassword());
            userResponse.setPhone(u.getPhone());
            userResponse.setAddress(u.getAddress());

            RoleEntity roleEntity = u.getRole();
            userResponse.setRoles(roleEntity != null ? roleEntity.getName() : "No Role");

            responseList.add(userResponse);
        }

        return responseList;
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
    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<UserResponse> searchUsers(String keyword) {
        List<UserEntity> users = userRepository.searchUsers(keyword);
        List<UserResponse> list = new ArrayList<>();
        for (UserEntity u: users) {
            UserResponse userResponse = new UserResponse();
            userResponse.setId(u.getId());
            userResponse.setEmail(u.getEmail());
            userResponse.setPassword(u.getPassword());
            userResponse.setAddress(u.getAddress());
            userResponse.setPhone(u.getPhone());
            userResponse.setUsername(u.getUsername());

            RoleEntity roleEntity = u.getRole();
            userResponse.setRoles(roleEntity != null ? roleEntity.getName() : "No Role");

            list.add(userResponse);
        }
        return list;
    }
    @Override
    public Page<UserResponse> getAllUserPage(Integer pageNo) {
        int pageSize = 3; // Số sản phẩm trên mỗi trang
        List<UserResponse> allUser = getAllUser();
        // Phân trang dữ liệu
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allUser.size());

        List<UserResponse> sublist = allUser.subList(start, end);
        return new PageImpl<>(sublist, pageable, allUser.size());
    }
}