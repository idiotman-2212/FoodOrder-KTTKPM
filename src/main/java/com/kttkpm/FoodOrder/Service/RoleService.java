package com.kttkpm.FoodOrder.Service;

import com.kttkpm.FoodOrder.Entity.RoleEntity;
import com.kttkpm.FoodOrder.Payload.Response.RoleResponse;
import com.kttkpm.FoodOrder.Repository.RoleRepository;
import com.kttkpm.FoodOrder.Service.Imp.RoleServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService implements RoleServiceImp {

    @Autowired
    private RoleRepository roleRepository;
    @Override
            public List<RoleResponse> getAllRoles() {
                List<RoleEntity> list = roleRepository.findAll();
                List<RoleResponse> listResponse = new ArrayList<>();

                for (RoleEntity data: list) {
                    RoleResponse roleResponse = new RoleResponse();
                    roleResponse.setId(data.getId());
                    roleResponse.setName(data.getName());

            listResponse.add(roleResponse);
        }
        return listResponse;
    }

    @Override
    public RoleEntity getRoleById(Integer idRole) {
        return roleRepository.findById(idRole).orElse(null);
    }
}