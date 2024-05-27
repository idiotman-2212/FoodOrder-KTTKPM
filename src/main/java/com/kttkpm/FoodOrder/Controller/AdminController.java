package com.kttkpm.FoodOrder.Controller;

import com.kttkpm.FoodOrder.Entity.CategoryEntity;
import com.kttkpm.FoodOrder.Entity.ProductEntity;
import com.kttkpm.FoodOrder.Entity.RoleEntity;
import com.kttkpm.FoodOrder.Entity.UserEntity;
import com.kttkpm.FoodOrder.Payload.Request.ProductRequest;
import com.kttkpm.FoodOrder.Payload.Request.SignUpRequest;
import com.kttkpm.FoodOrder.Payload.Response.CategoryResponse;
import com.kttkpm.FoodOrder.Payload.Response.ProductResponse;
import com.kttkpm.FoodOrder.Repository.CategoryRepository;
import com.kttkpm.FoodOrder.Repository.ProductRepository;
import com.kttkpm.FoodOrder.Repository.RoleRepository;
import com.kttkpm.FoodOrder.Repository.UserRepository;
import com.kttkpm.FoodOrder.Service.CategoryService;
import com.kttkpm.FoodOrder.Service.ProductService;
import com.kttkpm.FoodOrder.Service.RoleService;
import com.kttkpm.FoodOrder.Service.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;


@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Value("${root.folder}")
    private String rootFolder;

    @GetMapping("")
    public String adminHome() {
        return "adminPage";
    }//page admin home

    //Accounts
    @GetMapping("/users")
    public String getAcc(Model model) {
        model.addAttribute("users", userService.getAllUser());
        model.addAttribute("roles", roleService.getAllRoles());
        return "users";
    }

    @GetMapping("/users/add")
    public String getUserAdd(Model model) {
        model.addAttribute("userDTO", new SignUpRequest());
        model.addAttribute("roles", roleService.getAllRoles());
        return "usersAdd";
    }

    @PostMapping("/users/add")
    public String postUserAdd(@ModelAttribute("userDTO") SignUpRequest userDTO) {
        UserEntity user = new UserEntity();
        RoleEntity roleEntity = new RoleEntity();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUsername(userDTO.getUsername());

        if (userDTO.getIdRole() != 0) {
            int roleId = userDTO.getIdRole();
            roleEntity = roleService.getRoleById(roleId);
        }
        if (roleEntity == null) {
            System.out.println("Vai trò không tồn tại");
            return "redirect:/admin/users";
        }

        RoleEntity roles = new RoleEntity();
        user.setRole(roles);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            System.out.println("Thêm thất bại " + e.getMessage());
            return "redirect:/admin/users?error=true";
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.deleteUserById(id);
        return "redirect:/admin/users";
    }//delete 1 user

    @GetMapping("/users/update/{id}")
    public String getUserUpdate(@PathVariable int id, Model model) {
        Optional<UserEntity> opUser = userRepository.findById(id);

        if (opUser.isPresent()) {
            UserEntity user = opUser.get();
            SignUpRequest userDTO = new SignUpRequest();
            userDTO.setId(user.getId());
            userDTO.setEmail(user.getEmail());

            userDTO.setPassword(null);
            userDTO.setUsername(user.getUsername());


            if (user.getRole() != null) {
                userDTO.setIdRole(user.getRole().getId());
            }
            model.addAttribute("userDTO", userDTO);
            model.addAttribute("roles", roleService.getAllRoles());
            return "usersEdit";
        } else {
            return "404";
        }
    }


    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable int id, @ModelAttribute("userDTO") SignUpRequest signUpRequest) {
        Optional<UserEntity> opUser = userRepository.findById(id);

        if (opUser.isPresent()) {
            UserEntity user = opUser.get();
            user.setEmail(signUpRequest.getEmail());
            if (signUpRequest.getPassword() != null && !signUpRequest.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            }

            user.setUsername(signUpRequest.getUsername());

            if (signUpRequest.getIdRole() != 0) {
                int roleId = signUpRequest.getIdRole();
                RoleEntity userRole = roleService.getRoleById(roleId);

                if (userRole != null) {
                    RoleEntity roles = new RoleEntity();
                    user.setRole(roles);
                } else {
                    System.out.println("Vai trò không tồn tại");
                    return "redirect:/admin/users";
                }
            }
                userRepository.save(user);
                return "redirect:/admin/users";
            } else {
                return "404";
            }
        }//update user

    @GetMapping("users/search")
    public String searchUser(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<UserEntity> list;

        if (keyword != null && !keyword.isEmpty()) {
            list = userRepository.searchUsers(keyword);

            if (list.isEmpty()) {
                model.addAttribute("noResults", true);
            }
        } else {
            list = userRepository.findAll();
        }
        model.addAttribute("list", list);
        model.addAttribute("keyword", keyword);
        return "users";
    }//search user

    //Categories session
    @GetMapping("/categories")
    public String getCat(Model model) {
        model.addAttribute("categories", categoryService.getAllCategory());
        return "category";
    }//view all categories

    @GetMapping("/categories/add")
    public String getAddCategory(Model model) {
        model.addAttribute("category", new CategoryEntity());
        return "categoriesAdd";
    }

    @PostMapping("/categories/add")
    public String postAddCategory(@ModelAttribute("category") CategoryEntity category, Model model) {
        boolean isCategoryAdded = categoryService.insertCategory(category.getName());

        if (isCategoryAdded) {
            return "redirect:/admin/categories";
        } else {
            model.addAttribute("error", "Failed to add category. Please try again.");
            return "categoriesAdd";
        }
    }//form add new category > do add

    @GetMapping("/categories/delete/{id}")
    public String deleteCat(@PathVariable int id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/admin/categories";
    }//delete 1 category

    @GetMapping("/categories/update/{id}")
    public String updateCat(@PathVariable int id, Model model) {
        Optional<CategoryEntity> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            return "categoriesEdit";
        } else {
            return "404";
        }
    }//form edit category, fill old data into form

    @PostMapping("/categories/update/{id}")
    public String updateCategory(@ModelAttribute CategoryEntity updatedCategory) {
        Optional<CategoryEntity> existingCategory = categoryRepository.findById(updatedCategory.getId());

        if (existingCategory.isPresent()) {
            CategoryEntity categoryEntity = existingCategory.get();
            categoryEntity.setName(updatedCategory.getName());
            categoryRepository.save(categoryEntity);
            return "redirect:/admin/categories";
        } else {
            return "404";
        }
    }

    //Products session
    @GetMapping("/product")
    public String getProduct(Model model, @RequestParam(name = "pageNo", defaultValue ="1") Integer pageNo ) {
        Page<ProductResponse> products = productService.getAllProductsPage(pageNo);
        if(pageNo != null){
            model.addAttribute("totalPage", products.getTotalPages());
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("products", products);

        }else {
            model.addAttribute("error", "Không thể lấy danh sách sản phẩm.");
        }
        return "productsAdmin";
    }//view all products

    @GetMapping("/product/search")
    public String searchProduct(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<ProductResponse> productList = productService.searchProducts(keyword);
        model.addAttribute("products", productList);
        model.addAttribute("keyword", keyword);
        return "productsAdmin";
    }//search product

    @GetMapping("/product/add")
    public String getProductAdd(Model model) {
        model.addAttribute("productRequest", new ProductEntity());
        model.addAttribute("categories", categoryService.getAllCategory());

        return "productsAdd";
    }

    @PostMapping("/product/add")
    public String postProductAdd(@ModelAttribute("productRequest") @Valid ProductRequest productRequest,
                                 BindingResult result,
                                 Model model) throws IOException {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategory());
            return "productsAdd";
        }

        productService.insertProduct(productRequest);

        return "redirect:/admin/product";
    }//form add new product > do add

    @GetMapping("/product/delete/{id}")
    public String deleteProductById(@PathVariable int id) {
        productService.deleteProductById(id);
        return "redirect:/admin/product";
    }//delete 1 product

    @GetMapping("/product/update/{id}")
    public String updateProductById(@PathVariable("id") String idStr, Model model) {
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            return "redirect:/error";
        }
        // Tìm kiếm sản phẩm theo ID
        Optional<ProductEntity> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            ProductEntity productEntity = productOptional.get();
            model.addAttribute("productRequest", productEntity);
            model.addAttribute("categories", categoryService.getAllCategory());

            return "productsEdit";
        } else {
            return "redirect:/404";
        }
    }
    @PostMapping("/product/update/{id}")
    public String processUpdateProduct(@ModelAttribute("productRequest") ProductRequest productDTO,
                                       @PathVariable String idStr,
                                       @RequestParam MultipartFile file,
                                       @RequestParam String categoryName,
                                       @RequestParam String desc,
                                       Model model) throws IOException {
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            return "redirect:/error";
        }

        Optional<ProductEntity> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            ProductEntity productEntity = productOptional.get();

            String rootFolder = "src/main/resources/static/images";

            // Cập nhật thông tin sản phẩm
            productEntity.setName(productDTO.getName());
            productEntity.setPrice(productDTO.getPrice());
            productEntity.setQuantity(productDTO.getQuantity());
            productEntity.setDescription(productDTO.getDesc());

            // Lưu ảnh nếu người dùng chọn ảnh mới
            if (!file.isEmpty()) {
                String oldImage = productEntity.getImage();
                if (oldImage != null) {
                    Files.deleteIfExists(Paths.get(rootFolder, oldImage));
                }

                String newImage = file.getOriginalFilename();
                Path newPathImageCopy = Paths.get(rootFolder, newImage);
                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, newPathImageCopy, StandardCopyOption.REPLACE_EXISTING);
                }
                productEntity.setImage(newImage);
            }

            CategoryEntity categoryEntity = categoryRepository.findByName(categoryName);
            productEntity.setCategory(categoryEntity);

            productRepository.save(productEntity);

            return "redirect:/admin/product";
        } else {
            return "404";
        }
    }

}