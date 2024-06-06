package com.kttkpm.FoodOrder.Payload.Response;

<<<<<<< HEAD
import lombok.Data;

import java.util.Date;
@Data
=======
import lombok.*;

import java.util.Date;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
>>>>>>> origin/tai-dev
public class ProductResponse {
    private int id;
    private String name;
    private String image;
    private Double price;
    private String description;
    private int quantity;
    private Date createDate;
    private CategoryResponse category;


<<<<<<< HEAD
    public ProductResponse(int id, String name, String image, Double price, String description, int quantity, Date createDate, CategoryResponse category) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.createDate = createDate;
        this.category = category;
    }


=======
>>>>>>> origin/tai-dev
}