package com.kttkpm.FoodOrder.Service;

import com.kttkpm.FoodOrder.Entity.CategoryEntity;
import com.kttkpm.FoodOrder.Entity.ProductEntity;
import com.kttkpm.FoodOrder.Helper.ProductConverter;
import com.kttkpm.FoodOrder.Payload.Request.ProductRequest;
import com.kttkpm.FoodOrder.Payload.Response.CategoryResponse;
import com.kttkpm.FoodOrder.Payload.Response.ProductResponse;
import com.kttkpm.FoodOrder.Repository.CategoryRepository;
import com.kttkpm.FoodOrder.Repository.ProductRepository;
import com.kttkpm.FoodOrder.Service.Imp.ProductServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService implements ProductServiceImp {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductConverter productConverter;

//    @Value("${root.folder}")
    private String rootFolder="../resources/static/productImages";

    @Override
    public void insertProduct(ProductRequest productRequest) {
        try {
            // Save CategoryEntity
            CategoryEntity categoryEntity = categoryRepository.findByName(productRequest.getCategoryName());
            if (categoryEntity == null) {
                categoryEntity = new CategoryEntity();
                categoryEntity.setName(productRequest.getCategoryName());
                try {
                    categoryEntity = categoryRepository.save(categoryEntity);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            // Save ProductEntity
            String pathImage = rootFolder + "/" + productRequest.getFile().getOriginalFilename();
            Path path = Paths.get(rootFolder);
            Path pathImageCopy = Paths.get(pathImage);

            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }

            Files.copy(productRequest.getFile().getInputStream(), pathImageCopy, StandardCopyOption.REPLACE_EXISTING);

            ProductEntity productEntity = new ProductEntity();
            productEntity.setName(productRequest.getName());
            productEntity.setImage(productRequest.getFile().getOriginalFilename());
            productEntity.setPrice(productRequest.getPrice());
            productEntity.setQuantity(productRequest.getQuantity());
            productEntity.setDescription(productRequest.getDescription());
            productEntity.setCategory(categoryEntity);
            productEntity.setCreateDate(new Date());

            productRepository.save(productEntity);

        } catch (IOException | DataAccessException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<ProductResponse> getAllProduct() {
        List<ProductEntity> list = productRepository.findAll();
        List<ProductResponse> responseList = new ArrayList<>();

        for (ProductEntity item : list) {
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(item.getId());
            productResponse.setName(item.getName());
            productResponse.setImage(item.getImage());
            productResponse.setQuantity(item.getQuantity());
            productResponse.setDescription(item.getDescription());

            if (item.getPrice() != null) {
                productResponse.setPrice(item.getPrice().doubleValue());
            } else {
                productResponse.setPrice(0.0);
            }

            CategoryEntity categoryEntity = item.getCategory();
            if (categoryEntity != null) {
                CategoryResponse categoryResponse = new CategoryResponse();
                categoryResponse.setName(categoryEntity.getName());
                categoryResponse.setId(categoryEntity.getId());
                categoryResponse.setCreateDate(categoryEntity.getCreateDate());
                productResponse.setCategory(categoryResponse);
            }
            productResponse.setCreateDate(item.getCreateDate());

            responseList.add(productResponse);
        }

        return responseList;
    }

    @Override
    public ProductResponse getProductById(int id) {
        Optional<ProductEntity> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            ProductEntity productEntity = productOptional.get();
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(productEntity.getId());
            productResponse.setName(productEntity.getName());
            productResponse.setImage(productEntity.getImage());
            productResponse.setDescription(productEntity.getDescription());
            productResponse.setPrice(productEntity.getPrice());
            productResponse.setCreateDate(productEntity.getCreateDate());

            CategoryEntity categoryEntity = productEntity.getCategory();
            if (categoryEntity != null) {
                CategoryResponse categoryResponse = new CategoryResponse();
                categoryResponse.setName(categoryEntity.getName());
                categoryResponse.setId(categoryEntity.getId());
                categoryResponse.setCreateDate(categoryEntity.getCreateDate());
                productResponse.setCategory(categoryResponse);
            }
            return productResponse;
        }else {
            return null;
        }
    }

    @Override
    public boolean deleteProductById(int idProduct) {
        if(productRepository.existsById(idProduct)){
            productRepository.deleteById(idProduct);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateProductById(int idProduct, String name, MultipartFile file, String description, double price, int quanity, int idCategory) throws IOException {
        Optional<ProductEntity> productOptional = productRepository.findById(idProduct);
        List<ProductResponse> responseList = new ArrayList<>();

        if (productOptional.isPresent()) {
            ProductEntity productEntity = productOptional.get();

            String oldImage = productEntity.getImage();
            if (oldImage != null) {
                Files.deleteIfExists(Paths.get(rootFolder, oldImage));
            }

            String newImage = file.getOriginalFilename();
            Path newPathImageCopy = Paths.get(rootFolder, newImage);
            Files.copy(file.getInputStream(), newPathImageCopy, StandardCopyOption.REPLACE_EXISTING);

            productEntity.setName(name);
            productEntity.setImage(file.getOriginalFilename());
            productEntity.setPrice(price);
            productEntity.setQuantity(quanity);
            productEntity.setDescription(description);

            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setId(idCategory);
            productEntity.setCategory(categoryEntity);

            productEntity.setCreateDate(new Date());

            productRepository.save(productEntity);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<ProductResponse> getProductByName(String productName) {
        List<ProductEntity> products = productRepository.findByNameContainingIgnoreCase(productName);
        return products.stream()
                .map(ProductConverter::toProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> searchProducts(String keyword) {
        List<ProductEntity> products = productRepository.searchProducts(keyword);
        return products.stream()
                .map(ProductConverter::toProductResponse)
                .collect(Collectors.toList());
    }
    public List<ProductEntity> getAllProductByCategoryId(int id) {
        return productRepository.findAllByCategory_Id(id);
    }

    @Override
    public Page<ProductResponse> getAllProductsPage(Integer pageNo) {
        int pageSize = 3; // Số sản phẩm trên mỗi trang
        List<ProductResponse> allProducts = getAllProduct();
        // Phân trang dữ liệu
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allProducts.size());

        List<ProductResponse> sublist = allProducts.subList(start, end);
        return new PageImpl<>(sublist, pageable, allProducts.size());
    }

}
