package com.kttkpm.FoodOrder.Service.Imp;

import com.kttkpm.FoodOrder.Entity.ProductEntity;
import com.kttkpm.FoodOrder.Payload.Request.ProductRequest;
import com.kttkpm.FoodOrder.Payload.Response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductServiceImp {
    //boolean insertProduct(String name, MultipartFile file, double price, int quantity,int idCategory, String description) throws IOException;
    void insertProduct(ProductRequest productRequest);
    List<ProductResponse> getAllProduct();
    ProductResponse getProductById(int id);
    boolean deleteProductById(int idProduct);
    boolean updateProductById(int idProduct, ProductRequest productRequest) throws IOException;
    List<ProductResponse> getProductByName(String productName);
    List<ProductResponse> searchProducts(String keyword);
    List<ProductEntity> getAllProductByCategoryId(int id);
    Page<ProductResponse> getAllProductsPage(Integer pageNo);
}