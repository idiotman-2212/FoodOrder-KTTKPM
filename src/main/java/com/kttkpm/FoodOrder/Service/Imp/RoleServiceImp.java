package com.kttkpm.FoodOrder.Service.Imp;

import com.kttkpm.FoodOrder.Entity.RoleEntity;
import com.kttkpm.FoodOrder.Payload.Response.RoleResponse;

import java.util.List;

public interface RoleServiceImp {
    List<RoleResponse> getAllRoles();

    RoleEntity getRoleById(Integer idRole);
}