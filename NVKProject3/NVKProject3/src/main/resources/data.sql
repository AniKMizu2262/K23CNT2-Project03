-- =======================================================
-- 1. RESET DATA (LÀM SẠCH SÀNH SANH & RESET ID VỀ 1)
-- =======================================================
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE nvk_reviews;
TRUNCATE TABLE nvk_order_detail;
TRUNCATE TABLE nvk_cart_item;
TRUNCATE TABLE nvk_products;
TRUNCATE TABLE nvk_categories;
TRUNCATE TABLE nvk_customers;
TRUNCATE TABLE nvk_admins;

SET FOREIGN_KEY_CHECKS = 1;

-- =======================================================
-- 2. INSERT TÀI KHOẢN (ADMIN & USER MẶC ĐỊNH)
-- =======================================================
INSERT INTO nvk_admins (nvk_username, nvk_password, nvk_full_name, nvk_active, nvk_avatar)
VALUES ('admin', '123456', 'Quản Trị Viên', 1, 'admin.png');

INSERT INTO nvk_customers (nvk_email, nvk_password, nvk_full_name, nvk_phone, nvk_address, nvk_active, nvk_avatar)
VALUES ('nvk@gmail.com', '123456', 'Nguyễn Văn Khải', '0912345678', 'Hà Nội', 1, 'user1.png'),
       ('test@gmail.com', '123456', 'Khách Test', '0999888777', 'Đà Nẵng', 1, 'user2.png');

-- =======================================================
-- 3. INSERT DANH MỤC (6 LOẠI)
-- =======================================================
INSERT INTO nvk_categories (nvk_name)
VALUES ('Điện thoại'),    -- ID: 1
       ('Laptop'),        -- ID: 2
       ('Máy tính bảng'), -- ID: 3
       ('Đồng hồ'),       -- ID: 4
       ('Phụ kiện'),      -- ID: 5
       ('PC & Màn hình');
-- ID: 6

-- =======================================================
-- 4. INSERT 60 SẢN PHẨM (10 SP / DANH MỤC)
-- QUY ƯỚC:
-- nvk_status: 0 (Ngừng), 1 (Bán), 2 (Sắp về)
-- nvk_quantity: 0 (Hết hàng), >0 (Còn hàng)
-- =======================================================

-- --- DANH MỤC 1: ĐIỆN THOẠI (ID: 1) ---
INSERT INTO nvk_products (nvk_code, nvk_name, nvk_brand, nvk_price, nvk_old_price, nvk_quantity, nvk_img_url,
                          nvk_description, nvk_status, nvk_cate_id)
VALUES ('P001', 'iPhone 15 Pro Max 256GB', 'Apple', 29990000, 34990000, 50,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/i/p/iphone-15-pro-max_3.jpg',
        'Titan tự nhiên, chip A17 Pro.', 1, 1),
       ('P002', 'Samsung Galaxy S24 Ultra', 'Samsung', 26990000, 31990000, 30,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/s/s/ss-s24-ultra-xam-222.png',
        'Quyền năng AI.', 1, 1),
       ('P003', 'iPhone 16 Pro Max (Concept)', 'Apple', 0, 0, 0,
        'https://cdn.tgdd.vn/Files/2023/09/24/1544321/iphone-16-pro-max-khi-nao-ra-mat-gia-bao-nhieu-co-gi-moi-thumb-1.jpg',
        'Siêu phẩm tương lai.', 2, 1),      -- SẮP VỀ (1)
       ('P004', 'Xiaomi 14 Ultra', 'Xiaomi', 24990000, 29990000, 15,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/x/i/xiaomi-14-ultra_1_.png',
        'Camera Leica đỉnh cao.', 1, 1),
       ('P005', 'Samsung Galaxy Z Fold 6', 'Samsung', 0, 0, 0,
        'https://images.samsung.com/is/image/samsung/p6pim/vn/galaxy-z-fold6/gallery/vn-galaxy-z-fold6-f956-sm-f956bzkbxsv-542365737?$650_519_PNG$',
        'Gập mở quyền năng.', 2, 1),        -- SẮP VỀ (2)
       ('P006', 'iPhone 11 64GB', 'Apple', 8990000, 11990000, 0,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/i/p/iphone-11.jpg',
        'Huyền thoại tạm hết hàng.', 1, 1), -- HẾT HÀNG
       ('P007', 'Google Pixel 8 Pro', 'Google', 21500000, 24000000, 10,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:0:0/q:90/plain/https://cellphones.com.vn/media/catalog/product/g/o/google-pixel-8-pro_2_.png',
        'Android gốc mượt mà.', 1, 1),
       ('P008', 'Oppo Find N3 Flip', 'Oppo', 19990000, 22990000, 20,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/o/p/oppo-find-n3-flip-hong-1_1.png',
        'Gập nhỏ gọn.', 1, 1),
       ('P009', 'Vsmart Aris Pro', 'Vsmart', 4500000, 6000000, 0,
        'https://cdn.tgdd.vn/Products/Images/42/228811/vsmart-aris-pro-xanh-la-600x600.jpg', 'Đã ngừng sản xuất.', 0,
        1),                                 -- NGỪNG KD
       ('P010', 'Sony Xperia 1 V', 'Sony', 28990000, 32990000, 5,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:0:0/q:90/plain/https://cellphones.com.vn/media/catalog/product/s/o/sony-xperia-1-v_1.png',
        'Màn hình 4K.', 1, 1);

-- --- DANH MỤC 2: LAPTOP (ID: 2) ---
INSERT INTO nvk_products (nvk_code, nvk_name, nvk_brand, nvk_price, nvk_old_price, nvk_quantity, nvk_img_url,
                          nvk_description, nvk_status, nvk_cate_id)
VALUES ('L001', 'MacBook Air M2 2022', 'Apple', 24590000, 28990000, 100,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/m/a/macbook-air-m2-midnight.png',
        'Mỏng nhẹ, pin trâu.', 1, 2),
       ('L002', 'Dell XPS 13 Plus', 'Dell', 45000000, 50000000, 10,
        'https://laptopvang.com/wp-content/uploads/2023/04/Dell-XPS-9320-Plus-2023-Platinum-LaptopVANG-6.jpg',
        'Thiết kế tương lai.', 1, 2),
       ('L003', 'Asus ROG Zephyrus G14', 'Asus', 38990000, 42990000, 0, 'https://rog.asus.com/media/1673898656626.jpg',
        'Gaming 14 inch mạnh nhất.', 1, 2), -- HẾT HÀNG
       ('L004', 'MacBook Pro M4 (Pre-order)', 'Apple', 0, 0, 0,
        'https://macone.vn/wp-content/uploads/2024/05/Chip-Apple-M4-Ra-mat.jpg', 'Sức mạnh hủy diệt.', 2,
        2),                                 -- SẮP VỀ (3)
       ('L005', 'Lenovo ThinkPad X1 Carbon', 'Lenovo', 41000000, 45000000, 12,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:0:0/q:90/plain/https://cellphones.com.vn/media/catalog/product/l/e/lenovo-thinkpad-x1-carbon-gen-9-1.png',
        'Bền bỉ chuẩn doanh nhân.', 1, 2),
       ('L006', 'HP Spectre x360', 'HP', 35990000, 39990000, 8,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:0:0/q:90/plain/https://cellphones.com.vn/media/catalog/product/h/p/hp-spectre-x360-14-ef0030tu-6k773pa-2.png',
        'Xoay gập 360 độ.', 1, 2),
       ('L007', 'Acer Nitro 5 Tiger', 'Acer', 19990000, 24990000, 50,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/l/a/laptop-acer-nitro-5-tiger-an515-58-773y.png',
        'Gaming quốc dân.', 1, 2),
       ('L008', 'MSI Raider GE78', 'MSI', 70000000, 80000000, 2,
        'https://vn-store.msi.com/cdn/shop/files/Raider-GE78-HX_13V_1.png?v=1685608006', 'Quái vật hiệu năng.', 1, 2),
       ('L009', 'Laptop Cũ Giá Rẻ', 'NoBrand', 2000000, 0, 0,
        'https://laptoptcc.com/wp-content/uploads/2020/06/laptop-cu-gia-re.jpg', 'Ngừng kinh doanh dòng này.', 0,
        2),                                 -- NGỪNG KD
       ('L010', 'Surface Pro 10', 'Microsoft', 0, 0, 0,
        'https://surfacehcm.vn/wp-content/uploads/2024/03/surface-pro-10-business-platinum-1.jpg', 'AI PC thế hệ mới.',
        2, 2);
-- SẮP VỀ (4)

-- --- DANH MỤC 3: MÁY TÍNH BẢNG (ID: 3) ---
INSERT INTO nvk_products (nvk_code, nvk_name, nvk_brand, nvk_price, nvk_old_price, nvk_quantity, nvk_img_url,
                          nvk_description, nvk_status, nvk_cate_id)
VALUES ('T001', 'iPad Pro M4 13 inch', 'Apple', 35990000, 39990000, 20,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/i/p/ipad-pro-13-m4-2024-5g-256gb_1.png',
        'Mỏng không tưởng.', 1, 3),
       ('T002', 'Samsung Galaxy Tab S9 Ultra', 'Samsung', 29990000, 34990000, 15,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/s/a/samsung-galaxy-tab-s9-ultra-1.png',
        'Màn hình khổng lồ.', 1, 3),
       ('T003', 'Xiaomi Pad 6', 'Xiaomi', 8990000, 10990000, 40,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/x/i/xiaomi-pad-6_1.png',
        'Ngon bổ rẻ.', 1, 3),
       ('T004', 'iPad Gen 9', 'Apple', 6990000, 8990000, 100,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/i/p/ipad-10-2-2021-2.png',
        'iPad quốc dân.', 1, 3),
       ('T005', 'iPad Mini 7', 'Apple', 0, 0, 0,
        'https://vtv1.mediacdn.vn/thumb_w/640/562122370168008704/2023/10/11/ipad-mini-7-purple-1697011666498522770267.jpg',
        'Nhỏ nhưng có võ.', 2, 3),  -- SẮP VỀ (5)
       ('T006', 'Samsung Tab A9+', 'Samsung', 5490000, 6490000, 25,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/s/a/samsung-galaxy-tab-a9-plus_1_.png',
        'Giải trí cơ bản.', 1, 3),
       ('T007', 'Lenovo Tab P11 Pro', 'Lenovo', 9990000, 11990000, 10,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/t/a/tab_p11_pro_gen_2_1.png',
        'Âm thanh JBL.', 1, 3),
       ('T008', 'iPad Air 4', 'Apple', 14990000, 16990000, 0,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:0:0/q:90/plain/https://cellphones.com.vn/media/catalog/product/i/p/ipad-air-4-blue-2020.jpg',
        'Đời cũ ngừng bán.', 0, 3), -- NGỪNG KD
       ('T009', 'Huawei MatePad 11.5', 'Huawei', 6990000, 7990000, 12,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/h/u/huawei-matepad-11-5-inch_2.png',
        'Phần cứng ngon.', 1, 3),
       ('T010', 'Kindle Scribe', 'Amazon', 10500000, 0, 5,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:0:0/q:90/plain/https://cellphones.com.vn/media/catalog/product/m/a/may-doc-sach-kindle-scribe-16gb_2.png',
        'Vừa đọc vừa viết.', 1, 3);

-- --- DANH MỤC 4: ĐỒNG HỒ (ID: 4) ---
INSERT INTO nvk_products (nvk_code, nvk_name, nvk_brand, nvk_price, nvk_old_price, nvk_quantity, nvk_img_url,
                          nvk_description, nvk_status, nvk_cate_id)
VALUES ('W001', 'Apple Watch Ultra 2', 'Apple', 21990000, 23990000, 10,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/w/a/watch-ultra-2-ocean_1.png',
        'Siêu bền bỉ.', 1, 4),
       ('W002', 'Garmin Fenix 7 Pro', 'Garmin', 25990000, 29990000, 5,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/g/a/garmin-fenix-7-pro-solar_2_.png',
        'Pin năng lượng mặt trời.', 1, 4),
       ('W003', 'Apple Watch Series 9', 'Apple', 10490000, 11990000, 30,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/a/p/apple-watch-s9-41mm-4g-vien-thep-day-thep-vang_1.png',
        'Chip S9 mạnh mẽ.', 1, 4),
       ('W004', 'Samsung Galaxy Watch 6', 'Samsung', 6490000, 7490000, 40,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/s/a/samsung-galaxy-watch-6-classic-43mm-bluetooth_1_.png',
        'Viền xoay vật lý.', 1, 4),
       ('W005', 'Apple Watch X (Concept)', 'Apple', 0, 0, 0,
        'https://img.global.news.samsung.com/vn/wp-content/uploads/2024/07/Galaxy-Watch-Ultra-Titanium-White-Perspective-Front.jpg',
        'Thiết kế từ tính.', 2, 4), -- SẮP VỀ (6)
       ('W006', 'Huawei Watch GT 4', 'Huawei', 4990000, 5990000, 20,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/d/o/dong-ho-thong-minh-huawei-watch-gt4-46mm-day-da_1_.png',
        'Pin 2 tuần.', 1, 4),
       ('W007', 'Xiaomi Band 8', 'Xiaomi', 890000, 990000, 100,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/x/i/xiaomi-mi-band-8_1_1.png',
        'Màn hình 60Hz.', 1, 4),
       ('W008', 'Coros Pace 3', 'Coros', 5990000, 0, 0,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/d/o/dong-ho-thong-minh-coros-pace-3_2_.png',
        'Tạm hết hàng.', 1, 4),     -- HẾT HÀNG
       ('W009', 'Đồng hồ cơ cổ lỗ sĩ', 'NoBrand', 100000, 0, 0,
        'https://upload.wikimedia.org/wikipedia/commons/thumb/b/b3/Pocket_watch_with_chain.jpg/800px-Pocket_watch_with_chain.jpg',
        'Không bán nữa.', 0, 4),    -- NGỪNG KD
       ('W010', 'Casio G-Shock GSS', 'Casio', 3500000, 0, 15,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:0:0/q:90/plain/https://cellphones.com.vn/media/catalog/product/d/o/dong-ho-casio-g-shock-ga-2100-1a1dr_1.png',
        'Nồi đồng cối đá.', 1, 4);

-- --- DANH MỤC 5: PHỤ KIỆN (ID: 5) ---
INSERT INTO nvk_products (nvk_code, nvk_name, nvk_brand, nvk_price, nvk_old_price, nvk_quantity, nvk_img_url,
                          nvk_description, nvk_status, nvk_cate_id)
VALUES ('A001', 'AirPods Pro 2 USB-C', 'Apple', 5990000, 6990000, 50,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/a/i/airpods-pro-2_1_1.png',
        'Chống ồn chủ động.', 1, 5),
       ('A002', 'Sạc dự phòng Anker 737', 'Anker', 2990000, 3500000, 20,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/p/i/pin-sac-du-phong-anker-737-gan-prime-24000mah-200w-a1289_1.png',
        'Sạc nhanh 140W.', 1, 5),
       ('A003', 'Sony WH-1000XM5', 'Sony', 7490000, 8490000, 15,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/t/a/tai-nghe-chup-tai-khong-day-sony-wh-1000xm5_3_.png',
        'Chống ồn hay nhất.', 1, 5),
       ('A004', 'Sony WH-1000XM6', 'Sony', 0, 0, 0,
        'https://image.webuy.vn/2024/6/17/sony-wh-1000xm6-1-17186008803521021703649.jpg', 'Thế hệ tiếp theo.', 2,
        5), -- SẮP VỀ (7)
       ('A005', 'Chuột Logitech MX Master 3S', 'Logitech', 2490000, 2990000, 30,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/c/h/chuot-khong-day-logitech-mx-master-3s_1_.png',
        'Chuột văn phòng tốt nhất.', 1, 5),
       ('A006', 'Bàn phím Keychron K8 Pro', 'Keychron', 2190000, 2590000, 25,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/b/a/ban-phim-co-keychron-k8-pro-nhom-rgb-hot-swap_1_.png',
        'Gõ sướng tay.', 1, 5),
       ('A007', 'Củ sạc Ugreen Nexode 65W', 'Ugreen', 690000, 990000, 60,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/c/u/cu-sac-nhanh-ugreen-gan-65w-3-cong-10335_2.png',
        'Nhỏ gọn.', 1, 5),
       ('A008', 'Tai nghe dây cũ', 'NoBrand', 50000, 0, 0,
        'https://down-vn.img.susercontent.com/file/409d94944510069695d85202860d5b62', 'Ngừng kinh doanh.', 0,
        5), -- NGỪNG KD
       ('A009', 'Ba lô Tomtoc 360', 'Tomtoc', 1200000, 1500000, 15,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/b/a/balo-tomtoc-casual-a60-laptop-15-6-16-inch-a60-e01d_2_.png',
        'Chống sốc tốt.', 1, 5),
       ('A010', 'Dây cáp Baseus 100W', 'Baseus', 150000, 250000, 100,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/c/a/cap-sac-baseus-crystal-shine-series-fast-charging-data-cable-type-c-to-type-c-100w-1-2m-xanh_2_.png',
        'Siêu bền.', 1, 5);

-- --- DANH MỤC 6: PC & MÀN HÌNH (ID: 6) ---
INSERT INTO nvk_products (nvk_code, nvk_name, nvk_brand, nvk_price, nvk_old_price, nvk_quantity, nvk_img_url,
                          nvk_description, nvk_status, nvk_cate_id)
VALUES ('C001', 'PC Gaming NVK Ultra', 'NVK Build', 35000000, 40000000, 5,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/p/c/pc-cps-gaming-g3_2.png',
        'Cấu hình khủng.', 1, 6),
       ('C002', 'Màn hình LG 27GR95QE OLED', 'LG', 19990000, 24990000, 8,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/m/a/man-hinh-gaming-lg-ultragear-oled-27gr95qe-b-27-inch_3_.png',
        'OLED 240Hz.', 1, 6),
       ('C003', 'Card RTX 5090 (Founder Ed)', 'NVIDIA', 0, 0, 0,
        'https://file.hstatic.net/200000722513/article/thiet-ke-geforce-rtx-5090-lo-dien-chiem-den-4-khe-cam-3-quat-tan-nhiet-va-pcb-moi_7633e9d8e7c140c88820c7cc6ec8e146.jpg',
        'Hủy diệt mọi game.', 2, 6), -- SẮP VỀ (8 - Chốt sổ)
       ('C004', 'Màn hình Dell UltraSharp', 'Dell', 13500000, 15500000, 12,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/m/a/man-hinh-dell-ultrasharp-u2723qe-27-inch-4k-ips_2.png',
        'Chuẩn màu đồ họa.', 1, 6),
       ('C005', 'PC Văn phòng Đồng bộ HP', 'HP', 8990000, 0, 20,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/m/a/may-tinh-dong-bo-hp-280-pro-g9-sff-72l52pa_1.png',
        'Bền bỉ.', 1, 6),
       ('C006', 'Màn hình Asus ProArt', 'Asus', 11000000, 12500000, 10,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/m/a/man-hinh-asus-proart-pa278qv-27-inch-2k-ips-75hz-chuyen-do-hoa_2.png',
        'Chuyên thiết kế.', 1, 6),
       ('C007', 'Màn hình CRT Cổ', 'Samsung', 500000, 0, 0,
        'https://upload.wikimedia.org/wikipedia/commons/thumb/1/1d/Samsung_SyncMaster_551v.jpg/1200px-Samsung_SyncMaster_551v.jpg',
        'Đồ cổ ngừng bán.', 0, 6),   -- NGỪNG KD
       ('C008', 'PC Mini Mac Mini M2', 'Apple', 14990000, 16990000, 15,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/m/a/mac-mini-m2-2023_2_.png',
        'Nhỏ gọn mạnh mẽ.', 1, 6),
       ('C009', 'Vỏ case NZXT H9 Flow', 'NZXT', 4500000, 0, 0,
        'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/c/a/case-nzxt-h9-flow-mid-tower-trang_2_.png',
        'Tạm hết hàng.', 1, 6),      -- HẾT HÀNG
       ('C010', 'Ghế Herman Miller', 'Herman', 30000000, 0, 2,
        'https://themansion.vn/uploads/source/herman-miller/aeron/aeron-onyx-01.jpg', 'Bảo vệ cột sống.', 1, 6);

-- =======================================================
-- 5. INSERT BÌNH LUẬN (DEMO 2 CÁI CHO VUI)
-- =======================================================
INSERT INTO nvk_reviews (nvk_rating, nvk_comment, nvk_created_date, nvk_customer_id, nvk_product_id)
VALUES (5, 'Máy đẹp xuất sắc, giao hàng nhanh.', CURDATE(), 1, 1),
       (4, 'Dùng tốt nhưng hơi nóng khi chơi game nặng.', CURDATE(), 2, 2);