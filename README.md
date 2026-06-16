<h1 align="center">
  🍽️ Premium Restaurant POS System
</h1>

*Read this in other languages: [English](README_en.md), [Tiếng Việt](README.md).*

<p align="center">
  <img src="https://img.shields.io/badge/Java-11%2B-blue?style=for-the-badge&logo=java" alt="Java Version">
  <img src="https://img.shields.io/badge/MySQL-8.0-orange?style=for-the-badge&logo=mysql" alt="MySQL">
  <img src="https://img.shields.io/badge/Swing-GUI-brightgreen?style=for-the-badge" alt="Java Swing">
  <img src="https://img.shields.io/badge/Architecture-3--Tier-purple?style=for-the-badge" alt="3-Tier Architecture">
  <img src="https://img.shields.io/badge/License-MIT-black?style=for-the-badge" alt="License">
</p>

<p align="center">
  Một hệ thống quản lý nhà hàng (Point of Sale - POS) chuyên nghiệp được xây dựng bằng <b>Java Swing</b> và <b>MySQL</b>, áp dụng triệt để mô hình <b>3-Tier Architecture (3 Lớp)</b>, bảo mật cao và giao diện Dark Mode (Catppuccin Mocha) hiện đại.
</p>

---

## 🌟 Tính Năng Nổi Bật (Key Features)

### 💻 Dành cho Quản Lý (Manager)
- **Dashboard Thống Kê**: Tích hợp biểu đồ doanh thu (Java2D Bar Chart) tự vẽ, trực quan hóa dữ liệu theo thời gian thực.
- **Quản lý Nhân Sự & Tài Khoản**: Phân quyền hệ thống, mã hóa mật khẩu bảo mật (SHA-256 + Salt).
- **Quản Lý Bàn & Đặt Bàn**: Theo dõi trạng thái bàn (Trống, Đang sử dụng, Đã đặt trước).
- **Quản Lý Thực Đơn (Menu)**: Tích hợp 3 phân hệ (Món ăn, Đồ uống, Danh mục) vào chung một giao diện thông minh.
- **Xuất Báo Cáo (CSV/Excel)**: Xuất dữ liệu kinh doanh không lỗi font Tiếng Việt (UTF-8 BOM).

### 🛒 Dành cho Thu Ngân (Staff / POS)
- **Giao Diện POS Cảm Ứng**: Thao tác chọn bàn, thêm/bớt món ăn nhanh chóng chỉ bằng các click chuột.
- **Tính Toán Thông Minh**: Hỗ trợ tuỳ chỉnh phần trăm (%) Giảm giá và Thuế (VAT).
- **In Hóa Đơn Tự Động**: Tự động sinh file hóa đơn HTML đẹp mắt và gọi lệnh In (`Ctrl+P`) qua trình duyệt ngay khi thanh toán.

---

## 🏗️ Kiến Trúc Hệ Thống (Architecture)

Dự án áp dụng mô hình chuẩn **3-Tier Architecture**:
1. **GUI (Presentation Layer)**: Giao diện tương tác người dùng, tích hợp các Component tự thiết kế (RoundedButton, StyledTable, SearchBar, ToastNotification).
2. **BLL (Business Logic Layer)**: Xử lý nghiệp vụ, kiểm tra tính đúng đắn của dữ liệu trước khi lưu trữ (Validation).
3. **DAL (Data Access Layer) & DTO**: Giao tiếp trực tiếp với MySQL thông qua mô hình GenericDAO và `PreparedStatement` (Chống 100% SQL Injection).

---

## 🎨 Giao Diện (UI/UX Design)

Hệ thống loại bỏ hoàn toàn giao diện Java Swing nhàm chán bằng cách:
- Áp dụng hệ màu **Catppuccin Mocha Dark Theme** hiện đại và dịu mắt.
- Tự thiết kế toàn bộ Components để có góc bo tròn, màu sắc highlight khi hover.
- Thay thế các hộp thoại cảnh báo khó chịu (`JOptionPane`) bằng hệ thống **Toast Notification** trượt mượt mà.

---

## 🚀 Hướng Dẫn Cài Đặt (Installation)

### 1. Chuẩn Bị Môi Trường
- JDK 11 trở lên (Khuyến nghị Java 17 hoặc 21).
- Trình quản lý CSDL: MySQL Server (Khuyên dùng [XAMPP](https://www.apachefriends.org/) hoặc MySQL Workbench).
- IDE: Apache NetBeans, IntelliJ IDEA hoặc Eclipse.

### 2. Cài Đặt Cơ Sở Dữ Liệu
1. Mở MySQL / phpMyAdmin.
2. Import file `sql/restaurant_schema.sql` có sẵn trong thư mục dự án.
3. Script sẽ tự động tạo database `restaurantmanage` kèm toàn bộ dữ liệu mẫu (Dummy data).

### 3. Cấu Hình Ứng Dụng
Mở file cấu hình Database (`src/config/AppConfig.java`) và thay đổi theo cấu hình máy của bạn:
```java
public static final String DB_URL = "jdbc:mysql://localhost:3306/restaurantmanage";
public static final String DB_USER = "root";
public static final String DB_PASS = "12345"; // Đổi mật khẩu nếu cần
```

### 4. Chạy Ứng Dụng
- Mở dự án bằng NetBeans.
- Chạy file `src/GUI/Login.java` (ấn `Shift + F6`).
- Đăng nhập bằng tài khoản Quản trị viên mặc định:
  - **Tên đăng nhập:** `admin`
  - **Mật khẩu:** `admin123`

---

## 📄 Giấy Phép (License)
Dự án được phân phối dưới giấy phép **MIT License**. Bạn hoàn toàn có thể sao chép, chỉnh sửa và sử dụng cho mục đích cá nhân, đồ án môn học hoặc thương mại.
