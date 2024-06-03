package com.kttkpm.FoodOrder.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String image;
    private Double price;
    private String description;
    private int quantity;
    private Date createDate;

    @PrePersist
    protected void onCreate() {
        createDate = new Date();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<FavouriteEntity> favourite;

    @ManyToMany(mappedBy = "products", cascade = CascadeType.ALL)
    private List<OrderEntity> orders;

    @OneToMany(mappedBy = "product")
    private List<CartEntity> carts;
}
