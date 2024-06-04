package com.kttkpm.FoodOrder.Controller;

import com.kttkpm.FoodOrder.Entity.*;
import com.kttkpm.FoodOrder.Payload.Request.ProductRequest;
import com.kttkpm.FoodOrder.Payload.Request.SignUpRequest;
import com.kttkpm.FoodOrder.Payload.Response.CategoryResponse;
import com.kttkpm.FoodOrder.Payload.Response.OrderResponse;
import com.kttkpm.FoodOrder.Payload.Response.ProductResponse;
import com.kttkpm.FoodOrder.Payload.Response.UserResponse;
import com.kttkpm.FoodOrder.Repository.CategoryRepository;
import com.kttkpm.FoodOrder.Repository.OrderRepository;
import com.kttkpm.FoodOrder.Repository.ProductRepository;
import com.kttkpm.FoodOrder.Repository.UserRepository;
import com.kttkpm.FoodOrder.Service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartService cartService;

    @Value("${root.folder}")
    private String rootFolder;

    @GetMapping("")
    public String adminHome(Model model) {
        String currentUser = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        UserEntity user = userService.findUserByEmail(currentUser);
        model.addAttribute("username", user.getUsername());
        return "adminPage";
    }//page admin home

    //Accounts
    @GetMapping("/users")
    public String getAcc(Model model , @RequestParam(name = "pageNo", defaultValue ="1") Integer pageNo ) {
        Page<UserResponse> users = userService.getAllUsersPage(pageNo);
        if(pageNo != null){
            model.addAttribute("totalPage", users.getTotalPages());
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("users", users);

        }else {
            model.addAttribute("error", "Không thể lấy danh sách danh mục.");
        }
        model.addAttribute("roles", roleService.getAllRoles());
        return "usersAdmin";
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
        RoleEntity roleEntity = null;

        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUsername(userDTO.getUsername());

        // Kiểm tra xem vai trò có tồn tại không
        if (userDTO.getIdRole() != 0) {
            int roleId = userDTO.getIdRole();
            roleEntity = roleService.getRoleById(roleId);
            if (roleEntity == null) {
                // Nếu vai trò không tồn tại, xử lý theo ý bạn (ví dụ: hiển thị thông báo lỗi)
                System.out.println("Vai trò không tồn tại");
                return "redirect:/admin/users";
            }
        }

        // Gán vai trò cho người dùng
        user.setRole(roleEntity);

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
            user.setPhone(signUpRequest.getPhone());
            user.setAddress(signUpRequest.getAddress());

            if (signUpRequest.getIdRole() != 0) {
                int roleId = signUpRequest.getIdRole();
                RoleEntity userRole = roleService.getRoleById(roleId);
                if (userRole != null) {
                    user.setRole(userRole);
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

    @GetMapping("/users/search")
    public String searchUsers(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<UserResponse> userList = userService.searchUsers(keyword);
        model.addAttribute("users", userList);
        model.addAttribute("keyword", keyword);
        return "usersAdmin";
    }//search user

    //Categories session
    @GetMapping("/categories")
    public String getCat(Model model, @RequestParam(name = "pageNo", defaultValue ="1") Integer pageNo ) {
        Page<CategoryResponse> category = categoryService.getAllCategoryPage(pageNo);
        if(pageNo != null){
            model.addAttribute("totalPage", category.getTotalPages());
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("categories", category);

        }else {
            model.addAttribute("error", "Không thể lấy danh sách danh mục.");
        }
        return "category";
    }//view all categories

    @GetMapping("/categories/search")
    public String searchCategory(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<CategoryResponse> categoryList = categoryService.searchCategories(keyword);
        model.addAttribute("categories", categoryList);
        model.addAttribute("keyword", keyword);
        return "category";
    }//search category

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
        model.addAttribute("productRequest", new ProductRequest());
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
                                       @PathVariable("id") String idStr,
                                       @RequestParam("file") MultipartFile file,
                                       @RequestParam("price") double price,
                                       @RequestParam("quantity") int quantity,
                                       @RequestParam("name") String name,
                                       @RequestParam("category") int categoryId,
                                       @RequestParam("description") String description,
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
            productEntity.setName(name);
            productEntity.setPrice(price);
            productEntity.setQuantity(quantity);
            productEntity.setDescription(description);

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

            CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
            productEntity.setCategory(categoryEntity);

            productRepository.save(productEntity);

            return "redirect:/admin/product";
        } else {
            return "404";
        }
    }

    //Order session
    @GetMapping("/orders")
    public String showOrderPage(Model model, @RequestParam(name = "pageNo", defaultValue ="1") Integer pageNo){
        Page<OrderResponse> orderResponses = orderService.getAllOrdersPage(pageNo);
        if(pageNo != null){
            model.addAttribute("totalPage", orderResponses.getTotalPages());
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("orders", orderResponses);

        }else {
            model.addAttribute("error", "Không thể lấy danh sách đơn hanng.");
        }
        return "orderAdmin";
    }//view all orders

    @GetMapping("/orders/search")
    public String searchOrders(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<OrderResponse> orderList = orderService.searchOrders(keyword);
        model.addAttribute("orders", orderList);
        model.addAttribute("keyword", keyword);
        return "orderAdmin";
    }//search order

    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable int id) {
        orderService.deleteOrderById(id);
        return "redirect:/admin/orders";
    }//delete 1 order

    @GetMapping("/orders/update/{id}")
    public String getOrderUpdateForm(@PathVariable int id, Model model) {
        OrderResponse order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        model.addAttribute("orderStatuses", OrderStatus.values());
        return "ordersEdit";
    }

    @PostMapping("/orders/update/{id}")
    public String updateOrderStatus(@PathVariable int id, @RequestParam OrderStatus orderStatus) {
        orderService.updateOrderStatus(id, orderStatus);
        return "redirect:/admin/orders";
    }
}