package com.kttkpm.FoodOrder.Helper;

import com.kttkpm.FoodOrder.Entity.ProductEntity;
import com.kttkpm.FoodOrder.Payload.Response.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {
    // Đối tượng instance duy nhất của ProductConverter
    private static final ProductConverter instance = new ProductConverter();

    // Constructor private để ngăn việc tạo instance từ bên ngoài lớp
    private ProductConverter() {
        // Khởi tạo ProductConverter
    }

    // Phương thức để lấy instance duy nhất của ProductConverter
    public static ProductConverter getInstance() {
        return instance;
    }

    public ProductResponse toProductResponse(ProductEntity productEntity) {
        if (productEntity == null) {
            return null;
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(productEntity.getId());
        productResponse.setName(productEntity.getName());
        productResponse.setImage(productEntity.getImage());
        productResponse.setPrice(productEntity.getPrice());
        productResponse.setDescription(productEntity.getDescription());
        productResponse.setQuantity(productEntity.getQuantity());
        productResponse.setCreateDate(productEntity.getCreateDate());

        productResponse.setCategory(CategoryConverter.toCategoryResponse(productEntity.getCategory()));

        return productResponse;
    }

    public ProductEntity toProductEntity(ProductResponse productResponse) {
        if (productResponse == null) {
            return null;
        }
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(productResponse.getId());
        productEntity.setName(productResponse.getName());
        productEntity.setImage(productResponse.getImage());
        productEntity.setPrice(productResponse.getPrice());
        productEntity.setDescription(productResponse.getDescription());
        productEntity.setQuantity(productResponse.getQuantity());
        productEntity.setCreateDate(productResponse.getCreateDate());

        productEntity.setCategory(CategoryConverter.toCategoryEntity(productResponse.getCategory()));

        return productEntity;
    }
}
