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
    // Sử dụng ProductConverter để thực hiện chuyển đổi
    private final ProductConverter productConverter = ProductConverter.getInstance();
    @Value("${root.folder}")
    private String rootFolder;
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
    public boolean updateProductById(int idProduct, ProductRequest productRequest) throws IOException {
        Optional<ProductEntity> productOptional = productRepository.findById(idProduct);

        if (productOptional.isPresent()) {
            ProductEntity productEntity = productOptional.get();
            String oldImage = productEntity.getImage();
            if (oldImage != null) {
                Files.deleteIfExists(Paths.get(rootFolder, oldImage));
            }

            String newImage = productRequest.getFile().getOriginalFilename();
            Path newPathImageCopy = Paths.get(rootFolder, newImage);
            Files.copy(productRequest.getFile().getInputStream(), newPathImageCopy, StandardCopyOption.REPLACE_EXISTING);

            productEntity.setName(productRequest.getName());
            productEntity.setImage(newImage);
            productEntity.setPrice(productRequest.getPrice());
            productEntity.setQuantity(productRequest.getQuantity());
            productEntity.setDescription(productRequest.getDescription());
            productEntity.setCreateDate(new Date()); // Set ngày tạo mới khi cập nhật

            // Tìm hoặc tạo mới danh mục sản phẩm
            CategoryEntity categoryEntity = categoryRepository.findByName(productRequest.getCategoryName());
            if (categoryEntity == null) {
                categoryEntity = new CategoryEntity();
                categoryEntity.setName(productRequest.getCategoryName());
                categoryEntity = categoryRepository.save(categoryEntity);
            }

            productEntity.setCategory(categoryEntity);

            productRepository.save(productEntity);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<ProductResponse> getProductByName(String productName) {
        List<ProductEntity> productList = productRepository.findByNameContaining(productName);
        List<ProductResponse> responseList = new ArrayList<>();

        for (ProductEntity item : productList) {
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(item.getId());
            productResponse.setName(item.getName());
            productResponse.setImage(item.getImage());
            productResponse.setDescription(item.getDescription());
            productResponse.setPrice(item.getPrice());
            productResponse.setCreateDate(item.getCreateDate());

            responseList.add(productResponse);
        }

        return responseList;
    }

    @Override
    public List<ProductResponse> searchProducts(String keyword) {
        List<ProductEntity> products = productRepository.searchProducts(keyword);
        return products.stream()
                .map(productConverter::toProductResponse)
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
