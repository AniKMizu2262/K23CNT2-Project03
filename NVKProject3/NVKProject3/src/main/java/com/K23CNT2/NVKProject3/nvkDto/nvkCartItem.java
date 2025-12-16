package com.K23CNT2.NVKProject3.nvkDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class nvkCartItem {
    private Long productId;
    private String productName;
    private String productImg;
    private double price;
    private int quantity;

    // Tính thành tiền của món này
    public double getTotalPrice() {
        return price * quantity;
    }
}