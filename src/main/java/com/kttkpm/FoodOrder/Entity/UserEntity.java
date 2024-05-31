package com.kttkpm.FoodOrder.Entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
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

    public UserEntity() {
    }

    public UserEntity(int id, String username, String password, String email, String phone, String address, Date createDate, RoleEntity role, List<CartEntity> carts, List<FavouriteEntity> favourites) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.createDate = createDate;
        this.role = role;
        this.carts = carts;
        this.favourites = favourites;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public RoleEntity getRole() {
        return role;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
    }

    public List<CartEntity> getCarts() {
        return carts;
    }

    public void setCarts(List<CartEntity> carts) {
        this.carts = carts;
    }

    public List<FavouriteEntity> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<FavouriteEntity> favourites) {
        this.favourites = favourites;
    }
}
