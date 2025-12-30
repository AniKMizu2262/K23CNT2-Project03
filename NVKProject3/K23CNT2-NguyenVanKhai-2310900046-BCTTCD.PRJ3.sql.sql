create database NVK_Project3_DB
use NVK_Project3_DB
-- 1. BẢNG DANH MỤC
CREATE TABLE nvk_categories (
  nvk_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nvk_name VARCHAR(255) NOT NULL
);

-- 2. BẢNG SẢN PHẨM
CREATE TABLE nvk_products (
  nvk_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nvk_code VARCHAR(255) UNIQUE,
  nvk_name VARCHAR(255) NOT NULL,
  nvk_img_url VARCHAR(1000),
  nvk_price DOUBLE,
  nvk_old_price DOUBLE,
  nvk_quantity INT DEFAULT 0,
  nvk_description TEXT,
  nvk_status INT DEFAULT 1 COMMENT '0:An, 1:Ban, 2:Sap ve',
  nvk_cate_id BIGINT,
  nvk_brand VARCHAR(255),
  FOREIGN KEY (nvk_cate_id) REFERENCES nvk_categories(nvk_id)
);

-- 3. BẢNG KHÁCH HÀNG
CREATE TABLE nvk_customers (
  nvk_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nvk_email VARCHAR(255) NOT NULL UNIQUE,
  nvk_password VARCHAR(255) NOT NULL,
  nvk_full_name VARCHAR(255),
  nvk_phone VARCHAR(255),
  nvk_address VARCHAR(255),
  nvk_avatar VARCHAR(255),
  nvk_active BIT(1) DEFAULT 1
);

-- 4. BẢNG ĐƠN HÀNG
CREATE TABLE nvk_orders (
  nvk_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nvk_code VARCHAR(255) UNIQUE,
  nvk_customer_id BIGINT,
  nvk_created_date DATETIME(6),
  nvk_total_amount DOUBLE,
  nvk_receiver_name VARCHAR(255),
  nvk_receiver_phone VARCHAR(255),
  nvk_receiver_address VARCHAR(255),
  nvk_status INT DEFAULT 0,
  FOREIGN KEY (nvk_customer_id) REFERENCES nvk_customers(nvk_id)
);

-- 5. BẢNG CHI TIẾT ĐƠN HÀNG
CREATE TABLE nvk_order_details (
  nvk_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nvk_order_id BIGINT,
  nvk_product_id BIGINT,
  nvk_quantity INT,
  nvk_price DOUBLE,
  FOREIGN KEY (nvk_order_id) REFERENCES nvk_orders(nvk_id),
  FOREIGN KEY (nvk_product_id) REFERENCES nvk_products(nvk_id)
);

-- 6. BẢNG ĐÁNH GIÁ (REVIEW)
CREATE TABLE nvk_reviews (
  nvk_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nvk_product_id BIGINT,
  nvk_customer_id BIGINT,
  nvk_rating INT CHECK (nvk_rating BETWEEN 1 AND 5),
  nvk_comment TEXT,
  nvk_created_date DATE,
  FOREIGN KEY (nvk_product_id) REFERENCES nvk_products(nvk_id),
  FOREIGN KEY (nvk_customer_id) REFERENCES nvk_customers(nvk_id)
);

-- 7. BẢNG QUẢN TRỊ VIÊN (ADMIN)
CREATE TABLE nvk_admins (
  nvk_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nvk_username VARCHAR(255) NOT NULL UNIQUE,
  nvk_password VARCHAR(255) NOT NULL,
  nvk_full_name VARCHAR(255),
  nvk_avatar VARCHAR(255),
  nvk_active BIT(1) DEFAULT 1
);