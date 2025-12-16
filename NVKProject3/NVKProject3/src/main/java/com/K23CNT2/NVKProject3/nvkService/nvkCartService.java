package com.K23CNT2.NVKProject3.nvkService;

import com.K23CNT2.NVKProject3.nvkDto.nvkCartItem;
import com.K23CNT2.NVKProject3.nvkEntity.nvkProduct;
import com.K23CNT2.NVKProject3.nvkRepository.nvkProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class nvkCartService {

    @Autowired
    private nvkProductRepository productRepo;

    private static final String CART_SESSION_KEY = "nvkCart";

    // 1. Lấy giỏ hàng từ Session
    public List<nvkCartItem> getCart(HttpSession session) {
        List<nvkCartItem> cart = (List<nvkCartItem>) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    // 2. Thêm sản phẩm vào giỏ
    public void addToCart(Long productId, int quantity, HttpSession session) {
        List<nvkCartItem> cart = getCart(session);
        nvkProduct product = productRepo.findById(productId).orElse(null);

        if (product != null) {
            // Kiểm tra xem sản phẩm đã có trong giỏ chưa
            for (nvkCartItem item : cart) {
                if (item.getProductId().equals(productId)) {
                    item.setQuantity(item.getQuantity() + quantity); // Cộng dồn số lượng
                    return;
                }
            }
            // Nếu chưa có thì thêm mới
            nvkCartItem newItem = new nvkCartItem(
                    product.getNvkId(),
                    product.getNvkName(),
                    product.getNvkImgUrl(),
                    product.getNvkPrice(),
                    quantity
            );
            cart.add(newItem);
        }
    }

    // 3. Xóa sản phẩm khỏi giỏ
    public void removeFromCart(Long productId, HttpSession session) {
        List<nvkCartItem> cart = getCart(session);
        cart.removeIf(item -> item.getProductId().equals(productId));
    }

    // 4. Tính tổng tiền
    public double getTotalAmount(HttpSession session) {
        List<nvkCartItem> cart = getCart(session);
        return cart.stream().mapToDouble(nvkCartItem::getTotalPrice).sum();
    }

    // 5. Xóa sạch giỏ (sau khi thanh toán)
    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }
}