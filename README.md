# Lập trình web
## Xây dựng hệ thống đặt đồ ăn online
# 1. Công nghệ sử dụng
- Ngôn ngữ lập trình: Java
- Framework: Spring boot, Security
- Library: Thymeleaf
- Mô hình MVC
- Cơ sở dữ liệu: MySQL

# 2. Workflow hệ thống
![image](https://github.com/idiotman-2212/FoodOrder-KTTKPM/assets/82036270/d5b7f53d-53f2-499c-9a50-89c0e53bad17)
# 3. Lượt đồ ERD
![image](https://github.com/idiotman-2212/FoodOrder-KTTKPM/assets/82036270/a480b1c1-dda3-40df-b391-43f677f4ca31)
# 4. Lượt đồ CSDL
![image](https://github.com/idiotman-2212/FoodOrder-KTTKPM/assets/82036270/158b218c-5e4e-4d74-862b-cc2b44f17431)

Script để tạo cơ sở dữ liệu có để trong thư mục **/src/main/resource/application.properties** của dự án

# 5. Giao diện chung
- Trang đăng nhập
![image](https://github.com/idiotman-2212/FoodOrder-KTTKPM/assets/82036270/4f285e31-438a-4bd0-967b-9b4d08336582)
- Trang đăng ký
![image](https://github.com/idiotman-2212/FoodOrder-KTTKPM/assets/82036270/283054b1-4fad-4619-bc0d-b01ca9f66eab)
- Trang quên mật khẩu
![image](https://github.com/idiotman-2212/FoodOrder-KTTKPM/assets/82036270/dda9d875-d21e-46b3-9977-ffaf911ee4f5)

# 6. Giao diện Admin
- Giao diện màn hình chính
![image](https://github.com/idiotman-2212/FoodOrder-KTTKPM/assets/82036270/044ed978-e9df-4946-8556-259a28c1e1b2)
- Quản lý danh sách người dùng (CRUD)
![image](https://github.com/idiotman-2212/FoodOrder-KTTKPM/assets/82036270/1aa35c47-b683-40b4-ac5e-5f28dd9e0fe4)
- Quản lý danh sách danh mục (CRUD)
![image](https://github.com/idiotman-2212/FoodOrder-KTTKPM/assets/82036270/ef6fe77e-f9cb-420f-bc85-e3d5b7ab0340)
- Quản lý danh sách sản phẩm (CRUD)
![image](https://github.com/idiotman-2212/FoodOrder-KTTKPM/assets/82036270/be640998-279b-47ec-941d-1f41e81dd45c)
- Quản lý danh sách đơn hàng (Sửa trạng thái đơn hàng)
![image](https://github.com/idiotman-2212/FoodOrder-KTTKPM/assets/82036270/023f8e01-9473-4370-8e84-cec7b65deb1b)

# 7. Giao diện người dùng
- Giao diện màn hình chính
![image](https://github.com/idiotman-2212/FoodOrder-KTTKPM/assets/82036270/4de5cfa4-0f59-440b-94a2-5fe52cf6b975)
- Chỉnh sửa thông tin cá nhân người dùng
![image](https://github.com/idiotman-2212/FoodOrder-KTTKPM/assets/82036270/432d7ee0-3805-48bd-aafa-f551a1d79dc6)
- Xem tất cả sản phẩm (Có lọc sản phẩm theo danh mục)
![image](https://github.com/idiotman-2212/FoodOrder-KTTKPM/assets/82036270/cfa80fa2-9d73-4b9a-ad7c-201e1a7c22e8)
- Xem chi tiết sản phẩm
![image](https://github.com/idiotman-2212/FoodOrder-KTTKPM/assets/82036270/42572c73-ccbc-4739-bebb-b03f14854a65)
- Xem giỏ hàng
![image](https://github.com/idiotman-2212/FoodOrder-KTTKPM/assets/82036270/bfd47637-3d1c-4794-a942-bd6a2509390c)
- Đặt hàng
![image](https://github.com/idiotman-2212/FoodOrder-KTTKPM/assets/82036270/8a419724-25fd-4c9d-8f24-2378eb66504b)

# 8. Hướng dẫn cài đặt
- Vào Terminal/Cmd gõ "git clone https://github.com/idiotman-2212/FoodOrder-KTTKPM.git" hoặc có thể Download Zip và giải nén thư mục
![image](https://github.com/idiotman-2212/FoodOrder-KTTKPM/assets/82036270/f5347ff0-780d-4387-b1c8-52f62a8a8dcb)
- Vào **/src/main/resource/application.properties** chỉnh sửa thông tin kết nối với MySQL
![image](https://github.com/idiotman-2212/FoodOrder-KTTKPM/assets/82036270/2f4526e5-9fff-4894-a90c-bc51da4e3ea0)
### Khởi chạy dự án 
- Vào Terminal trên IDE dùng lệnh <pre>mvn clean install</pre> sau đó gõ lệnh <pre>mvn spring-boot:run</pre>


