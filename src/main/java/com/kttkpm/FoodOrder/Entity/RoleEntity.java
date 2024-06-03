package com.kttkpm.FoodOrder.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    private Date createDate;

    @PrePersist
    protected void onCreate() {
        createDate = new Date();
    }

    @OneToMany(mappedBy = "role")
    private List<UserEntity> users;
}
