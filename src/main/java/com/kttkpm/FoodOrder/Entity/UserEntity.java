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
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
    private Date createDate;

    @PrePersist
    protected void onCreate() {
        createDate = new Date();
    }

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;

   @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
   private List<CartEntity> carts;

   @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
   private List<FavouriteEntity> favourites;

    @OneToMany(mappedBy = "user")
    private List<OrderEntity> orders;
}
