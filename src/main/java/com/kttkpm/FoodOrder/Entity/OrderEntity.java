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
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date orderDate;
    private String orderDesc;
    private Double orderFee;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private OrderStatus orderStatus;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_order",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<ProductEntity> products;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private CartEntity cart;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<PaymentEntity> payments;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
