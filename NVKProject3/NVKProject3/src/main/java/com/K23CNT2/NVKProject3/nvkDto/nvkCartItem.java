package com.K23CNT2.NVKProject3.nvkDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) đại diện cho một mục trong giỏ hàng.
 * Class này không tương tác trực tiếp với Database, chỉ dùng để lưu trữ tạm thời trong Session.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class nvkCartItem {

    private Long productId;
    private String productName;
    private String productImg;
    private double price;
    private int quantity;

    /**
     * Tính thành tiền của mục này (Đơn giá * Số lượng).
     * Được sử dụng để hiển thị tổng tiền cho từng dòng trong trang Giỏ hàng.
     */
    public double getTotalPrice() {
        return price * quantity;
    }
}