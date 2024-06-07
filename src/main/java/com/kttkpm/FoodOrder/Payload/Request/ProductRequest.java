package com.kttkpm.FoodOrder.Payload.Request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private int id; // Thêm trường id
    @NotNull(message = "File không được phép rỗng")
    private MultipartFile file;

    @NotNull(message = "Tên không được phép rỗng")
    private String name;
    private String image;
    @DecimalMin(value = "0.1")
    private double price;
    private String description;
    private String categoryName;
    private int categoryId;
    private int quantity;
}
