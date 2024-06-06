package com.kttkpm.FoodOrder.Payload.Request;

<<<<<<< HEAD
import lombok.Data;

@Data
public class CartRequest {
    private int userId;
    private int quantity;
    private int productId;

=======
import com.kttkpm.FoodOrder.Entity.OrderEntity;
import com.kttkpm.FoodOrder.Entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartRequest {
    private int userId;
    private int id;
>>>>>>> origin/tai-dev
}