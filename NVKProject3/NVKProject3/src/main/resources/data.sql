-- =======================================================
-- 1. RESET DATA
-- =======================================================
SET FOREIGN_KEY_CHECKS = 0;
DELETE
FROM nvk_reviews;
DELETE
FROM nvk_products;
DELETE
FROM nvk_customers;
DELETE
FROM nvk_admins;
DELETE
FROM nvk_categories;

ALTER TABLE nvk_reviews
    AUTO_INCREMENT = 1;
ALTER TABLE nvk_products
    AUTO_INCREMENT = 1;
ALTER TABLE nvk_customers
    AUTO_INCREMENT = 1;
ALTER TABLE nvk_admins
    AUTO_INCREMENT = 1;
ALTER TABLE nvk_categories
    AUTO_INCREMENT = 1;
SET FOREIGN_KEY_CHECKS = 1;

-- =======================================================
-- 2. INSERT DATA (DÙNG API ẢNH ẢO - KHÔNG LO CHẾT LINK)
-- =======================================================

-- 1. DANH MỤC
INSERT INTO nvk_categories (nvk_name)
VALUES ('Điện thoại'),
       ('Laptop'),
       ('Máy tính bảng'),
       ('Đồng hồ'),
       ('Phụ kiện'),
       ('PC & Màn hình');

-- 2. ADMIN & KHÁCH
INSERT INTO nvk_admins (nvk_username, nvk_password, nvk_full_name, nvk_active, nvk_avatar)
VALUES ('admin', '123456', 'Quản Trị Viên', 1, 'https://placehold.co/200x200?text=Admin');

INSERT INTO nvk_customers (nvk_email, nvk_password, nvk_full_name, nvk_phone, nvk_address, nvk_active, nvk_avatar)
VALUES ('khach1@gmail.com', '123456', 'Nguyễn Văn A', '0912345678', 'Hà Nội', 1,
        'https://placehold.co/200x200?text=Khach+A'),
       ('user@nvk.com', '123456', 'Khách Demo', '0999888777', 'Đà Nẵng', 1,
        'https://placehold.co/200x200?text=Khach+Demo');

-- 3. SẢN PHẨM (Link Online Placehold.co)

-- --- ĐIỆN THOẠI ---
INSERT INTO nvk_products (nvk_code, nvk_name, nvk_brand, nvk_price, nvk_old_price, nvk_quantity, nvk_img_url,
                          nvk_description, nvk_status, nvk_cate_id)
VALUES ('DT001', 'iPhone 15 Pro Max', 'Apple', 29990000, 34990000, 50,
        'https://placehold.co/600x400/png?text=iPhone+15+Pro+Max',
        'Vỏ Titan tự nhiên, chip A17 Pro mạnh mẽ nhất.', 1, 1),

       ('DT002', 'Samsung Galaxy S24 Ultra', 'Samsung', 26990000, 31990000, 45,
        'https://placehold.co/600x400/png?text=Samsung+S24+Ultra',
        'Galaxy AI, khung Titan, camera 200MP.', 1, 1),

       ('DT003', 'Xiaomi 13 Ultra', 'Xiaomi', 24990000, 29990000, 30,
        'https://placehold.co/600x400/png?text=Xiaomi+13+Ultra',
        'Huyền thoại ống kính Leica, cảm biến 1 inch.', 1, 1),

       ('DT004', 'Google Pixel 8 Pro', 'Google', 25990000, 30000000, 20,
        'https://placehold.co/600x400/png?text=Pixel+8+Pro',
        'Camera AI đỉnh cao từ Google.', 1, 1);

-- --- LAPTOP ---
INSERT INTO nvk_products (nvk_code, nvk_name, nvk_brand, nvk_price, nvk_old_price, nvk_quantity, nvk_img_url,
                          nvk_description, nvk_status, nvk_cate_id)
VALUES ('LT001', 'MacBook Air M2', 'Apple', 24990000, 28990000, 100,
        'https://placehold.co/600x400/png?text=MacBook+Air+M2',
        'Màu Midnight tuyệt đẹp, mỏng nhẹ khó tin.', 1, 2),

       ('LT002', 'Dell XPS 13', 'Dell', 45000000, 50000000, 10,
        'https://placehold.co/600x400/png?text=Dell+XPS+13',
        'Thiết kế viền siêu mỏng, sang trọng.', 1, 2),

       ('LT003', 'ThinkPad X1 Carbon', 'Lenovo', 19990000, 24990000, 60,
        'https://placehold.co/600x400/png?text=ThinkPad+X1',
        'Bàn phím trứ danh, bền bỉ chuẩn doanh nhân.', 1, 2);

-- --- MÁY TÍNH BẢNG ---
INSERT INTO nvk_products (nvk_code, nvk_name, nvk_brand, nvk_price, nvk_old_price, nvk_quantity, nvk_img_url,
                          nvk_description, nvk_status, nvk_cate_id)
VALUES ('TB001', 'iPad Pro 11 inch', 'Apple', 23990000, 26990000, 25,
        'https://placehold.co/600x400/png?text=iPad+Pro+11',
        'Hiệu năng thay thế Laptop.', 1, 3);

-- --- ĐỒNG HỒ ---
INSERT INTO nvk_products (nvk_code, nvk_name, nvk_brand, nvk_price, nvk_old_price, nvk_quantity, nvk_img_url,
                          nvk_description, nvk_status, nvk_cate_id)
VALUES ('DH001', 'Apple Watch Ultra', 'Apple', 19590000, 21990000, 50,
        'https://placehold.co/600x400/png?text=Apple+Watch+Ultra',
        'Thiết kế hầm hố, pin trâu cho dân thể thao.', 1, 4);

-- --- PHỤ KIỆN ---
INSERT INTO nvk_products (nvk_code, nvk_name, nvk_brand, nvk_price, nvk_old_price, nvk_quantity, nvk_img_url,
                          nvk_description, nvk_status, nvk_cate_id)
VALUES ('PK001', 'AirPods Pro 2', 'Apple', 5490000, 6190000, 150,
        'https://placehold.co/600x400/png?text=AirPods+Pro+2',
        'Chống ồn chủ động, âm thanh không gian.', 1, 5),

       ('PK002', 'Sony Headphones', 'Sony', 6990000, 8490000, 30,
        'https://placehold.co/600x400/png?text=Sony+Headphones',
        'Vua chống ồn, chất âm phòng thu.', 1, 5);

-- 5. ĐÁNH GIÁ
INSERT INTO nvk_reviews (nvk_rating, nvk_comment, nvk_created_date, nvk_customer_id, nvk_product_id)
VALUES (5, 'Máy đẹp xuất sắc, giao hàng nhanh.', CURDATE(), 1, 1),
       (4, 'Dùng tốt nhưng hơi nóng khi chơi game nặng.', CURDATE(), 2, 2);