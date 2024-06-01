package com.kttkpm.FoodOrder.Payload.Request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductRequest {
    @NotNull(message = "File không được phép rỗng")
    private MultipartFile file;

    @NotNull(message = "Tên không được phép rỗng")
    private String name;
    private String image;
    @DecimalMin(value = "0.1")
    private double price;
    private String description;
    private String categoryName;
    private int quantity;
    private Date createDate;


}
